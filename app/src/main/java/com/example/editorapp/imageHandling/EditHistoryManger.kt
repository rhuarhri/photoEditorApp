package com.example.editorapp.imageHandling

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log

class EditHistoryManger(appContext : Context, private val imageHeight : Int, private val imageWidth : Int) {

    private val lastEditedKey : String = "last_edited_image"

    private var saveImage : SaveImageHandler
    private var getImage : RetrieveImageHandler
    private var deleteImage : RemoveImageHandler

    private var appPreferences : SharedPreferences
    private var preferencesEditor : SharedPreferences.Editor
    private val imageLayers : ArrayList<ArrayList<String>> = arrayListOf()
    private val imageHistory : ArrayList<String> = arrayListOf()
    val name : String = "edit_history"

    init {
        appPreferences = appContext.getSharedPreferences(name, MODE_PRIVATE)
        preferencesEditor = appPreferences.edit()
        saveImage = SaveImageHandler(appContext)
        getImage = RetrieveImageHandler(appContext)
        deleteImage = RemoveImageHandler()
    }

    fun recordLocation()
    {
        val combinedImage : Bitmap = combineLayers(getLayerList(), imageHeight, imageWidth)

        saveImage.savePhoto(combinedImage, null, null)

        preferencesEditor.putString(lastEditedKey, saveImage.savedPhotoPath)
        preferencesEditor.apply()
    }

    fun getLastImage() : Bitmap?
    {
        if (appPreferences.contains(lastEditedKey)) {
            val imageFileLoc: String = appPreferences.getString(lastEditedKey, "")!!
            return getImage.getBitmapFromFile(imageFileLoc)
        }
        else {
            return null
        }
    }

    fun getLastImageLoc() : String?
    {
        if (appPreferences.contains(lastEditedKey)) {
            return appPreferences.getString(lastEditedKey, "")!!
        }
        else
        {
            return null
        }
    }

    fun getCurrentImage() : Bitmap?
    {

        if (imageHistory[currentLocation] != null)
        {
            val fileLocation : String = imageHistory[currentLocation]!!
            val foundImage : Bitmap = getImage.getBitmapFromFile(fileLocation)

            return foundImage
        }
        else
        {
            return null
        }
    }

    fun getCurrentImageFilePath() : String?
    {

        if (imageHistory[currentLocation] != null)
        {
            return imageHistory.last()
        }
        else
        {
            return null
        }
    }

    private var currentLayer : Int = -1 //so the first layer being add is 0
    private val maxLayers : Int = 9 //i.e. 10 layers 0 to 9
    fun addLayer(image : Bitmap)
    {

        saveImage.savePhoto(image, null, null)

        if (currentLayer < maxLayers)
        {
            currentLayer++
            Log.d("layer", "layer added at location $currentLayer")
            imageLayers.add(arrayListOf<String>())

            imageLayers[currentLayer].add(saveImage.savedPhotoPath)

        }

    }

    private fun addLayerAt(image : Bitmap, location : Int)
    {
        saveImage.savePhoto(image, null, null)

        Log.d("layer", "adding layer at $location")

        if (currentLayer < maxLayers)
        {
            imageLayers.add(location, arrayListOf<String>())

            imageLayers[location].add(saveImage.savedPhotoPath)
        }
    }

    fun getLayerList() : Array<String>
    {

        Log.d("layer location", "the location is $currentLayer")

        val showingLayers : ArrayList<String> = arrayListOf()

        var test : Int = 0

        for (layer in imageLayers)
        {

                Log.d("layer", "layer found at location $test")
                test++
                showingLayers.add(layer.last())

        }

        return showingLayers.toTypedArray()
    }

    fun combineLayers(layers : Array<String>, height: Int, width: Int) : Bitmap
    {
        val newImage : Bitmap = Bitmap.createBitmap(height, width, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(newImage)

        for (i in layers)
        {
            val currentImage : Bitmap = getImage.formatBitmapFromFile(i, height, width)

            val scaledImage : Bitmap = Bitmap.createScaledBitmap(currentImage, width, height, false)

            canvas.drawBitmap(scaledImage, 0.0f, 0.0f, null)
        }

        return newImage
    }

    fun deleteLayer(position : Int) : Bitmap
    {
        imageLayers.removeAt(position)
        currentLayer = 0
        val imageToShow = imageLayers[currentLayer].last()
        val foundImage : Bitmap = getImage.getBitmapFromFile(imageToShow)
        return foundImage
    }

    fun copyLayer(position: Int) : Bitmap
    {
        currentLayer = position
        val copyImagePath = imageLayers[currentLayer].last()
        val copyImage : Bitmap = getImage.getBitmapFromFile(copyImagePath)
        addLayerAt(copyImage, currentLayer++)
        return copyImage
    }

    fun displayLayer(position: Int) : Bitmap
    {
        currentLayer = position

        val viewImagePath = imageLayers[currentLayer].last()
        val viewImage : Bitmap = getImage.getBitmapFromFile(viewImagePath)
        return viewImage
    }

    private var currentLocation : Int = 0
    fun add(image : Bitmap) {

        Log.d("edit history", "image added to history")
        saveImage.savePhoto(image, null, null)

        imageLayers[currentLayer].add(saveImage.savedPhotoPath)

    }

    fun undo() : Bitmap?
    {

        Log.d("Edit history", "current location is $currentLayer history size ${imageLayers[currentLayer].size}")

        if (imageLayers[currentLayer].size > 1)
        {


        val imageToUndo = imageLayers[currentLayer].last()

        deleteImage.removeImage(imageToUndo)

            val lastLocation = imageLayers[currentLayer].size -1
            imageLayers[currentLayer].removeAt(lastLocation)


            //val secondToLastLocation : Int = imageLayers[currentLayer].size -2 //which is -1 to get location and -1 to get to second to last
            val imageToShow = imageLayers[currentLayer].last()//[secondToLastLocation]
            val foundImage : Bitmap = getImage.getBitmapFromFile(imageToShow)
            return foundImage
        }
        else
        {
            return null
        }


    }

}