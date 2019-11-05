package com.example.editorapp.imageEditing

import android.content.Context
import android.graphics.*
import android.renderscript.*
import com.example.editorapp.R


class ImageFilters (private val appContext : Context) {

    private lateinit var changingImage : Bitmap

    private lateinit var newImage : Bitmap

    public fun applyFilter(image : Bitmap, appContext: Context) : Bitmap
    {
        //changingImage = image.copy(Bitmap.Config.ARGB_8888, true)

        //newImage = Bitmap.createBitmap(image.height, image.width, Bitmap.Config.ARGB_8888)

        //var canvas : Canvas = Canvas(newImage)

        //canvas.drawBitmap(changingImage, 0.0f, 0.0f, oldStyle())

        //cropCircle(canvas)

        newImage = blur(image, appContext)

        return newImage

    }


    private fun grayScale() : Paint
    {

        var grayCM : ColorMatrix = ColorMatrix()
        grayCM.setSaturation(0f)

        var grayFilter : ColorFilter = ColorMatrixColorFilter(grayCM)

        var paint : Paint = Paint()
        paint.colorFilter = grayFilter

        return paint

    }

    private fun sepia() : Paint
    {

        var grayCM : ColorMatrix = ColorMatrix()
        grayCM.setScale(1f, 1f, 0.8f, 1f)

        var grayFilter : ColorFilter = ColorMatrixColorFilter(grayCM)

        var paint : Paint = Paint()
        paint.colorFilter = grayFilter

        return paint

    }

    private fun binary() : Paint
    {

        val m = 255f
        val t = -255 * 128f

        val baseValue : Float = 0f

        var grayCM : ColorMatrix = ColorMatrix(
            floatArrayOf(
                m, baseValue, baseValue, 1f, t,
                baseValue, m, baseValue, 1f, t,
                baseValue, baseValue, m, 1f, t,
                baseValue, baseValue, baseValue, 1f, baseValue
            )
        )

        var satCM : ColorMatrix = ColorMatrix()
        satCM.setSaturation(0f)
        satCM.postConcat(grayCM)

        var grayFilter : ColorFilter = ColorMatrixColorFilter(satCM)

        var paint : Paint = Paint()
        paint.colorFilter = grayFilter

        return paint
    }

    private fun invert() : Paint
    {
        var blueCM : ColorMatrix = ColorMatrix(floatArrayOf(
      -1f,  0f,  0f,  0f, 255f,
       0f, -1f,  0f,  0f, 255f,
       0f,  0f, -1f,  0f, 255f,
       0f,  0f,  0f,  1f,   0f))

        var grayFilter : ColorFilter = ColorMatrixColorFilter(blueCM)

        var paint : Paint = Paint()
        paint.colorFilter = grayFilter

        return paint
    }

    private fun alphaBlue() : Paint
    {
        var blueCM : ColorMatrix = ColorMatrix(floatArrayOf(
            0f,    0f,    0f, 0f,   0f,
            0.3f,    0f,    0f, 0f,  50f,
            0f,    0f,    0f, 0f, 255f,
            0.2f, 0.4f, 0.4f, 0f, -30f))

        var grayFilter : ColorFilter = ColorMatrixColorFilter(blueCM)

        var paint : Paint = Paint()
        paint.colorFilter = grayFilter

        return paint
    }

    private fun alphaPink() : Paint
    {
        var blueCM : ColorMatrix = ColorMatrix(floatArrayOf(
            0f,    0f,    0f, 0f, 255f,
            0f,    0f,    0f, 0f,   0f,
            0.2f,    0f,    0f, 0f,  50f,
            0.2f, 0.2f, 0.2f, 0f, -20f))

        var grayFilter : ColorFilter = ColorMatrixColorFilter(blueCM)

        var paint : Paint = Paint()
        paint.colorFilter = grayFilter

        return paint
    }

    private fun alphaYello() : Paint
    {
        var blueCM : ColorMatrix = ColorMatrix(floatArrayOf(
            0f,    0f,    0f, 0f, 255f,
            0f,    0f,    0f, 0f,   153f,
            0.2f,    0f,    0f, 0f,  51f,
            0.2f, 0.2f, 0.2f, 0f, -20f))

        var grayFilter : ColorFilter = ColorMatrixColorFilter(blueCM)

        var paint : Paint = Paint()
        paint.colorFilter = grayFilter

        return paint
    }

