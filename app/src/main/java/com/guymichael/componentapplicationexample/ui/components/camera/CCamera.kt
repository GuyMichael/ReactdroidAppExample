package com.guymichael.componentapplicationexample.ui.components.camera

import androidx.annotation.IdRes
import androidx.camera.core.CameraXConfig
import androidx.camera.view.CameraView
import androidx.lifecycle.LifecycleOwner
import com.guymichael.kotlinreact.model.ownstate.BooleanState
import com.guymichael.reactdroid.core.Utils
import com.guymichael.reactdroid.core.model.AComponent
import com.guymichael.reactdroid.core.withAutoCancel
import com.guymichael.reactiveapp.BuildConfig

/**
 * A component for androidx's camera-x (uses Android's camera2 internally).
 *
 * Note: it is up to you to validate permissions (`Manifest.permission.CAMERA`)
 *
 * @see CameraLogic.REQUIRED_PERMISSIONS
 */
class CCamera(v: CameraView) : AComponent<CameraProps, BooleanState, CameraView>(v) {

    private var isCameraActive = false
    private var isHoldingCamera = false

    init {
        if (BuildConfig.DEBUG) {
            Utils.getActivity(v)!!.also {
                when (it) {
                    !is LifecycleOwner
                    -> throw IllegalArgumentException(CCamera::class.simpleName +
                        " must have a parent 'Activity' which implements 'LifecycleOwner'"
                    )

                    !is CameraXConfig.Provider
                    -> throw IllegalArgumentException(CCamera::class.simpleName +
                        " must have a parent 'Activity' which implements 'CameraXConfig.Provider'"
                    )
                }
            }
        }
    }

    override fun createInitialState(props: CameraProps) = BooleanState(false) //is surface ready

    override fun componentDidMount() {
        //TODO for tests only
        /*CameraLogic.initExtensions()
            .thenAwaitWithContextOrCancel(Utils.getActivity(mView.context)!!) { context, _ ->
                if (CameraX.isInitialized()) APromise.of()
                else APromise.from( CameraX.initialize(context, Camera2Config.defaultConfig()) )
                    .thenMap { Unit }
            }
            .then {
                setState(true)
            }
            .execute(this)*/
    }

    override fun componentWillUnmount() {
        releaseCamera()
    }




    private fun startCamera() {
        if( !isCameraActive) {
            isHoldingCamera = true
            isCameraActive = true

            if (mView.context != null) {
                props.cameraSelector.invoke().also {
                    CameraLogic.startCamera(mView
                        , it
                        , props.imageCapture?.invoke(it)
                        , props.aspectRatio
                    )
                    .withAutoCancel(this)
                    .execute()
                }
            }
        }
    }

    private fun stopCamera() {
        if (isCameraActive) {
            isCameraActive = false
//            CameraX.shutdown() TODO
        }
    }

    private fun releaseCamera() {
        if (isHoldingCamera) {
//            CameraX.shutdown() TODO
            isCameraActive = false
            isHoldingCamera = false
        }
    }




    override fun render() {
        if (this.ownState.value) {
            //surface ready
            startCamera()
        } else {
            stopCamera()
        }
    }
}




fun AComponent<*, *, *>.withCamera(@IdRes id: Int) = CCamera(mView.findViewById(id))