package com.example.editorapp

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.editorapp.imageHandling.LayerManager
import com.example.editorapp.imageHandling.SaveImageHandler
import com.google.android.material.snackbar.Snackbar
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.uiThread

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

            val layerManager : LayerManager = LayerManager(applicationContext)
            val newImage : Bitmap = layerManager.combineLayers(files, height, width)

            uiThread {
                savingImage = newImage
                savingImageIV.setImageBitmap(savingImage)
            }
        }

    }

    public fun createImage(view : View)
    {
        val name : String = nameET.text.toString()

        val saveImage : SaveImageHandler = SaveImageHandler(applicationContext)

        saveImage.savePhoto(savingImage, name, type)

        Snackbar.make(view, "image saved", Snackbar.LENGTH_SHORT).show()
    }
}