    private fun blackAndWhite() : Paint
    {
        var blueCM : ColorMatrix = ColorMatrix(floatArrayOf(
            1.5f,    1.5f,    1.5f, 0f, 0f,
            1.5f,    1.5f,    1.5f, 0f, 0f,
            1.5f,    1.5f,    1.5f, 0f, 0f,
            0f,    0f,    0f, 1f,  0f,
            -1f,    -1f,    -1f, 0f, 1f))

        var grayFilter : ColorFilter = ColorMatrixColorFilter(blueCM)

        var paint : Paint = Paint()
        paint.colorFilter = grayFilter

        return paint
    }

    private fun oldStyle() : Paint
    {
        var blueCM : ColorMatrix = ColorMatrix(floatArrayOf(
            0.5f,    0.5f,    0.5f, 0f, 0f,
            0.5f,    0.5f,    0.5f, 0f, 0f,
            0.5f,    0.5f,    0.5f, 0f, 0f,
            0f,    0f,    0f, 1f,  0f,
            0.2f,    0.2f,    0.2f, 0f, 1f))

        var grayFilter : ColorFilter = ColorMatrixColorFilter(blueCM)

        var paint : Paint = Paint()
        paint.colorFilter = grayFilter

        return paint
    }

    private fun colourChange() : Paint
    {
        var blueCM : ColorMatrix = ColorMatrix(floatArrayOf(
            0f,    0f,    0f, 0f, 0f,
            0f,    1f,    0f, 0f,   0f,
            0f,    0f,    1f, 0f,  0f,
            0f,    0f,    0f, 1f,  0f,
            0f,    0f,    0f, 0f, 1f))

        var grayFilter : ColorFilter = ColorMatrixColorFilter(blueCM)

        var paint : Paint = Paint()
        paint.colorFilter = grayFilter

        return paint
    }

    private fun lightRed() : Paint
    {
        var paint : Paint = Paint()
        paint.setColorFilter(LightingColorFilter(Color.RED, 0))
        return paint
    }


    private fun cropCircle(canvas : Canvas)
    {

        var circle : Bitmap = BitmapFactory.decodeResource(appContext.resources, R.drawable.circle_ic)

        var maskPaint : Paint = Paint()

        maskPaint.setXfermode(
            PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        )

        canvas.drawBitmap(circle, 0f, 0f, maskPaint)



    }

    private fun boxBlur(orginal : Bitmap, context : Context) : Bitmap
    {

        var newImage : Bitmap = Bitmap.createBitmap(orginal.width, orginal.height, Bitmap.Config.ARGB_8888)

        var renderScript : RenderScript = RenderScript.create(context)

        var imageIn : Allocation = Allocation.createFromBitmap(renderScript, orginal)
        var imageOut : Allocation = Allocation.createFromBitmap(renderScript,newImage)

        var blur : ScriptIntrinsicConvolve3x3 = ScriptIntrinsicConvolve3x3.create(renderScript, Element.U8_4(renderScript))
        blur.setInput(imageIn)
        blur.setCoefficients(
            floatArrayOf(
                -1f, -1f, -1f,
                -1f, 8f, -1f,
                -1f, -1f, -1f
            )
            /*
            floatArrayOf(
                0f, -1f, 0f,
                -1f, 5f, -1f,
                0f, -1f, 0f
            )*/
            /*floatArrayOf(
            1f, 1f, 1f,
            1f, 1f, 1f,
            1f, 1f, 1f
        )*/)
        blur.forEach(imageOut)

        imageOut.copyTo(newImage)
        renderScript.destroy()
        return newImage
    }


    private fun blur(orginal: Bitmap, context: Context) : Bitmap
    {
        var newImage : Bitmap = Bitmap.createBitmap(orginal.width, orginal.height, Bitmap.Config.ARGB_8888)

        var renderScript : RenderScript = RenderScript.create(context)

        var imageIn : Allocation = Allocation.createFromBitmap(renderScript, orginal)
        var imageOut : Allocation = Allocation.createFromBitmap(renderScript,newImage)

        var blur : ScriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))
        blur.setInput(imageIn)
        blur.setRadius(25f) //between 0 and 25
        blur.forEach(imageOut)

        imageOut.copyTo(newImage)
        renderScript.destroy()
        return newImage
    }

}