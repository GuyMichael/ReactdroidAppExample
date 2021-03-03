package com.guymichael.componentapplicationexample.ui.qrcode.component

import android.annotation.SuppressLint
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.annotation.IdRes
import androidx.core.util.isNotEmpty
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.guymichael.apromise.APromise
import com.guymichael.componentapplicationexample.ui.qrcode.component.model.SurfaceChangeInfo
import com.guymichael.kotlinreact.model.ownstate.BooleanState
import com.guymichael.kotlinreact.setState
import com.guymichael.reactdroid.core.ViewUtils
import com.guymichael.reactdroid.core.activity.ComponentActivity
import com.guymichael.reactdroid.core.fragment.ComponentFragment
import com.guymichael.reactdroid.core.model.AComponent
import com.guymichael.reactdroid.core.withAutoCancel
import com.guymichael.reactdroid.core.withGlobalErrorHandling
import io.reactivex.rxjava3.schedulers.Schedulers
import java.lang.ref.WeakReference

/**
 * Requires camera permissions to show. See `WithPermissions` HOC component
 */
class CQRScanner(v: SurfaceView) : AComponent<QRScannerProps, BooleanState, SurfaceView>(v) {
    override fun createInitialState(props: QRScannerProps) = BooleanState(false) //whether or not surface is ready

    private val barcodeDetector = BarcodeDetector.Builder(mView.context)
        .setBarcodeFormats(Barcode.ALL_FORMATS)
        .build()

    private val cameraSource = CameraSource.Builder(mView.context, barcodeDetector)
        .setAutoFocusEnabled(true)
        .build()

    private var isCameraActive = false
    private var isHoldingCamera = false

    //cache to use between surface ready and actual onMount,
    // because we have no way of force-invoking the callback after it was fired
    private var pendingSurfaceChangedInfo: SurfaceChangeInfo? = null
    private var pendingSurfaceReady: Boolean = false

    private val cameraScheduler by lazy { Schedulers.io() }

    //this callback uses our props, so it must be used after componentDidMount or we'll have a race condition
    private val mSurfaceHolderCallback by lazy { object : SurfaceHolder.Callback {
        override fun surfaceCreated(holder: SurfaceHolder) {
//            Log.e("QRScanner", "on surface changed - actual")
            //we want to wait for actual user-visibility of the camera before starting,
            // e.g. when inside a list (recycler) or a pager, where views are mounted before shown
            //THINK create a HOC to do that
            ViewUtils.waitForViewOnScreen(mView, false, 0).then {
                if (isPassedOrDuringRender()) {
//                    Log.e("QRScanner", "on surface changed - actual - already rendered")
                    setState(true)
                } else {
//                    Log.e("QRScanner", "on surface changed - actual - caching")
                    pendingSurfaceReady = true
                }
            }
            .withAutoCancel(this@CQRScanner)
            .withGlobalErrorHandling(this@CQRScanner)
            .execute()
        }
        override fun surfaceDestroyed(holder: SurfaceHolder) {
//            Log.e("QRScanner", "surface destroyed - actual")
            if (isPassedOrDuringRender() && ownState.value) {
//                Log.e("QRScanner", "surface destroyed - settings state false")
                setState(false)
            }
        }
        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            val info = SurfaceChangeInfo(WeakReference(holder), format, width, height)
//            Log.e("QRScanner", "surface changed - actual")
            if (isPassedOrDuringRender()) {
//                Log.e("QRScanner", "surface changed - actual - already rendered")
                props.init_onSurfaceChanged?.invoke(info)
            } else {
//                Log.e("QRScanner", "surface changed - actual - caching")
                pendingSurfaceChangedInfo = info
            }
        }
    }}


    init {
        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {
                //To prevent memory leaks barcode scanner has been stopped
                //THINK
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                detections.detectedItems.takeIf { it.isNotEmpty() }?.also {
                    mView.post { props.init_onScanSucceed.invoke(it) }
                }
            }
        })

        mView.holder.addCallback(mSurfaceHolderCallback)
    }

    override fun componentDidMount() {
//        Log.e("QRScanner", "didMount")
        //update local state if surface is already ready at this point
        if (pendingSurfaceReady && !ownState.value) {
//            Log.e("QRScanner", "didMount - setting state true")
            setState(true)
        }

        //notify surface callback now if surface already ready at this point
        props.init_onSurfaceChanged?.also { callback ->
        pendingSurfaceChangedInfo?.also { info ->
//            Log.e("QRScanner", "onMount - notifying callback")
            callback.invoke(info)
        }}
    }

    override fun componentWillUnmount() { //this is called after surfaceDestroyed() making the camera stop before released
        mView.holder.removeCallback(mSurfaceHolderCallback)
        releaseCamera()
        pendingSurfaceChangedInfo = null
        pendingSurfaceReady = false
    }


    @SuppressLint("MissingPermission")
    private fun startCamera(props: QRScannerProps) {
        if( !isCameraActive && mView.context != null) { //should never be null here - called from render()
            APromise.on(cameraScheduler).then {
                try {
                    cameraSource.start(mView.holder)
                    isHoldingCamera = true
                    isCameraActive = true

                } catch (e: RuntimeException) {
                    //Exception configuring surface
                    //THINK. So far it happened when it should (e.g. a camera-list-item is recycled)
                    props.init_onErrorStartingCamera?.also {
                        APromise.postAtEndOfMainExecutionQueue {
                            it.invoke(e)
                        }
                    }
                }
            }
            .withAutoCancel(this)
            .execute()
        }
    }

    private fun stopCamera() {
        if (isCameraActive) {
            isCameraActive = false

            if (mView.context != null) {
                APromise.on(cameraScheduler).then {
                    try {
                        cameraSource.stop()
                    } catch (e: Exception) {
                        //some error, assume camera not started
                        e.printStackTrace()
                    }
                }
                .withAutoCancel(this)
                .execute()
            }
        }
    }

    private fun releaseCamera() {
        if (isHoldingCamera) {
            if (mView.context != null) {
                APromise.on(cameraScheduler).then {
                    try {
                        cameraSource.release()
                    } catch (e: Exception) {
                        //some error, assume camera released
                        e.printStackTrace()
                    }
                }
                .withAutoCancel(this)
                .execute()
            }
            isCameraActive = false
            isHoldingCamera = false
        }
    }



    override fun render() {
        mView.holder.surface?.takeIf { it.isValid }?.also {
            if (ownState.value) { //surface ready
                startCamera(this.props)
            } else {
                stopCamera()
            }
        }
    }
}





fun AComponent<*, *, *>.withQRCodeScanner(@IdRes id: Int) = CQRScanner(mView.findViewById(id))
fun ComponentFragment<*>.withQRCodeScanner(@IdRes id: Int) = CQRScanner(view!!.findViewById(id))
fun ComponentActivity<*>.withQRCodeScanner(@IdRes id: Int) = CQRScanner(findViewById(id))