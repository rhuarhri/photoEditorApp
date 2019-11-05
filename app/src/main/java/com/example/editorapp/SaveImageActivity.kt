package com.example.editorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import com.google.android.material.snackbar.Snackbar

class SaveImageActivity : AppCompatActivity() {

    private lateinit var typeSpn : Spinner
    private lateinit var nameET : EditText
    private lateinit var locationET : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_image)

        typeSpn = findViewById(R.id.typeSpn)
        ArrayAdapter.createFromResource(applicationContext, R.array.file_types, android.R.layout.simple_spinner_item)
            .also { arrayAdapter ->

                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                typeSpn.adapter = arrayAdapter
            }

        nameET = findViewById(R.id.nameET)
        locationET = findViewById(R.id.locationET)


    }

    public fun createImage(view : View)
    {
        Snackbar.make(view, "image saved", Snackbar.LENGTH_SHORT).show()
    }
}
