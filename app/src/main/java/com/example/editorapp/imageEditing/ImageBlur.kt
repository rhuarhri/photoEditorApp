package com.example.editorapp.imageEditing

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.Log
import android.widget.ImageView
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.uiThread

class ImageBlur(private val appContext : Context, private val imagePreview : ImageView, private var originalImage : Bitmap) {

    public fun blurEventProcessor(function : String, blurAmount: Int)
    {
        async {
            var changedImage : Bitmap? = null
            when (function)
            {
                "apply" ->
                {
                    originalImage = blurImage(originalImage, blurAmount)
                    changedImage = originalImage
                    changeApplied = true

                }
                "preview" ->
                {
                    changedImage = blurImage(originalImage, blurAmount)
                    changeApplied = false
                }
                else ->
                {
                    Log.e("colour change Event Processor", "$function is not a real function")
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

    private fun blurImage(image : Bitmap, blurAmount : Int) : Bitmap
    {
        val newImage : Bitmap = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)

        val renderScript : RenderScript = RenderScript.create(appContext)

        val imageIn : Allocation = Allocation.createFromBitmap(renderScript, image)
        val imageOut : Allocation = Allocation.createFromBitmap(renderScript,newImage)

        val blur : ScriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))
        blur.setInput(imageIn)
        blur.setRadius(blurAmount.toFloat()) //between 0 and 25
        blur.forEach(imageOut)

        imageOut.copyTo(newImage)
        renderScript.destroy()
        return newImage
    }
}