package com.example.editorapp.imageEditing

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur

//class ImageBlur(context : Context) {

    //private val appContext : Context = context

    fun blurImage(appContext : Context, image : Bitmap, blurAmount : Int) : Bitmap
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
//}