package com.example.editorapp.imageEditing

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.Log
import android.widget.ImageView

class ImageBlur(private val appContext : Context, private val imagePreview : ImageView, private var originalImage : Bitmap) {

    private var changedImage : Bitmap? = null

    fun blurEventProcessor(function : String, blurAmount: Int)
    {


            when (function)
            {
                "apply" ->
                {
                    changeApplied = true
                    originalImage = blurImage(originalImage, blurAmount)
                    changedImage = originalImage

                }
                "preview" ->
                {
                    changeApplied = false
                    changedImage = blurImage(originalImage, blurAmount)
                }
                else ->
                {
                    Log.e("colour change Event Processor", "$function is not a real function")
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

    private fun blurImage(image : Bitmap, blurAmount : Int) : Bitmap
    {
        val newImage : Bitmap = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)

        val renderScript : RenderScript = RenderScript.create(appContext)

        val imageIn : Allocation = Allocation.createFromBitmap(renderScript, image)
        val imageOut : Allocation = Allocation.createFromBitmap(renderScript,newImage)

        val blur : ScriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))
        blur.setInput(imageIn)
        Log.d("blur image", "blur amount is $blurAmount")
        blur.setRadius(blurAmount.toFloat()) //between 0 and 25
        blur.forEach(imageOut)

        imageOut.copyTo(newImage)
        renderScript.destroy()
        return newImage
    }
}