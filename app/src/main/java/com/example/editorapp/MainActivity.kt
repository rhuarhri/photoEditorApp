package com.example.editorapp

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.TextureView
import android.widget.Button
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.editorapp.cameraCode.CameraHandler
import com.example.editorapp.imageHandling.RetrieveImageHandler
import com.example.editorapp.imageHandling.SaveImageHandler
import com.example.editorapp.slideShowCode.SlideShowAdapter
import com.example.editorapp.slideShowCode.SlideShowListener
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.uiThread
import java.io.File

class MainActivity : AppCompatActivity(), SlideShowListener{

    private lateinit var cameraManger : CameraHandler
    private lateinit var cameraBTN : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermission()

        val cameraTV : TextureView = findViewById(R.id.cameraTV)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED) {
            cameraManger = CameraHandler(this, cameraTV)
        }


        cameraBTN = findViewById(R.id.cameraBTN)
        cameraBTN.setOnClickListener {
            val saveImage : SaveImageHandler = SaveImageHandler(applicationContext)
            val photoFile = saveImage.getPhotoFile()


            cameraManger.imageCapture.takePicture(photoFile, object : ImageCapture.OnImageSavedListener
            {
                override fun onImageSaved(file: File) {

                }

                override fun onError(
                    useCaseError: ImageCapture.UseCaseError,
                    message: String,
                    cause: Throwable?
                ) {

                }

            })
        }

        val slideShowListener = this

        async {
            var imageGetter: RetrieveImageHandler = RetrieveImageHandler(applicationContext)

            var images = arrayOf(
                imageGetter.formatBitmapFromResources(R.drawable.aim_filter, 100, 100),
                imageGetter.formatBitmapFromResources(R.drawable.film_filter, 100, 100),
                imageGetter.formatBitmapFromResources(R.drawable.fire_filter, 100, 100),
                imageGetter.formatBitmapFromResources(R.drawable.jail_filter, 100, 100),
                imageGetter.formatBitmapFromResources(R.drawable.money_filter, 100, 100),
                imageGetter.formatBitmapFromResources(R.drawable.photo_filter, 100, 100),
                imageGetter.formatBitmapFromResources(R.drawable.rose_filter, 100, 100),
                imageGetter.formatBitmapFromResources(R.drawable.stage_filter, 100, 100)
            )

            uiThread {
                var overlayRV : RecyclerView = findViewById(R.id.overlayRV)
                val rvAdapter: RecyclerView.Adapter<*> = SlideShowAdapter(applicationContext, images, slideShowListener)
                overlayRV.apply {

                    setHasFixedSize(false)
                    layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)

                    adapter = rvAdapter
                }
            }

        }



    }

    override fun onItemClick(position: Int) {

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
