package com.example.editorapp.imageHandling

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import com.example.editorapp.R
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.net.HttpURLConnection

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
        /*
        if (filePath == "" || filePath == null)
        {
            return setDefault(100, 100)
        }else{
            val imageFile = File(filePath)

            val foundImage : Bitmap? = BitmapFactory.decodeStream(FileInputStream(imageFile) as InputStream?, null, null)

            if (foundImage == null)
            {
                return setDefault(100, 100)
            }
            else
            {
                return foundImage
            }
        }*/

        return fromFile(filePath, null, null)

    }

    fun formatBitmapFromFile(filePath: String?, height: Int, width: Int) : Bitmap
    {
        /*
        if (filePath == "" || filePath == null)
        {
            return setDefault(height, width)
        }else{
            val imageFile = File(filePath)

            val foundImage : Bitmap? = BitmapFactory.decodeStream(FileInputStream(imageFile) as InputStream?, null, imageOptions(height, width))

            if (foundImage == null)
            {
                return setDefault(height, width)
            }
            else
            {
                return foundImage
            }
        }*/

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

    fun getBitmapFromResoucres(location : Int) : Bitmap
    {
        /*
        val foundImage : Bitmap? = BitmapFactory.decodeResource(context.resources, location, null)
        if (foundImage == null)
        {
            return setDefault(100, 100)
        }
        else
        {
            return foundImage
        }*/

        return fromResource(location, null, null)
    }

    fun formatBitmapFromResources(location : Int, height : Int, width : Int) : Bitmap
    {
        /*
        val foundImage : Bitmap? = BitmapFactory.decodeResource(context.resources, location, imageOptions(height, width))
        if (foundImage == null)
        {
            return setDefault(height, width)
        }
        else
        {
            return foundImage
        }*/

        return fromResource(location, height, width)
    }


    /*
    fun getBitmapFromURL(imageURL : String?, height : Int, width : Int) : Bitmap?
    {
        return if (imageURL == "" || imageURL == null) {
            setDefault(height, width)
        } else {

            val url = java.net.URL(imageURL)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.connect()

            val inStream: InputStream = connection.inputStream

            BitmapFactory.decodeStream(inStream, null, imageOptions(height, width))
        }
    }*/


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

        /*
        GlideApp.with(context)
            .load(imageLocation)
            .override(desiredIV.height,desiredIV.width)
            .placeholder(R.drawable.no_image_icon)
            .error(R.drawable.no_image_icon)
            .into(desiredIV)*/
    }
}