package com.example.editorapp

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.example.editorapp.imageHandling.EditHistoryManger
import com.example.editorapp.imageHandling.LayerManager
import com.example.editorapp.imageHandling.RetrieveImageHandler
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class ImagePreviewActVM : ViewModel() {

    public lateinit var currentImage : Bitmap
    private var originalHeight : Int = 0
    private var originalWidth : Int = 0

    private lateinit var sentFilter : Bitmap

    private lateinit var getImage : RetrieveImageHandler


    public fun setupUI(sentData : Intent, appContext : Context)
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
    public fun undo() : Bitmap
    {
        currentImage = editHistory.undo()!!
        return currentImage
    }

    public fun recordLocation()
    {
        editHistory.recordLocation()
    }

    public fun addToHistory(image : Bitmap)
    {
        editHistory.add(image)
    }

    //layer Manager code
    private lateinit var layerManager : LayerManager

    public fun getLayers() : Array<String>
    {
        return layerManager.getLayerList()
    }

    public fun removeLayer(position : Int)
    {
        layerManager.removeLayer(position)
    }

    public fun copyLayer(position: Int)
    {
        doAsync {
            var copyLayer : Bitmap = layerManager.getLayer(position)!!
            layerManager.addLayer(copyLayer)
        }
    }

    public fun combineLayer() : Bitmap
    {
        currentImage = layerManager.combineLayers(layerManager.getLayerList(), originalHeight, originalWidth)

        return currentImage
    }

    public fun viewLayer(position: Int) : Bitmap
    {
        currentImage = layerManager.getLayer(position)!!
        return currentImage
    }

    public fun addLayer(image : Bitmap)
    {
        layerManager.addLayer(image)
    }

    //getters and setters
    public fun getOriginalHeight() : Int
    {
        return originalHeight
    }

    public fun getOriginalWidth() : Int
    {
        return originalWidth
    }

    /*
    public fun getCurrentImage() : Bitmap
    {
        return currentImage
    }*/

}