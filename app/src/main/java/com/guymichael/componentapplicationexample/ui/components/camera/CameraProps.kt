package com.guymichael.componentapplicationexample.ui.components.camera

import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import com.guymichael.kotlinreact.model.OwnProps

data class CameraProps(
        val cameraSelector: () -> CameraSelector = { CameraLogic.defaultCameraSelector() }
        , val imageCapture: ((CameraSelector) -> ImageCapture)? = null
        , @AspectRatio.Ratio val aspectRatio: Int = AspectRatio.RATIO_16_9
        , val analyzers: List<ImageAnalysis> = emptyList()//doesn't affect re-renders
    ) : OwnProps() {

    override fun getAllMembers() = listOf(
        aspectRatio //NOCOMMIT
    )
}