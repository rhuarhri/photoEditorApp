package com.example.editorapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.example.editorapp.fragmentCode.welcomeFragment.AcceptImageFRG
import com.example.editorapp.fragmentCode.welcomeFragment.FromWelcomeFragment
import com.example.editorapp.imageHandling.EditHistoryManger
import com.example.editorapp.imageHandling.RetrieveImageHandler
import com.example.editorapp.imageHandling.SaveImageHandler
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.uiThread

class WelcomeActivity : AppCompatActivity(), FromWelcomeFragment{


    private val requestCode : Int = 111

    //private var imageLoc : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        //imagePreviewIV = findViewById(R.id.imagePreviewIV)

        /*
        async {

            //val getImage : RetrieveImageHandler = RetrieveImageHandler(applicationContext)

            val editHistory : EditHistoryManger = EditHistoryManger(applicationContext, 0, 0)
            chosenImage = editHistory.getLastImage()

            uiThread {
                if (chosenImage != null){
                //imagePreviewIV.setImageBitmap(chosenImage!!)
                    }
            }
        }*/



/*
        doneBTN = findViewById(R.id.doneBTN)
        doneBTN.setOnClickListener {

            if (chosenImage != null) {

                val saveImage : SaveImageHandler = SaveImageHandler(applicationContext)
                saveImage.savePhoto(chosenImage!!)

                val goTo: Intent = Intent(this, ImagePreviewActivity::class.java)
                goTo.putExtra("photo", saveImage.savedPhotoPath)
                goTo.putExtra("height", chosenImage!!.height)
                goTo.putExtra("width", chosenImage!!.width)
                startActivity(goTo)
            }
        }

 */

    }

    public fun openCamera(view: View)
    {
        val goTo : Intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(goTo)
    }

    public fun findImage(view: View)
    {
        val chooser = Intent()
            .setType("image/*")
            .setAction(Intent.ACTION_GET_CONTENT)

        startActivityForResult(Intent.createChooser(chooser, "Select a image"), requestCode)
    }

    public fun getSavedImage(view: View)
    {
        val acceptImageFRG : AcceptImageFRG = AcceptImageFRG()

        val fragIn : Bundle = Bundle()
        fragIn.putString("function", "saved")

        acceptImageFRG.arguments = fragIn

        val openFragment : FragmentTransaction = supportFragmentManager.beginTransaction()
        openFragment.replace(R.id.acceptImageFRGLoc, acceptImageFRG)

        openFragment.commit()
    }


    private var chosenImage: Bitmap? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK)
        {
            when(requestCode)
            {
                requestCode -> {

                    val acceptImageFRG : AcceptImageFRG = AcceptImageFRG()

                    val imageLoc : String = data?.data.toString()

                    val fragIn : Bundle = Bundle()
                    fragIn.putString("image", imageLoc)
                    fragIn.putString("function", "search")

                    acceptImageFRG.arguments = fragIn

                    val openFragment : FragmentTransaction = supportFragmentManager.beginTransaction()
                    openFragment.replace(R.id.acceptImageFRGLoc, acceptImageFRG)

                    openFragment.commit()
                }
            }
        }
    }
}
