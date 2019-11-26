package com.example.editorapp.imageEditing

import android.graphics.Bitmap
import android.graphics.Canvas
import java.util.*

public fun addImageOverlay(currentImage: Bitmap, overlay : Bitmap) : Bitmap
{
    val newImage : Bitmap = Bitmap.createBitmap(currentImage.width, currentImage.height, Bitmap.Config.ARGB_8888)

    val canvas : Canvas = Canvas(newImage)
    canvas.drawBitmap(currentImage, 0f, 0f, null)
    canvas.drawBitmap(overlay, 0f, 0f, null)

    return newImage
}