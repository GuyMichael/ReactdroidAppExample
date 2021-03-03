package com.guymichael.componentapplicationexample.ui.qrcode.component

import android.util.SparseArray
import com.google.android.gms.vision.barcode.Barcode
import com.guymichael.componentapplicationexample.ui.qrcode.component.model.SurfaceChangeInfo
import com.guymichael.kotlinreact.model.OwnProps

/** @param init_onScanSucceed consumer for barCodes - never empty when invoked */
data class QRScannerProps(
        val init_onScanSucceed: (barCodes: SparseArray<Barcode>) -> Unit,
        /** This is called immediately after any structural changes (format or size) have been made
         * to the surface. You should at this point update the imagery in the surface.
         * This method is always called at least once (after camera started),
         * and when the component is mounted */
        val init_onSurfaceChanged: ((SurfaceChangeInfo) -> Unit)? = null,
        val init_onErrorStartingCamera: ((e: RuntimeException) -> Unit)? = null
    ) : OwnProps() {

    override fun getAllMembers() = emptyList<Any?>()
}