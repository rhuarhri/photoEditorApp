package com.example.editorapp.imageEditing

import android.graphics.*

class ImageSelection {

    private var isSelecting = false

    var startX : Int = 0
    var startY : Int = 0
    var X : Int = 0
    var Y : Int = 0
    var height : Int = 0
    var width : Int = 0

    val shapeColour : String = "#7000ffff"

    fun setPosition(posX : Int, posY : Int)
    {
        isSelecting = true
        startX = posX
        startY = posY
        X = posX
        Y = posY
    }

    fun setHeightAndWidth(posX : Int, posY: Int)
    {
            if (posX > X && posY > Y) {
                height = posX - X
                width = posY - Y
            }

    }

    fun getRect() : Rect
    {
        return Rect(X, Y, height, width)
    }

    fun stopSelecting()
    {
        isSelecting = false
    }

    fun selectionInProgress() : Boolean
    {
        return isSelecting
    }

    fun displayImage(currentImage : Bitmap) : Bitmap
    {
        val rectImage = Bitmap.createBitmap(currentImage.width, currentImage.height, Bitmap.Config.ARGB_8888)

        val rectCanvas = Canvas(rectImage)

        val paintShape = Paint()
        paintShape.color = Color.parseColor(shapeColour)

        val square : Rect = getRect()

        rectCanvas.drawRect(square, paintShape)

        val newImage : Bitmap = Bitmap.createBitmap(currentImage.width, currentImage.height, Bitmap.Config.ARGB_8888)

        val cropCanvas = Canvas(newImage)

        cropCanvas.drawBitmap(currentImage, 0f,0f, null)

        cropCanvas.drawBitmap(rectImage, 0f,0f, null)

        return newImage
    }
}