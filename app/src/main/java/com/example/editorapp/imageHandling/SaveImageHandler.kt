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
    private fun createImageFile(): File {
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
            savedPhotoPath = absolutePath
        }

    }

    @Throws(IOException::class)
    private fun createCustomImageFile(name : String, type : String) : File
    {
        val storageDir: File = context.getExternalFilesDir(Environment.DIRECTORY_DCIM)!!
        return File.createTempFile(
            name,
            type,
            storageDir /* directory */
        ).apply {
            savedPhotoPath = absolutePath
        }
    }

    fun savePhoto(photo : Bitmap, name: String?, type: String?)
    {
        val imageFile: File =
        if (name == null || type == null) {
             createImageFile()
        }
        else {
            createCustomImageFile(name, type)
        }

        val fileStream = FileOutputStream(imageFile)

        photo.compress(getCompressionType(type), 85, fileStream)


        fileStream.flush()
        fileStream.close()

    }

    private fun getCompressionType(type : String?) : Bitmap.CompressFormat
    {
        return when(type) {
            ".jpeg" -> Bitmap.CompressFormat.JPEG
            ".png" -> Bitmap.CompressFormat.PNG
            ".WEBP" -> Bitmap.CompressFormat.WEBP
            else -> Bitmap.CompressFormat.PNG
        }
    }

    fun getPhotoFile():File
    {
        return createImageFile()
    }

}