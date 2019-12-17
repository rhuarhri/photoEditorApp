package com.example.editorapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentTransaction
import com.example.editorapp.fragmentCode.welcomeFragment.AcceptImageFRG
import com.example.editorapp.fragmentCode.welcomeFragment.FromWelcomeFragment

class WelcomeActivity : AppCompatActivity(), FromWelcomeFragment{

    private val requestCode : Int = 111

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

    }

    fun openCamera(view: View)
    {
        val goTo = Intent(applicationContext, MainActivity::class.java)
        startActivity(goTo)
    }

    fun findImage(view: View)
    {
        val chooser = Intent()
            .setType("image/*")
            .setAction(Intent.ACTION_GET_CONTENT)

        startActivityForResult(Intent.createChooser(chooser, "Select a image"), requestCode)
    }

    fun getSavedImage(view: View)
    {
        val acceptImageFRG = AcceptImageFRG()

        val fragIn = Bundle()
        fragIn.putString("function", "saved")

        acceptImageFRG.arguments = fragIn

        val openFragment : FragmentTransaction = supportFragmentManager.beginTransaction()
        openFragment.replace(R.id.acceptImageFRGLoc, acceptImageFRG)

        openFragment.commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK)
        {
            when(requestCode)
            {
                requestCode -> {

                    val acceptImageFRG = AcceptImageFRG()

                    val imageLoc : String = data?.data.toString()

                    val fragIn = Bundle()
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