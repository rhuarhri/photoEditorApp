package com.example.editorapp

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.example.editorapp.imageHandling.RetrieveImageHandler
import java.io.File

/*
A view model holds the business logic only
this ensures that an activity only deals with the UI
 */

class MainActVM : ViewModel() {

    public val images = arrayOf((R.drawable.aim_filter).toString(), (R.drawable.film_filter).toString(),
        (R.drawable.fire_filter).toString(), (R.drawable.jail_filter).toString(),
        (R.drawable.money_filter).toString(), (R.drawable.photo_filter).toString(),
        (R.drawable.rose_filter).toString(), (R.drawable.stage_filter).toString()
    )

    public var filterLocation : String = images[0]

    public fun getChosenFilter (appContext : Context) : Bitmap
    {
        val imageGetter: RetrieveImageHandler = RetrieveImageHandler(appContext)
        return imageGetter.getBitmapFromResources(filterLocation.toInt())
    }

    public fun setChosenFilter (appContext : Context, position : Int, height : Int, width : Int) : Bitmap
    {
        val imageGetter: RetrieveImageHandler = RetrieveImageHandler(appContext)

        filterLocation = images[position]

        return imageGetter.formatBitmapFromResources(images[position].toInt(), height, width)
    }

    public fun sendCapturedImage(appContext: Context, file : File) : Intent
    {
        val goto : Intent = Intent(appContext, ImagePreviewActivity::class.java)
        goto.putExtra("photo", file.absolutePath)

        val image = RetrieveImageHandler(appContext).getBitmapFromFile(file.canonicalPath)
        goto.putExtra("height", image.height)
        goto.putExtra("width", image.width)

        goto.putExtra("layer", filterLocation)

        return goto
    }
}