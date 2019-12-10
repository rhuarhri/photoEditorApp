package com.example.editorapp.imageHandling

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.util.Log

class EditHistoryManger(appContext : Context, private val imageHeight : Int, private val imageWidth : Int) {

    private val lastEditedKey : String = "last_edited_image"
    private var currentKeyValue : Int = 0
    private val maxEntryAmount : Int = 10

    private var saveImage : SaveImageHandler
    private var getImage : RetrieveImageHandler
    private var deleteImage : RemoveImageHandler

    private var appPreferences : SharedPreferences
    private var preferencesEditor : SharedPreferences.Editor
    private val imageHistory : Array<String?> = arrayOf(null, null, null, null, null, null, null, null, null, null)
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
        //TODO all the layers need to be joined before last image saved
        //preferencesEditor.putString(lastEditedKey, imageHistory[currentLocation])
        //preferencesEditor.apply()
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
            return imageHistory[currentLocation]!!
        }
        else
        {
            return null
        }
    }

    private var currentLocation : Int = 0
    fun add(image : Bitmap)
    {
        Log.d("edit history", "image added to history")
        saveImage.savePhoto(image, null, null)

        currentLocation++

        if (imageHistory[currentLocation] == null)
        {
            imageHistory[currentLocation] = saveImage.savedPhotoPath
        }
        else
        {
            deleteImage.removeImage(imageHistory[currentLocation]!!)
            imageHistory[currentLocation] = saveImage.savedPhotoPath
        }
        /*
        if (appPreferences.contains("${getKeyValue()}"))
        {
            val fileLocation : String = appPreferences.getString("$currentKeyValue", "")!!
            //Log.d("removed image", "The removed image is $fileLocation")
            deleteImage.removeImage(fileLocation)
            preferencesEditor.putString("$currentKeyValue", saveImage.savedPhotoPath)
            preferencesEditor.apply()
        }
        else
        {
            preferencesEditor.putString("$currentKeyValue", saveImage.savedPhotoPath)
            preferencesEditor.apply()
        }*/

    }

    fun undo() : Bitmap?
    {

        if (imageHistory[currentLocation] != null)
        {
            deleteImage.removeImage(imageHistory[currentLocation]!!)
            imageHistory[currentLocation] = null

            if (currentLocation > 0)
            {
                currentLocation--
            }
            else
            {
                currentLocation = imageHistory.size - 1
            }

            val fileLoc : String = imageHistory[currentLocation]!!

            val foundImage : Bitmap = getImage.getBitmapFromFile(fileLoc)

            return foundImage
        }
        else
        {
            return null
        }
        /*
        if (appPreferences.contains("$currentKeyValue"))
        {

            var fileLocation : String = appPreferences.getString("$currentKeyValue", "")!!
            deleteImage.removeImage(fileLocation)

            if (currentKeyValue != 0){
                --currentKeyValue
            }
            else
            {
                currentKeyValue = maxEntryAmount
            }

            if (appPreferences.contains("$currentKeyValue")) {
                fileLocation = appPreferences.getString("$currentKeyValue", "")!!
                val foundImage : Bitmap = getImage.getBitmapFromFile(fileLocation)

                return foundImage
            }
            else
            {
                return null
            }

        }
        else
        {
         return null
        }*/

    }

}