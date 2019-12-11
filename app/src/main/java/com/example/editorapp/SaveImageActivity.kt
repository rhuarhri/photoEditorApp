package com.example.editorapp

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.editorapp.imageHandling.SaveImageHandler
import com.google.android.material.snackbar.Snackbar
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.uiThread
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.os.Environment
import android.util.Log
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*
import android.R.attr.description
import android.content.ContentValues
import android.content.Intent
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.net.Uri
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.example.editorapp.imageHandling.EditHistoryManger
import java.text.SimpleDateFormat


class SaveImageActivity : AppCompatActivity() {

    private lateinit var savingImageIV : ImageView
    private lateinit var savingImage : Bitmap
    private lateinit var typeSpn : Spinner
    private lateinit var nameET : EditText
    //private lateinit var locationET : EditText

    private var type : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_image)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED) {
            Log.d("Permissions", "access to external storage granted")
        }
        else{
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            }
            else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_CONTACTS), 111
                )
            }
            Log.d("Permissions", "access to external storage denied")
        }

        savingImageIV = findViewById(R.id.savingIV)

        typeSpn = findViewById(R.id.typeSpn)
        ArrayAdapter.createFromResource(applicationContext, R.array.file_types, android.R.layout.simple_spinner_item)
            .also { arrayAdapter ->

                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                typeSpn.adapter = arrayAdapter
            }

        typeSpn.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                type = parent!!.getItemAtPosition(position).toString()
            }

        }


        nameET = findViewById(R.id.nameET)
        //locationET = findViewById(R.id.locationET)

        val extras : Bundle = intent.extras!!

        val height : Int = extras.getInt("height")
        val width : Int = extras.getInt("width")
        val files : Array<String> = extras.getStringArray("layers")!!

        async {

            val editHistory : EditHistoryManger = EditHistoryManger(applicationContext, height, width)
            val newImage : Bitmap = editHistory.combineLayers(files, height, width)

            uiThread {
                savingImage = newImage
                savingImageIV.setImageBitmap(savingImage)
            }
        }

    }

    private val filepath = "MyFileStorage"

    private val isExternalStorageReadOnly: Boolean get() {
        val extStorageState = Environment.getExternalStorageState()
        return if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            true
        } else {
            false
        }
    }
    private val isExternalStorageAvailable: Boolean get() {
        val extStorageState = Environment.getExternalStorageState()
        return if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            true
        } else{
            false
        }
    }

    public fun createImage(view : View)
    {
        val name : String = nameET.text.toString()

        //val saveImage : SaveImageHandler = SaveImageHandler(applicationContext)

        // Get the context wrapper instance
        val wrapper = ContextWrapper(applicationContext)

        // Initializing a new file
        // The bellow line return a directory in internal storage
        //var path = wrapper.getDir("Pictures", Context.MODE_PRIVATE)

        val path = Environment.getExternalStorageDirectory().toString()
        //Log.d("image file path", "Path: $path")


        // Create a file to save the image
        var file = File(path, "testImageName.jpg")

        /*
        try {
            // Get the file output stream
            val stream: OutputStream = FileOutputStream(file)

            // Compress bitmap
            savingImage.compress(Bitmap.CompressFormat.JPEG, 100, stream)

            // Flush the stream
            stream.flush()

            // Close stream
            stream.close()

            Snackbar.make(view, "image saved", Snackbar.LENGTH_SHORT).show()
        } catch (e: IOException){ // Catch the exception
            e.printStackTrace()
        }*/

        saveImageWithMediaStore()

        //saveImage.savePhoto(savingImage, name, type)


    }

    val REQUEST_TAKE_PHOTO = 1

    public fun saveImageWithMediaStore()
    {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    //...
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.android.fileprovider",
                        it
                    )
                    //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    //startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }

                val wrapper = ContextWrapper(applicationContext)

                var path = wrapper.getDir("Pictures", Context.MODE_PRIVATE)

                var file = File(path, "testImageName.jpg")

                val stream: OutputStream = FileOutputStream(file)

                // Compress bitmap
                savingImage.compress(Bitmap.CompressFormat.JPEG, 100, stream)

                // Flush the stream
                stream.flush()

                // Close stream
                stream.close()


            }
        }




    }

    private lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }
}
