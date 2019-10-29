package com.example.editorapp.imageHandling

/*
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.nn4wchallenge.R
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class PreviewImageHandler(context : Context) {

    private var dialogTitle : TextView
    private var dialogImage : ImageView
    private var dialogLikeBTN : Button
    private var dialogDislikeBTN : Button

    private var createdDialog : Dialog = Dialog(context)

    private val imageRetriever : RetrieveImageHandler = RetrieveImageHandler(context)

    private val imageRemover : RemoveImageHandler = RemoveImageHandler()

    private val imageHeight : Int
    private val imageWidth : Int

    init {
        createdDialog.setContentView(R.layout.image_alert_layout)

        dialogTitle = createdDialog.findViewById(R.id.TitleTXT)

        dialogImage = createdDialog.findViewById(R.id.capturedPictureIV)

        imageHeight = dialogImage.height
        imageWidth = dialogImage.width

        dialogLikeBTN = createdDialog.findViewById(R.id.likeBTN)

        dialogDislikeBTN = createdDialog.findViewById(R.id.dislikeBTN)
    }

    fun showDialog(imagePath : String, Title : String)
    {

        doAsync {
            val image : Bitmap = imageRetriever.getBitmapFromFile(imagePath, 
                setToDefaultIfZero(imageHeight),
                setToDefaultIfZero(imageWidth))
            uiThread{
                dialogTitle.text = Title

                dialogImage.setImageBitmap(image)
                setupLikeBTN()
                setupDislikeBTN(imagePath)
                createdDialog.show()
            }
        }


    }

    private fun setToDefaultIfZero(value : Int) : Int
    {
        if (value == 0)
        {
            return 400 //defualt value
        }
        else
        {
            return value
        }
    }

    private fun setupLikeBTN()
    {
        dialogLikeBTN.setOnClickListener {

            createdDialog.cancel()
        }
    }

    private fun setupDislikeBTN(imageFilePath : String)
    {
        dialogDislikeBTN.setOnClickListener {

            imageRemover.removeImage(imageFilePath)

            createdDialog.cancel()
        }
    }
}
        */