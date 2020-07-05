package com.guymichael.componentapplicationexample.ui.camera

import android.view.View
import com.guymichael.componentapplicationexample.R
import com.guymichael.componentapplicationexample.ui.components.camera.CameraLogic
import com.guymichael.componentapplicationexample.ui.components.camera.CameraProps
import com.guymichael.componentapplicationexample.ui.components.camera.withCamera
import com.guymichael.kotlinreact.model.EmptyOwnProps
import com.guymichael.kotlinreact.model.EmptyOwnState
import com.guymichael.reactdroid.core.model.AComponent
import com.guymichael.reactdroid.core.model.AHOC
import com.guymichael.reactdroid.extensions.components.permissions.PermissionProps
import com.guymichael.reactdroid.extensions.components.permissions.WithPermissions

class CameraPage(v: View) : AComponent<CameraProps, EmptyOwnState, View>(v) {
    override fun createInitialState(props: CameraProps) = EmptyOwnState

    private val cCamera = withCamera(R.id.camera_view)

    override fun render() {
        cCamera.onRender(props)
    }



    companion object {
        //chains 3 components (from bottom to top):
        // 1. CameraPage (at bottom)
        // 2. WithPermissions - to require (some) permissions from user
        // 3. HOC (anonymous class) to deliver relevant (camera) permissions to #2
        fun withPermissions(v: View): AComponent<EmptyOwnProps, *, *> {
            return AHOC.from(WithPermissions(CameraPage(v))) {
                PermissionProps(
                    CameraLogic.REQUIRED_PERMISSIONS.toList()
                    , CameraProps()
                )
            }
        }
    }
}