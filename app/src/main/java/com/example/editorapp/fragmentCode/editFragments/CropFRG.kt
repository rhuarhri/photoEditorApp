package com.example.editorapp.fragmentCode.editFragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.editorapp.R

class CropFRG : Fragment() {

    public val shapeColour : String = "#000000"

    private lateinit var callback : FromFragment

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val activity : Activity = context as Activity
        try {
            callback = activity as FromFragment
        }
        catch(e : Exception)
        {
            Toast.makeText(context, "error is ${e.toString()}", Toast.LENGTH_LONG).show()
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.crop_frg_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var function = ""

        val squareBTN : ImageButton = view.findViewById(R.id.squareBTN)
        squareBTN.setOnClickListener {
            function = "square"
        }

        val applyBTN : ImageButton = view.findViewById(R.id.applyBTN)
        applyBTN.setOnClickListener {
            callback.fromCropFRG(function)
        }

    }

    }