package com.example.editorapp.imageEditing

import android.graphics.*
import android.util.Log
import android.widget.ImageView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

class ImageFilter(private val imagePreview : ImageView, private var originalImage : Bitmap) {

    private var changeApplied : Boolean = false

    public fun filterEvent(function : String, overlay: Bitmap?, filter : String)
    {

            var changedImage : Bitmap? = null
            when(function){
                "filter" ->
                {
                    originalImage = addImageFilter(originalImage, filter)
                    changedImage = originalImage
                }
                "applyFilter"->
                {
                    changeApplied = true
                    originalImage = addImageFilter(originalImage, filter)
                    changedImage = originalImage
                }

                "overlay" ->
                {
                    if (overlay != null) {
                        originalImage = addImageOverlay(originalImage, overlay)
                        changedImage = originalImage
                    }
                }
                "applyOverlay" ->
                {
                    if (overlay != null) {
                        changeApplied = true
                        originalImage = addImageOverlay(originalImage, overlay)
                        changedImage = originalImage
                    }
                }
            }

            imagePreview.setImageBitmap(changedImage)

    }

    public fun hasChanged() : Boolean
    {
        return changeApplied
    }

    private fun addImageOverlay(currentImage: Bitmap, overlay: Bitmap): Bitmap {
        val newImage: Bitmap =
            Bitmap.createBitmap(currentImage.width, currentImage.height, Bitmap.Config.ARGB_8888)

        val canvas: Canvas = Canvas(newImage)
        canvas.drawBitmap(currentImage, 0f, 0f, null)
        val scaledImage : Bitmap = Bitmap.createScaledBitmap(overlay, currentImage.width, currentImage.height, false)
        canvas.drawBitmap(scaledImage, 0f, 0f, null)

        return newImage
    }

    private fun addImageFilter(currentImage: Bitmap, filter : String) : Bitmap
    {
        val newImage: Bitmap =
            Bitmap.createBitmap(currentImage.width, currentImage.height, Bitmap.Config.ARGB_8888)

        val canvas: Canvas = Canvas(newImage)

        val paint : Paint = Paint()

        when(filter)
        {
            "alpha blue" ->
            {
                val alphaBlueCM : ColorMatrix = ColorMatrix(floatArrayOf(
                    0f,    0f,    0f, 0f,   0f,
                    0.3f,    0f,    0f, 0f,  50f,
                    0f,    0f,    0f, 0f, 255f,
                    0.2f, 0.4f, 0.4f, 0f, -30f))

                val alphaBlueFilter : ColorFilter = ColorMatrixColorFilter(alphaBlueCM)

                paint.colorFilter = alphaBlueFilter
            }
            "binary"->
            {
                val m = 255f
                val t = -255 * 128f

                val baseValue : Float = 0f

                val binaryCM : ColorMatrix = ColorMatrix(
                    floatArrayOf(
                        m, baseValue, baseValue, 1f, t,
                        baseValue, m, baseValue, 1f, t,
                        baseValue, baseValue, m, 1f, t,
                        baseValue, baseValue, baseValue, 1f, baseValue
                    )
                )

                val satCM : ColorMatrix = ColorMatrix()
                satCM.setSaturation(0f)
                satCM.postConcat(binaryCM)

                val binaryFilter : ColorFilter = ColorMatrixColorFilter(satCM)

                paint.colorFilter = binaryFilter
            }
            "gray scale"->
            {
                val grayCM : ColorMatrix = ColorMatrix()
                grayCM.setSaturation(0f)

                val grayFilter : ColorFilter = ColorMatrixColorFilter(grayCM)

                paint.colorFilter = grayFilter
            }
            "invert"->
            {
                val invertCM : ColorMatrix = ColorMatrix(floatArrayOf(
                    -1f,  0f,  0f,  0f, 255f,
                    0f, -1f,  0f,  0f, 255f,
                    0f,  0f, -1f,  0f, 255f,
                    0f,  0f,  0f,  1f,   0f))

                val invertFilter : ColorFilter = ColorMatrixColorFilter(invertCM)

                paint.colorFilter = invertFilter
            }
            "old style"->
            {
                val oldStyleCM : ColorMatrix = ColorMatrix(floatArrayOf(
                    0.5f,    0.5f,    0.5f, 0f, 0f,
                    0.5f,    0.5f,    0.5f, 0f, 0f,
                    0.5f,    0.5f,    0.5f, 0f, 0f,
                    0f,    0f,    0f, 1f,  0f,
                    0.2f,    0.2f,    0.2f, 0f, 1f))

                val oldStyleFilter : ColorFilter = ColorMatrixColorFilter(oldStyleCM)

                paint.colorFilter = oldStyleFilter
            }
            "sepia"->
            {
                val sepiaCM : ColorMatrix = ColorMatrix()
                sepiaCM.setScale(1f, 1f, 0.8f, 1f)

                val sepiaFilter : ColorFilter = ColorMatrixColorFilter(sepiaCM)

                paint.colorFilter = sepiaFilter
            }
        }

        canvas.drawBitmap(currentImage, 0f, 0f, paint)

        return newImage
    }

}