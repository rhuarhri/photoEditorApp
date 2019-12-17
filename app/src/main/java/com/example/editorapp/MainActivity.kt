package com.example.editorapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.TextureView
import android.widget.Button
import android.widget.ImageView
import androidx.camera.core.ImageCapture
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.editorapp.cameraCode.CameraHandler
import com.example.editorapp.imageHandling.SaveImageHandler
import com.example.editorapp.slideShowCode.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.File

class MainActivity : AppCompatActivity(), SlideShowListener{

    private lateinit var cameraManger : CameraHandler
    private lateinit var cameraBTN : Button
    private lateinit var overlayIV : ImageView

    private lateinit var activityViewModel : MainActVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activityViewModel = ViewModelProviders.of(this).get(MainActVM::class.java)

        checkPermission()

        val cameraTV : TextureView = findViewById(R.id.cameraTV)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED) {
            cameraManger = CameraHandler(this, cameraTV)
        }


        cameraBTN = findViewById(R.id.cameraBTN)
        cameraBTN.setOnClickListener {
            val saveImage = SaveImageHandler(applicationContext)
            val photoFile = saveImage.getPhotoFile()


            cameraManger.imageCapture.takePicture(photoFile, object : ImageCapture.OnImageSavedListener
            {
                override fun onImageSaved(file: File) {
                    doAsync {

                        val goto : Intent = activityViewModel.sendCapturedImage(applicationContext, file)

                        uiThread {

                            startActivity(goto)
                        }
                    }
                }

                override fun onError(
                    useCaseError: ImageCapture.UseCaseError,
                    message: String,
                    cause: Throwable?
                ) {

                }

            })
        }

        overlayIV = findViewById(R.id.overlayIV)
        doAsync {
            val chosenFilter : Bitmap = activityViewModel.getChosenFilter(applicationContext)
            uiThread {
                overlayIV.setImageBitmap(chosenFilter)
            }
        }

        val slideShowListener = this

                val overlayRV : RecyclerView = findViewById(R.id.overlayRV)
                val rvAdapter: RecyclerView.Adapter<*> = ShowOverlaysAdapter(applicationContext, activityViewModel.images, slideShowListener)
                overlayRV.apply {

                    setHasFixedSize(false)
                    layoutManager = LinearLayoutManager(
                        applicationContext,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )

                    adapter = rvAdapter
                }

    }

    override fun onItemClick(position: Int) {
        overlayIV.setImageBitmap(activityViewModel.setChosenFilter(applicationContext, position, overlayIV.height, overlayIV.width))
    }

    private fun checkPermission()
    {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {

            //permission not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                //show explanation
            } else {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA),
                    1)
                //result of permission granting process
            }
        } else {
            // Permission has already been granted
        }
    }

}
