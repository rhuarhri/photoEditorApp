package com.example.editorapp

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.TextureView
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.editorapp.cameraCode.CameraHandler
import com.example.editorapp.imageHandling.RetrieveImageHandler
import com.example.editorapp.imageHandling.SaveImageHandler
import com.example.editorapp.slideShowCode.*
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.uiThread
import java.io.File

class MainActivity : AppCompatActivity(), SlideShowListener{

    private lateinit var cameraManger : CameraHandler
    private lateinit var cameraBTN : Button
    private lateinit var overlayIV : ImageView

    private val images = arrayOf((R.drawable.aim_filter).toString(), (R.drawable.film_filter).toString(),
    (R.drawable.fire_filter).toString(), (R.drawable.jail_filter).toString(),
    (R.drawable.money_filter).toString(), (R.drawable.photo_filter).toString(),
    (R.drawable.rose_filter).toString(), (R.drawable.stage_filter).toString()
    )

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

        overlayIV = findViewById(R.id.overlayIV)

        val slideShowListener = this

                var overlayRV : RecyclerView = findViewById(R.id.overlayRV)
                val rvAdapter: RecyclerView.Adapter<*> = ShowOverlaysAdapter(applicationContext, images, slideShowListener)
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
        val imageGetter: RetrieveImageHandler = RetrieveImageHandler(applicationContext)

        val chosenFilter : Bitmap = imageGetter.formatBitmapFromResources(images[position].toInt(), overlayIV.height, overlayIV.width)

        overlayIV.setImageBitmap(chosenFilter)
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
