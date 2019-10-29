package com.example.editorapp.imageHandling

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap

class EditHistoryManger(appContext : Context, private val imageHeight : Int, private val imageWidth : Int) {

    private val lastEditedKey : String = "last_edited_image"
    private var currentKeyValue : Int = 0
    private val maxEntryAmount : Int = 10

    private var saveImage : SaveImageHandler
    private var getImage : RetrieveImageHandler
    private var deleteImage : RemoveImageHandler

    private var appPreferences : SharedPreferences
    private var preferencesEditor : SharedPreferences.Editor
    val name : String = "edit_history"

    init {
        appPreferences = appContext.getSharedPreferences(name, Context.MODE_PRIVATE)
        preferencesEditor = appPreferences.edit()
        saveImage = SaveImageHandler(appContext)
        getImage = RetrieveImageHandler(appContext)
        deleteImage = RemoveImageHandler()
    }

    fun recordLocation()
    {
        preferencesEditor.putInt(lastEditedKey, currentKeyValue)
        preferencesEditor.apply()
    }

    fun getLastImage() : Bitmap?
    {
        if (appPreferences.contains(lastEditedKey))
        {
            val imageKey : Int = appPreferences.getInt(lastEditedKey, 0)
            if (appPreferences.contains("$imageKey"))
            {
                currentKeyValue = imageKey
                val fileLocation : String = appPreferences.getString("$currentKeyValue", "")!!
                val foundImage : Bitmap = getImage.getBitmapFromFile(fileLocation, imageHeight, imageWidth)

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
        }
    }

    fun getCurrentImage() : Bitmap?
    {

        if (appPreferences.contains("$currentKeyValue"))
        {
            val fileLocation : String = appPreferences.getString("$currentKeyValue", "")!!
            val foundImage : Bitmap = getImage.getBitmapFromFile(fileLocation, imageHeight, imageWidth)

            return foundImage
        }
        else
        {
            return null
        }
    }

    fun getCurrentImageFilePath() : String?
    {

        if (appPreferences.contains("$currentKeyValue"))
        {
            val fileLocation : String = appPreferences.getString("$currentKeyValue", "")!!

            return fileLocation
        }
        else
        {
            return null
        }
    }

    fun add(image : Bitmap)
    {
        saveImage.savePhoto(image)

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
        }

    }

    fun undo() : Bitmap?
    {

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
                val foundImage : Bitmap = getImage.getBitmapFromFile(fileLocation, imageHeight, imageWidth)

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
        }

    }

    private fun getKeyValue() : Int
    {
        ++currentKeyValue
        if (currentKeyValue < maxEntryAmount)
        {
            return currentKeyValue
        }
        else
        {
            currentKeyValue = 0
            return currentKeyValue
        }

    }
}