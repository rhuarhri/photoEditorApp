package com.example.editorapp.imageHandling

import java.io.File

class RemoveImageHandler {

    fun removeImage(imageFilePath : String) : Boolean
    {
        var successful = false
        if (imageFilePath != "")
        {
            val currentImage = File(imageFilePath)
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