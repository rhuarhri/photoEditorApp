package com.example.editorapp.imageEditing

import android.graphics.*
import android.view.MotionEvent

class ImageDraw() {

    private lateinit var lastDrawing : Bitmap
    private var currentImage : Bitmap? = null

    private val paint : Paint = Paint().apply {
        isAntiAlias = true
        isDither = true
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }

    public var currentSize : Float = 0f
    public var currentColour : Int = 0

    private val path : Path = Path()

    init {
        paint.strokeWidth = 14f
        currentSize = 14f
        currentColour = Color.parseColor("#000000")
        paint.color = currentColour
    }


    public fun touchEventHandler(event : MotionEvent, positionX : Int, positionY : Int, image : Bitmap)
    {

            when(event.action) {
                MotionEvent.ACTION_DOWN -> onStartDraw(positionX.toFloat(), positionY.toFloat())
                MotionEvent.ACTION_MOVE -> {
                    imageToDrawOn(image)
                    whileDrawing(positionX.toFloat(), positionY.toFloat())
                }
                MotionEvent.ACTION_UP -> onStopDraw()
            }

    }

    private fun imageToDrawOn(image : Bitmap)
    {
        if (!::lastDrawing.isInitialized) {
            lastDrawing = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)
        }
        currentImage = image
    }

    public fun getDrawOnImage() : Bitmap?
    {
        if (currentImage != null) {
            val newImage = Bitmap.createBitmap(currentImage!!.width, currentImage!!.height, Bitmap.Config.ARGB_8888)
            val canvas: Canvas = Canvas(newImage)
            canvas.drawBitmap(currentImage!!, 0f, 0f, null)
            canvas.drawBitmap(lastDrawing, 0f, 0f, null)
            currentImage = newImage
        }
        return currentImage
    }

    public fun changeSettings(size : Float, colour : Int)
    {
        paint.strokeWidth = size
        currentSize = size
        paint.color = colour
        currentColour = colour
    }

    private var currentX : Float = 0f
    private var currentY : Float = 0f

    private fun onStartDraw(newX : Float, newY : Float)
    {
        path.reset()
        path.moveTo(newX, newY)
        currentX = newX
        currentY = newY
    }

    private fun onStopDraw()
    {
        path.reset()
    }

    private fun whileDrawing(newX: Float, newY: Float)
    {
        if (!withinErrorRange(newX, newY))
        {
            path.quadTo(currentX, currentY, (newX + currentX) / 2, (newY+ currentY) / 2)
            currentX = newX
            currentY = newY

            val newImage : Bitmap = Bitmap.createBitmap(lastDrawing.width, lastDrawing.height, Bitmap.Config.ARGB_8888)
            val canvas : Canvas = Canvas(newImage)
            canvas.drawPath(path, paint)

            canvas.drawBitmap(lastDrawing, 0f, 0f, null)

            lastDrawing = newImage

        }

    }


    private fun withinErrorRange(newX : Float, newY: Float) : Boolean
    {
        val errorX : Float = Math.abs(newX - currentX)
        val errorY : Float = Math.abs(newY - currentY)

        val errorAmount : Float = 5f

        return errorX <= errorAmount || errorY <= errorAmount
    }


}