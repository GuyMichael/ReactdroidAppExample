package com.guymichael.componentapplicationexample.ui.qrcode.component.model

import android.view.SurfaceHolder
import java.lang.ref.WeakReference

class SurfaceChangeInfo(
    val holder: WeakReference<SurfaceHolder>,
    val format: Int,
    val width: Int,
    val height: Int
)