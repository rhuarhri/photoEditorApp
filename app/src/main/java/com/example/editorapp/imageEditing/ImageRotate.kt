package com.example.editorapp.imageEditing

import android.graphics.Bitmap
import android.graphics.Canvas

//class ImageRotate {

    fun RotateImage(image : Bitmap, rotation: Float) : Bitmap
    {
        val newImage : Bitmap = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)

        val canvas : Canvas = Canvas(newImage)
        canvas.save()
        canvas.rotate(rotation, (image.width /2).toFloat(), (image.height / 2).toFloat())
        canvas.drawBitmap(image, 0.0f, 0.0f, null)
        canvas.restore()

        return newImage
    }
//}