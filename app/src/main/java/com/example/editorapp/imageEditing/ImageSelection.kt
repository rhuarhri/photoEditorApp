package com.example.editorapp.imageEditing

import android.graphics.*

class ImageSelection {

    private var isSelecting = false

    public var X : Int = 0
    public var Y : Int = 0
    public var height : Int = 0
    public var width : Int = 0

    public val shapeColour : String = "#7000ffff"

    public fun setPosition(posX : Int, posY : Int)
    {
        isSelecting = true
        X = posX
        Y = posY
    }

    public fun setHeightAndWidth(posX : Int, posY: Int)
    {
            if (posX > X && posY > Y) {
                height = posX - X
                width = posY - Y
            }

    }

    public fun getRect() : Rect
    {
        return Rect(X, Y, height, width)
    }

    public fun stopSelecting()
    {
        isSelecting = false
    }

    public fun selectionInProgress() : Boolean
    {
        return isSelecting
    }

    public fun displayImage(currentImage : Bitmap) : Bitmap
    {
        val rectImage = Bitmap.createBitmap(currentImage.width, currentImage.height, Bitmap.Config.ARGB_8888)

        val rectCanvas : Canvas = Canvas(rectImage)

        val paintShape : Paint = Paint()
        paintShape.color = Color.parseColor(shapeColour)

        val square : Rect = getRect()

        rectCanvas.drawRect(square, paintShape)

        val newImage : Bitmap = Bitmap.createBitmap(currentImage.width, currentImage.height, Bitmap.Config.ARGB_8888)

        val cropCanvas : Canvas = Canvas(newImage)

        cropCanvas.drawBitmap(currentImage, 0f,0f, null)

        cropCanvas.drawBitmap(rectImage, 0f,0f, null)

        return newImage
    }
}