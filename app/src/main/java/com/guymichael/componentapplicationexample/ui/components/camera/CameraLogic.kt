package com.guymichael.componentapplicationexample.ui.components.camera

import android.Manifest
import androidx.camera.core.*
import androidx.camera.extensions.BokehImageCaptureExtender
import androidx.camera.extensions.ExtensionsManager
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.CameraView
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import com.guymichael.apromise.APromise
import com.guymichael.reactdroid.core.Utils
import java.util.concurrent.Executors


object CameraLogic {
    val REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA
//        "android.permission.WRITE_EXTERNAL_STORAGE"
    )

    private var extensionsInitialized = false


    fun initExtensions(): APromise<Unit> {
        return if (extensionsInitialized) APromise.of() else {
            APromise.from(ExtensionsManager.init())
                .thenMap {
                    extensionsInitialized = true
                }
        }
    }



    fun startCamera(previewView: PreviewView
            , cameraSelector: CameraSelector = defaultCameraSelector()
            , imageCapture: ImageCapture? = null
            , @AspectRatio.Ratio aspectRatio: Int = AspectRatio.RATIO_16_9
            , lifecycleOwner: LifecycleOwner = Utils.getActivity(previewView)!! as LifecycleOwner
            , vararg analyzers: ImageAnalysis
        ): APromise<Camera> {

        val preview = Preview.Builder().setTargetAspectRatio(aspectRatio).build()

        val useCases: List<UseCase> = listOfNotNull(
            preview, imageCapture, *analyzers
        )

        //bindToLifecycle
        return APromise.from(ProcessCameraProvider.getInstance(previewView.context))
            .thenMap {
                it.bindToLifecycle(lifecycleOwner, cameraSelector, *useCases.toTypedArray())
            }.then { camera ->
                //https://developer.android.com/jetpack/androidx/releases/camera#camera2-core-1.0.0-alpha07
                previewView.preferredImplementationMode = PreviewView.ImplementationMode.TEXTURE_VIEW
//                preview.setSurfaceProvider(previewView.createSurfaceProvider(camera.cameraInfo))
                preview.setSurfaceProvider(previewView.createSurfaceProvider(null))
            }
    }

    fun startCamera(cameraView: CameraView
            , cameraSelector: CameraSelector = defaultCameraSelector()
            , imageCapture: ImageCapture? = defaultImageCapture(cameraSelector)
            , @AspectRatio.Ratio aspectRatio: Int = AspectRatio.RATIO_16_9
            , lifecycleOwner: LifecycleOwner = Utils.getActivity(cameraView)!! as LifecycleOwner
            , vararg analyzers: ImageAnalysis
        ): APromise<Camera> {

        val preview = Preview.Builder().setTargetAspectRatio(aspectRatio).build()

        val useCases: List<UseCase> = listOfNotNull(
            preview, imageCapture, *analyzers
        )

        //bindToLifecycle
        return APromise.from(ProcessCameraProvider.getInstance(cameraView.context))
            .thenMap {
                it.bindToLifecycle(lifecycleOwner, cameraSelector, *useCases.toTypedArray())
            }
    }




    fun takePicture(cameraView: CameraView): APromise<ImageProxy> {
        return APromise.ofCallback { promiseCallback ->
            cameraView.takePicture(
                Executors.newSingleThreadExecutor()
                , object : ImageCapture.OnImageCapturedCallback() {

                    override fun onCaptureSuccess(image: ImageProxy) {
                        promiseCallback.onSuccess(image)
                        super.onCaptureSuccess(image) //closes
                    }

                    override fun onError(e: ImageCaptureException) {
                        promiseCallback.onFailure(e)
                    }
                }
            )
        }
    }







    fun defaultCameraSelector(@CameraSelector.LensFacing cameraType: Int = CameraSelector.LENS_FACING_BACK)
    : CameraSelector {
        return CameraSelector.Builder().requireLensFacing(cameraType).build()
    }

    fun defaultImageCapture(cameraSelector: CameraSelector): ImageCapture {
        return ImageCapture.Builder().also { builder ->
            BokehImageCaptureExtender.create(builder).also {
                if (it.isExtensionAvailable(cameraSelector)) {
                    it.enableExtension(cameraSelector)
                }
            }
        }.build()
    }
}