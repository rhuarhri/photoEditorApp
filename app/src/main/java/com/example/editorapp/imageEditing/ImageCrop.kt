package com.example.editorapp.imageEditing

import android.graphics.*
import android.util.Log
import android.widget.ImageView

class ImageCrop (private val imagePreview : ImageView, private var originalImage : Bitmap) {

    private var changedImage : Bitmap? = null

    fun cropEventProcessor(function : String, colour : String, selector: ImageSelection)
    {

            when (function)
            {
                "square" ->
                {
                    originalImage = cropSquare(colour, originalImage, selector)
                    changedImage = originalImage
                    changeApplied = true

                }
                "circle" ->
                {
                    originalImage = cropCircle(colour, originalImage, selector)
                    changedImage = originalImage
                    changeApplied = true
                }
                else ->
                {
                    Log.e("crop Event Processor", "$function is not a real function")
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

    private fun cropSquare(colour : String, currentImage : Bitmap, selector : ImageSelection) : Bitmap
    {
        val rectImage = Bitmap.createBitmap(currentImage.width, currentImage.height, Bitmap.Config.ARGB_8888)

        val rectCanvas = Canvas(rectImage)

        val paintShape = Paint()
        paintShape.color = Color.parseColor(colour)

        val square : Rect = selector.getRect()

        rectCanvas.drawRect(square, paintShape)

        val newImage : Bitmap = Bitmap.createBitmap(currentImage.width, currentImage.height, Bitmap.Config.ARGB_8888)

        val cropCanvas = Canvas(newImage)

        cropCanvas.drawBitmap(currentImage, 0f,0f, null)

        val paintMask = Paint()
        paintMask.setXfermode(
            PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        )

        cropCanvas.drawBitmap(rectImage, 0f,0f, paintMask)

        val resizedImage : Bitmap = Bitmap.createBitmap(selector.width, selector.height, Bitmap.Config.ARGB_8888)

        val resizeCanvas = Canvas(resizedImage)

        resizeCanvas.drawBitmap(newImage, (0 - selector.X.toFloat()), (0-selector.Y.toFloat()), null)

        return resizedImage
    }

    private fun cropCircle(colour : String, currentImage : Bitmap, selector : ImageSelection) : Bitmap
    {
        val circleImage = Bitmap.createBitmap(currentImage.width, currentImage.height, Bitmap.Config.ARGB_8888)

        val circleCanvas = Canvas(circleImage)

        val paintShape = Paint()
        paintShape.color = Color.parseColor(colour)

        val height = selector.height.toFloat()
        val width = selector.width.toFloat()

        val circleRadius : Float = if (height > width) {
            width
        } else {
            height
        }

        val locationX = (0 - selector.X.toFloat()) - (width / 2)
        val locationY = (0-selector.Y.toFloat()) - (height / 2)
        //location x and y will be the center of the circle

        circleCanvas.drawCircle(locationX, locationY, circleRadius, paintShape)

        return circleImage

    }
}