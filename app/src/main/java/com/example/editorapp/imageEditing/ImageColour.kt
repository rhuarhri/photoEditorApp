package com.example.editorapp.imageEditing

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint

//class ImageColour {

    fun addColour(image : Bitmap, paintColour : Paint) : Bitmap
    {
        val newImage : Bitmap = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)

        val canvas : Canvas = Canvas(newImage)
        canvas.drawBitmap(image, 0.0f, 0.0f, paintColour)

        return newImage
    }
//}