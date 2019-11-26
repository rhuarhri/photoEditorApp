package com.example.editorapp.imageEditing

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.Log
import android.widget.ImageView
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.uiThread

class ImageColour (private val imagePreview : ImageView, private var originalImage : Bitmap){

    public fun colourChangeEventProcessor(function : String, paint : Paint)
    {
        async {
            var changedImage : Bitmap? = null
            when (function)
            {
                "apply" ->
                {
                    originalImage = addColour(originalImage, paint)
                    changedImage = originalImage
                    changeApplied = true

                }
                "preview" ->
                {
                    changedImage = addColour(originalImage, paint)
                    changeApplied = false
                }
                else ->
                {
                    Log.e("colour change Event Processor", "$function is not a real function")
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

    private fun addColour(image : Bitmap, paintColour : Paint) : Bitmap
    {
        val newImage : Bitmap = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)

        val canvas : Canvas = Canvas(newImage)
        canvas.drawBitmap(image, 0.0f, 0.0f, paintColour)

        return newImage
    }
}