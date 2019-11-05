package com.example.editorapp.fragmentCode.welcomeFragment

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.editorapp.ImagePreviewActivity
import com.example.editorapp.R
import com.example.editorapp.imageHandling.EditHistoryManger
import com.example.editorapp.imageHandling.SaveImageHandler
import kotlinx.android.synthetic.main.accept_image_frg_layout.*
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.uiThread


class AcceptImageFRG : Fragment() {

    private lateinit var callback : FromWelcomeFragment

    private lateinit var appContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)

        appContext = context

        val activity : Activity = context as Activity
        try {
            callback = activity as FromWelcomeFragment
        }
        catch(e : Exception)
        {
            Toast.makeText(context, "error is ${e.toString()}", Toast.LENGTH_LONG).show()
        }
    }

    private var imageLocation : String? = ""
    private lateinit var displayFunction : String

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (arguments != null)
        {
            imageLocation = arguments!!.getString("image")
            displayFunction = arguments!!.getString("function")!!

            when(displayFunction)
            {
                "saved" -> displaySavedImage()
                "camera" -> displayCameraImage()
                "search" -> displaySearchImage()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.accept_image_frg_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val previewIV : ImageView = view.findViewById(R.id.previewIV)

        val doneBTN : ImageButton = view.findViewById(R.id.doneBTN)
        doneBTN.setOnClickListener {

            val saveImage : SaveImageHandler = SaveImageHandler(appContext)
            saveImage.savePhoto(chosenImage)

            val goTo : Intent = Intent(appContext, ImagePreviewActivity::class.java)
            goTo.putExtra("photo", saveImage.savedPhotoPath)
            goTo.putExtra("height", chosenImage.height)
            goTo.putExtra("width", chosenImage.width)
            startActivity(goTo)
        }

        val cancelBTN : ImageButton = view.findViewById(R.id.cancelBTN)
        cancelBTN.setOnClickListener {
            fragmentManager!!.beginTransaction().remove(this).commit()
        }

    }

    private lateinit var chosenImage : Bitmap

    private fun displaySavedImage()
    {
        async {
            val editHistory: EditHistoryManger = EditHistoryManger(appContext, 0, 0)
            chosenImage = editHistory.getLastImage()!!


            uiThread {
                if (chosenImage != null) {
                    previewIV.setImageBitmap(chosenImage)
                }
            }
        }
    }

    private fun displayCameraImage()
    {

    }

    private fun displaySearchImage()
    {

        val contentResolver : ContentResolver = appContext.contentResolver

        async {

            val filePath = Uri.parse(imageLocation)

            chosenImage = MediaStore.Images.Media.getBitmap(
                contentResolver,
                filePath
            )
            uiThread {
                previewIV.setImageBitmap(chosenImage)
            }
        }

    }
}