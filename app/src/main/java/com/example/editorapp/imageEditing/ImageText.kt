package com.example.editorapp.imageEditing

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint

//class ImageText {

    fun addText(image : Bitmap, message : String, posX : Int, posY : Int, style : Paint) : Bitmap
    {
        val newImage : Bitmap = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(newImage)
        canvas.drawBitmap(image, 0.0f, 0.0f, null)
        canvas.drawText(message, posX.toFloat(), posY.toFloat(), style)

        return newImage
    }
//}