package com.example.editorapp.imageEditing

import android.graphics.*
import android.util.Log
import android.widget.ImageView
import com.example.editorapp.fragmentCode.editFragments.cropFRG
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class ImageCrop (private val imagePreview : ImageView, private var originalImage : Bitmap) {


    public fun cropEventProcessor(function : String, colour : String, selector: ImageSelection)
    {
        doAsync {
            var changedImage : Bitmap? = null
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

                }
                else ->
                {
                    Log.e("crop Event Processor", "$function is not a real function")
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


    fun cropSquare(colour : String, currentImage : Bitmap, selector : ImageSelection) : Bitmap
    {
        val rectImage = Bitmap.createBitmap(currentImage.width, currentImage.height, Bitmap.Config.ARGB_8888)

        val rectCanvas : Canvas = Canvas(rectImage)

        val paintShape : Paint = Paint()
        paintShape.color = Color.parseColor(colour)

        val square : Rect = selector.getRect()

        rectCanvas.drawRect(square, paintShape)

        val newImage : Bitmap = Bitmap.createBitmap(currentImage.width, currentImage.height, Bitmap.Config.ARGB_8888)

        val cropCanvas : Canvas = Canvas(newImage)

        cropCanvas.drawBitmap(currentImage, 0f,0f, null)

        val paintMask : Paint = Paint()
        paintMask.setXfermode(
            PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        )

        cropCanvas.drawBitmap(rectImage, 0f,0f, paintMask)

        val resizedImage : Bitmap = Bitmap.createBitmap(selector.width, selector.height, Bitmap.Config.ARGB_8888)

        val resizeCanvas : Canvas = Canvas(resizedImage)

        resizeCanvas.drawBitmap(newImage, (0 - selector.X.toFloat()), (0-selector.Y.toFloat()), null)

        return resizedImage
    }
}