package com.guymichael.componentapplicationexample.ui.camera

import android.view.View
import com.guymichael.componentapplicationexample.R
import com.guymichael.componentapplicationexample.ui.components.camera.CameraLogic
import com.guymichael.componentapplicationexample.ui.components.camera.CameraProps
import com.guymichael.kotlinreact.model.EmptyOwnProps
import com.guymichael.reactdroid.core.model.AComponent
import com.guymichael.reactdroid.extensions.components.permissions.PermissionProps
import com.guymichael.reactdroid.extensions.components.permissions.withPermissions
import com.guymichael.reactiveapp.fragments.BaseFragment

class CameraFragment : BaseFragment
<EmptyOwnProps, AComponent<PermissionProps<CameraProps>, *, *>, PermissionProps<CameraProps>>() {

    override fun getLayoutRes() = R.layout.fragment_camera
    //note: we can use convenience method 'CameraPage.withPermissions()' instead of these two methods:
    override fun createPageComponent(layout: View) = withPermissions(CameraPage(layout))
    override fun mapFragmentPropsToPageProps(props: EmptyOwnProps) = PermissionProps(
        CameraLogic.REQUIRED_PERMISSIONS.toList()
        , CameraProps()
    )
    override fun createDefaultProps() = EmptyOwnProps
}