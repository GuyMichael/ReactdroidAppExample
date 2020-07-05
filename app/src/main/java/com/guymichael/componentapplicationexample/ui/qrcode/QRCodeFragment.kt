package com.guymichael.componentapplicationexample.ui.qrcode

import android.view.View
import com.guymichael.componentapplicationexample.R
import com.guymichael.componentapplicationexample.ui.components.camera.CameraLogic
import com.guymichael.kotlinreact.model.EmptyOwnProps
import com.guymichael.reactdroid.core.model.AComponent
import com.guymichael.reactdroid.extensions.components.permissions.PermissionProps
import com.guymichael.reactdroid.extensions.components.permissions.withPermissions
import com.guymichael.reactiveapp.fragments.BaseFragment

class QRCodeFragment : BaseFragment
<EmptyOwnProps, AComponent<PermissionProps<EmptyOwnProps>, *, *>, PermissionProps<EmptyOwnProps>>() {
    override fun getLayoutRes() = R.layout.fragment_qr
    override fun createPageComponent(layout: View) = withPermissions(QrCodePage(layout))
    override fun mapFragmentPropsToPageProps(props: EmptyOwnProps) = PermissionProps(
        CameraLogic.REQUIRED_PERMISSIONS.toList()
        , EmptyOwnProps
        , initial_goToAppSettingsIfAlwaysDeny = true
    )
    override fun createDefaultProps() = EmptyOwnProps
}