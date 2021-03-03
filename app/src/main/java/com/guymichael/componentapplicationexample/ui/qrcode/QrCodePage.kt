package com.guymichael.componentapplicationexample.ui.qrcode

import android.util.SparseArray
import android.view.View
import com.google.android.gms.vision.barcode.Barcode
import com.guymichael.componentapplicationexample.R
import com.guymichael.componentapplicationexample.ui.components.camera.CameraLogic
import com.guymichael.componentapplicationexample.ui.qrcode.component.QRScannerProps
import com.guymichael.componentapplicationexample.ui.qrcode.component.model.SurfaceChangeInfo
import com.guymichael.componentapplicationexample.ui.qrcode.component.withQRCodeScanner
import com.guymichael.kotlinreact.model.EmptyOwnProps
import com.guymichael.kotlinreact.model.EmptyOwnState
import com.guymichael.reactdroid.core.model.AComponent
import com.guymichael.reactdroid.core.model.AHOC
import com.guymichael.reactdroid.extensions.animation.AnimUtils
import com.guymichael.reactdroid.extensions.components.permissions.PermissionProps
import com.guymichael.reactdroid.extensions.components.permissions.WithPermissions
import com.guymichael.reactiveapp.utils.AndroidUtils

class QrCodePage(v: View) : AComponent<EmptyOwnProps, EmptyOwnState, View>(v) {
    override fun createInitialState(props: EmptyOwnProps) = EmptyOwnState

    private val cQRScanner = withQRCodeScanner(R.id.surface_view)

    private val vDissolveForeground = v.findViewById<View>(R.id.dissolve_foreground)


    private fun onScanSucceed(barCodes: SparseArray<Barcode>) {
        AndroidUtils.toast(mView.context, barCodes.valueAt(0).displayValue)
    }

    private fun onSurfaceChanged(holder: SurfaceChangeInfo) {
        //reveal the camera slowly (called on camera start and size changes) by dissolving the foreground
        vDissolveForeground.also {
            it.alpha = 1F
            AnimUtils.animateFadeOut(it, 350, startDelay = 350).execute()
        }
    }


    override fun render() {
        cQRScanner.onRender(QRScannerProps(
            ::onScanSucceed, ::onSurfaceChanged
        ))
    }




    companion object {
        //chains 3 components (from bottom to top):
        // 1. QrCodePage (at bottom)
        // 2. WithPermissions - to require (some) permissions from user
        // 3. HOC (anonymous class) to deliver relevant (camera) permissions to #2
        fun withPermissions(v: View): AComponent<EmptyOwnProps, *, *> {
            return AHOC.from(WithPermissions(QrCodePage(v))) {
                PermissionProps(
                    CameraLogic.REQUIRED_PERMISSIONS.toList()
                    , EmptyOwnProps
                    , initial_goToAppSettingsIfAlwaysDeny = true
                )
            }
        }
    }
}