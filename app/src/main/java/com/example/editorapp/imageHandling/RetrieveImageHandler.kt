package com.example.editorapp.imageHandling

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import com.example.editorapp.R
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

/*
This could be easily done with glide an image processing library but it
was done like this to show that I know how it can be done
 */

class RetrieveImageHandler(private val context : Context) {

    private val glideApp : AppsGlideModule = AppsGlideModule()

    private fun imageOptions(height : Int, width : Int) : BitmapFactory.Options
    {

        val options = BitmapFactory.Options().apply {

            inJustDecodeBounds = true


            val photoW: Int = outWidth
            val photoH: Int = outHeight

            // Determine how much to scale down the image
            val scaleFactor: Int = Math.min(photoW / width, photoH / height)

            // Decode the image file into a Bitmap sized to fill the View
            inJustDecodeBounds = false
            inSampleSize = scaleFactor

        }

        return options
    }

    private fun fromFile(filePath : String?, height : Int?, width : Int?) : Bitmap
    {
        var default : Bitmap? = null
        var options : BitmapFactory.Options? = null

        if (height != null || width != null)
        {
            default = setDefault(height!!, width!!)
            options = imageOptions(height, width)
        }
        else
        {
            default = setDefault(100, 100)
        }

        if (filePath == "" || filePath == null)
        {
            return default
        }
        else
        {
            val imageFile = File(filePath)

            val foundImage : Bitmap? = BitmapFactory.decodeStream(FileInputStream(imageFile) as InputStream?, null, options)

            if (foundImage == null)
            {
                return default
            }
            else
            {
                return foundImage
            }
        }
    }

    fun getBitmapFromFile(filePath : String?) : Bitmap
    {

        return fromFile(filePath, null, null)

    }

    fun formatBitmapFromFile(filePath: String?, height: Int, width: Int) : Bitmap
    {
        return fromFile(filePath, height, width)
    }

    private fun fromResource(location : Int, height: Int?, width: Int?) : Bitmap
    {
        var default : Bitmap? = null
        var options : BitmapFactory.Options? = null

        if (height != null || width != null)
        {
            default = setDefault(height!!, width!!)
            options = imageOptions(height, width)
        }
        else
        {
            default = setDefault(100, 100)
        }

        val foundImage : Bitmap? = BitmapFactory.decodeResource(context.resources, location, options)
        if (foundImage == null)
        {
            return default
        }
        else
        {
            return foundImage
        }
    }

    fun getBitmapFromResources(location : Int) : Bitmap
    {

        return fromResource(location, null, null)
    }

    fun formatBitmapFromResources(location : Int, height : Int, width : Int) : Bitmap
    {

        return fromResource(location, height, width)
    }


    //returns default image if file or input stream is empty
    fun setDefault(height : Int, width : Int) : Bitmap
    {
        return BitmapFactory.decodeResource(context.resources, R.drawable.no_image_icon, imageOptions(height, width))
    }

    /*
    use of glide
    originally I was going to use the above code to display images on a recycler view but I kept having problems
    which I think result from recycler view already displaying an item before the image could be returned causing the
    app to display only a blank image view. I solved this problem by using glide to display images a recycler view.
    All images displayed in a recycler view are handled by the below code the rest are handled by the above code.
     */

    fun recyclerViewImageHandler(desiredIV : ImageView, imageLocation : String, isFile : Boolean)
    {
        val image =
        if (isFile) {
            imageLocation
        }
        else
        {
            imageLocation.toInt()
        }

        GlideApp.with(context)
            .load(image)
            .override(desiredIV.height, desiredIV.width)
            .placeholder(R.drawable.no_image_icon)
            .error(R.drawable.no_image_icon)
            .into(desiredIV)
    }
}