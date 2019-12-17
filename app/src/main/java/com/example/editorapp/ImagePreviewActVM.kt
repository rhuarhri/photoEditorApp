package com.example.editorapp

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.example.editorapp.imageHandling.EditHistoryManger
import com.example.editorapp.imageHandling.RetrieveImageHandler
import org.jetbrains.anko.doAsync

class ImagePreviewActVM : ViewModel() {

    lateinit var currentImage : Bitmap
    private var originalHeight : Int = 0
    private var originalWidth : Int = 0

    private lateinit var sentFilter : Bitmap

    private lateinit var getImage : RetrieveImageHandler


    fun setupUI(sentData : Intent, appContext : Context)
    {
        getImage = RetrieveImageHandler(appContext)

        currentImage = getImage.getBitmapFromFile(sentData.getStringExtra("photo"))

        originalHeight = sentData.getIntExtra("height", 0)
        originalWidth = sentData.getIntExtra("width", 0)

        editHistory = EditHistoryManger(appContext,
            originalHeight, originalWidth)


        val photoLayer = sentData.getStringExtra("layer")

        editHistory.addLayer(currentImage)

        if (photoLayer != null) {
            sentFilter = getImage.getBitmapFromResources(photoLayer.toInt())
            editHistory.addLayer(sentFilter)
            currentImage = sentFilter
        }

    }


    //editHistory code
    private lateinit var editHistory : EditHistoryManger
    fun undo() : Bitmap
    {
        val undoImage : Bitmap? = editHistory.undo()
        if (undoImage != null)
        {
            currentImage = undoImage
        }
        return currentImage
    }

    fun recordLocation()
    {
        editHistory.recordLocation()
    }

    fun addToHistory(image : Bitmap)
    {
        editHistory.add(image)
    }

    var layerPosition : Int = 0

    fun getLayers() : Array<String>
    {
        return editHistory.getLayerList()
    }

    fun removeLayer(position : Int)
    {
        editHistory.deleteLayer(position)
    }

    fun copyLayer(position: Int)
    {
        doAsync {
            editHistory.copyLayer(position)
        }
    }

    fun combineLayer() : Bitmap
    {
        currentImage = editHistory.combineLayers(editHistory.getLayerList(), currentImage.height, currentImage.width)

        return currentImage
    }

    fun viewLayer(position: Int) : Bitmap
    {
        currentImage = editHistory.displayLayer(position)
        return currentImage
    }

    fun addLayer(image : Bitmap)
    {
        editHistory.addLayer(image)
    }

    //getters and setters
    fun getOriginalHeight() : Int
    {
        return originalHeight
    }

    fun getOriginalWidth() : Int
    {
        return originalWidth
    }

}