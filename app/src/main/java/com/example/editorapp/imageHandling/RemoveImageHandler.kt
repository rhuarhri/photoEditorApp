package com.example.editorapp.imageHandling

import java.io.File

class RemoveImageHandler {

    fun removeImage(imageFilePath : String) : Boolean
    {
        var successful : Boolean = false
        if (imageFilePath != "")
        {
            val currentImage : File = File(imageFilePath)
            if (currentImage.exists())
            {
                currentImage.delete()
                //file successfully removed
                successful = true
            }

        }

        return successful
    }
}