package com.example.editorapp

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.example.editorapp.imageHandling.EditHistoryManger
import com.example.editorapp.imageHandling.LayerManager
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

        doAsync {
            editHistory.add(currentImage)
        }

        val photoLayer = sentData.getStringExtra("layer")

        layerManager = LayerManager(appContext)
        layerManager.addLayer(currentImage)

        if (photoLayer != null) {
            sentFilter = getImage.getBitmapFromResources(photoLayer.toInt())
            layerManager.addLayer(sentFilter)
        }

    }


    //editHistory code
    private lateinit var editHistory : EditHistoryManger
    fun undo() : Bitmap
    {
        currentImage = editHistory.undo()!!
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

    //layer Manager code
    private lateinit var layerManager : LayerManager

    fun getLayers() : Array<String>
    {
        return layerManager.getLayerList()
    }

    fun removeLayer(position : Int)
    {
        layerManager.removeLayer(position)
    }

    fun copyLayer(position: Int)
    {
        doAsync {
            val copyLayer : Bitmap = layerManager.getLayer(position)!!
            layerManager.addLayer(copyLayer)
        }
    }

    fun combineLayer() : Bitmap
    {
        currentImage = layerManager.combineLayers(layerManager.getLayerList(), originalHeight, originalWidth)

        return currentImage
    }

    fun viewLayer(position: Int) : Bitmap
    {
        currentImage = layerManager.getLayer(position)!!
        return currentImage
    }

    fun addLayer(image : Bitmap)
    {
        layerManager.addLayer(image)
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

    /*
    public fun getCurrentImage() : Bitmap
    {
        return currentImage
    }*/

}