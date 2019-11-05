package com.example.editorapp.cameraCode

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ImageFormat.JPEG
import android.graphics.Matrix
import android.media.Image
import android.provider.MediaStore
import android.util.Rational
import android.util.Size
import android.view.Surface
import android.view.TextureView
import android.view.ViewGroup
import androidx.camera.core.*
import androidx.lifecycle.LifecycleOwner


class CameraHandler(Owner : LifecycleOwner, CameraView : TextureView) {

    private val owner : LifecycleOwner = Owner
    private val cameraView : TextureView = CameraView
    private val height : Int = CameraView.height
    private val width : Int = CameraView.width

    var imageCapture : ImageCapture

    private var chosenCamera = CameraX.LensFacing.BACK

    init{
        CameraX.unbindAll()

        val preview = createPreview()

        updatePreview(preview)


        imageCapture = createCaptureImage()


        CameraX.bindToLifecycle(owner, preview, imageCapture)
    }

    /*
    fun swapCamera()
    {

        CameraX.unbindAll()

        val preview : Preview


        if (chosenCamera == CameraX.LensFacing.BACK)
        {
            chosenCamera = CameraX.LensFacing.FRONT


        }
        else
        {

            chosenCamera = CameraX.LensFacing.BACK



        }


            preview = createPreview()

            updatePreview(preview)

            CameraX.bindToLifecycle(owner, preview, imageCapture)



    }*/


    private fun createPreview() : Preview
    {
        val aspectRatio = Rational(width, height)

        val size = Size(width, height)

        val previewConfig = PreviewConfig.Builder().apply{
            setLensFacing(chosenCamera)
            setTargetAspectRatio(aspectRatio)
            setTargetResolution(size)
        }.build()

        return Preview(previewConfig)
    }

    private fun createImageAnalyser()
    {
        val imageConfig = ImageAnalysisConfig.Builder().setTargetResolution(Size(1280, 720))
            .build()

        val imageAnalysis = ImageAnalysis(imageConfig)
        imageAnalysis.setAnalyzer { image: ImageProxy, rotation: Int ->


        }

    }

    private fun updatePreview(newPreview : Preview)
    {
        newPreview.setOnPreviewOutputUpdateListener{
                previewOutput : Preview.PreviewOutput? ->


            val parent = cameraView.parent as ViewGroup
            parent.removeView(cameraView)
            parent.addView(cameraView, 0)

            cameraView.surfaceTexture = previewOutput!!.surfaceTexture

            updateTransform()

        }
    }

    private fun createCaptureImage() : ImageCapture
    {
        val imageCaptureConfig = ImageCaptureConfig.Builder().build()
        return ImageCapture(imageCaptureConfig)
    }



    private fun updateTransform()
    {
        val mx = Matrix()
        val w = width
        val h = height

        val cX = width / 2f
        val cY = height / 2f

        var rotationDgr : Int = 0
        val rotation : Int = cameraView.rotation.toInt()

        when (rotation)
        {

            Surface.ROTATION_0 -> rotationDgr = 0
            Surface.ROTATION_90 -> rotationDgr = 90
            Surface.ROTATION_180 -> rotationDgr = 180
            Surface.ROTATION_270 -> rotationDgr = 270
        }

        mx.postRotate(rotationDgr.toFloat(), cX, cY)
        cameraView.setTransform(mx)

    }

}