package com.example.editorapp.imageEditing

import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import android.widget.ImageView
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.uiThread

class ImageRotate (private val imagePreview : ImageView, private var originalImage : Bitmap){

    public fun rotateEventProcessor(function : String, rotation: Float)
    {
        async {
            var changedImage : Bitmap? = null
            when (function)
            {
                "apply" ->
                {
                    changeApplied = true
                    originalImage = rotateImage(originalImage, rotation)
                    changedImage = originalImage

                }
                "preview" ->
                {
                    changeApplied = false
                    changedImage = rotateImage(originalImage, rotation)
                }
                else ->
                {
                    Log.e("rotate Event Processor", "$function is not a real function")
                }
            }

            uiThread {
                imagePreview.setImageBitmap(changedImage)
            }

        }

    }

    private var changeApplied = false
    public fun hasChanged() : Boolean
    {
        return changeApplied;
    }
    private fun rotateImage(image : Bitmap, rotation: Float) : Bitmap
    {
        val newImage : Bitmap = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)

        val canvas : Canvas = Canvas(newImage)
        canvas.save()
        canvas.rotate(rotation, (image.width /2).toFloat(), (image.height / 2).toFloat())
        canvas.drawBitmap(image, 0.0f, 0.0f, null)
        canvas.restore()

        return newImage
    }
}