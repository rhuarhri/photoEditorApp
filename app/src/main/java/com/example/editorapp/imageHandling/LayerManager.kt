package com.example.editorapp.imageHandling

/*
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import java.util.ArrayList


class LayerManager (context : Context){

    private val imageLayers : ArrayList<String> = ArrayList()
    private val maxLayers : Int = 9 //i.e. 10 layers 0 to 9

    private var saveImage : SaveImageHandler = SaveImageHandler(context)
    private var getImage : RetrieveImageHandler = RetrieveImageHandler(context)
    private var deleteImage : RemoveImageHandler = RemoveImageHandler()

    fun getLayer(location : Int) : Bitmap?
    {

            return getImage.getBitmapFromFile(imageLayers[location])

    }

    private var currentLayer : Int = -1 //so the first layer being add is 0
    fun addLayer(image : Bitmap) : Boolean
    {
        saveImage.savePhoto(image, null, null)

        if (currentLayer < maxLayers)
        {
            currentLayer++
        }
        else
        {
            return false //can't add layer
        }

        imageLayers.add(currentLayer, saveImage.savedPhotoPath)

        return true //can add layer

    }

    fun removeLayer(location: Int)
    {
            deleteImage.removeImage(imageLayers[location])
            imageLayers.removeAt(location)

    }

    fun updateLayer(location : Int, image : Bitmap)
    {
        deleteImage.removeImage(imageLayers[location])
        saveImage.savePhoto(image, null, null)
        imageLayers[location] = saveImage.savedPhotoPath
    }

    fun getLayerList() : Array<String>
    {
        return imageLayers.toTypedArray()
    }

    fun getIconsOfLayers(layers : Array<String>, height : Int, width : Int) : Array<Bitmap>
    {
        val imageList : ArrayList<Bitmap> = ArrayList()

        for (i in 0 until layers.size)
        {
                imageList.add(getImage.formatBitmapFromFile(layers[i], height, width))

        }

        val iconArray : Array<Bitmap> = imageList.toTypedArray()

        //here because of list of bitmaps
        System.gc()

        return iconArray
    }

    fun combineLayers(layers : Array<String>, height: Int, width: Int) : Bitmap
    {
        val newImage : Bitmap = Bitmap.createBitmap(height, width, Bitmap.Config.ARGB_8888)

        val canvas : Canvas = Canvas(newImage)

        for (i in layers)
        {
            val currentImage : Bitmap = getImage.formatBitmapFromFile(i, height, width)

            canvas.drawBitmap(currentImage, 0.0f, 0.0f, null)
        }

        return newImage
    }

    fun getCurrentLayerLocation() : Int
    {
        return if (currentLayer > 0) {
            currentLayer
        } else {
            0
        }
    }


}*/