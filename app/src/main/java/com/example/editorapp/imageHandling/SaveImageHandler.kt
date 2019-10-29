package com.example.editorapp.imageHandling

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class SaveImageHandler (private val context : Context) {

    var savedPhotoPath : String = ""

    @Throws(IOException::class)
    fun getFileLocation() : Uri? {

        var photoUri : Uri? = null

        val photoFile: File? = createImageFile()

        photoFile?.also {
            val foundPhotoURI: Uri = FileProvider.getUriForFile(
                context,
                "com.example.editorapp.fileprovider",
                it
            )

            photoUri = foundPhotoURI
        }

        return photoUri
    }


    @Throws(IOException::class)
    fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        val prefix = "PNG_${timeStamp}_"
        val suffix = ".png"
        return File.createTempFile(
            prefix,
            suffix,
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            savedPhotoPath = absolutePath
        }

    }

    fun savePhoto(photo : Bitmap)
    {
        val imageFile : File = createImageFile()
        val fileStream = FileOutputStream(imageFile)

        photo.compress(Bitmap.CompressFormat.PNG, 85, fileStream)
        fileStream.flush()
        fileStream.close()

    }

    fun getPhotoFile():File
    {
        return createImageFile()
    }

}