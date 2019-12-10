package com.example.editorapp.imageEditing

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.Log
import android.widget.ImageView

class ImageColour (private val imagePreview : ImageView, private var originalImage : Bitmap){

    private var changedImage : Bitmap? = null

    fun colourChangeEventProcessor(function : String, paint : Paint)
    {

            when (function)
            {
                "apply" ->
                {
                    changeApplied = true
                    originalImage = addColour(originalImage, paint)
                    changedImage = originalImage

                }
                "preview" ->
                {
                    changeApplied = false
                    changedImage = addColour(originalImage, paint)
                }
                else ->
                {
                    Log.e("colour change Event Processor", "$function is not a real function")
                }
            }


        imagePreview.setImageBitmap(changedImage)

    }

    private var changeApplied = false
    fun hasChanged() : Boolean
    {
        return changeApplied
    }

    fun getChangedImage() : Bitmap
    {
        return changedImage!!
    }

    private fun addColour(image : Bitmap, paintColour : Paint) : Bitmap
    {
        val newImage : Bitmap = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(newImage)
        canvas.drawBitmap(image, 0.0f, 0.0f, paintColour)

        return newImage
    }
}