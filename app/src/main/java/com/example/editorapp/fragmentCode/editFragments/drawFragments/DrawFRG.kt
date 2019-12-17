package com.example.editorapp.fragmentCode.editFragments.drawFragments

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.editorapp.R
import com.example.editorapp.fragmentCode.editFragments.FromFragment

class DrawFRG : Fragment() {

    private lateinit var callback : FromFragment
    override fun onAttach(context: Context) {
        super.onAttach(context)

        val activity : Activity = context as Activity
        try {
            callback = activity as FromFragment
        }
        catch(e : Exception)
        {
            Toast.makeText(context, "error is $e", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.draw_frg_layout, container, false)
    }

    private var colour : Int = 0
    private lateinit var colourPreviewIV : ImageView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textSizeET : TextView = view.findViewById(R.id.textSizeET)

        colourPreviewIV = view.findViewById(R.id.colorPreviewIV)

        setupColourButtons(view)

        val setBTN : ImageButton = view.findViewById(R.id.setBTN)
        setBTN.setOnClickListener {
            callback.fromDraw(textSizeET.text.toString().toFloat(), colour)
        }
    }

    private fun setupColourButtons(view : View)
    {
        val blackBTN : Button = view.findViewById(R.id.blackBTN)
        blackBTN.setOnClickListener {
            setTextColour("#000000")
        }

        val whiteBTN : Button = view.findViewById(R.id.whiteBTN)
        whiteBTN.setOnClickListener {
            setTextColour("#ffffff")
        }

        val redBTN : Button = view.findViewById(R.id.redBTN)
        redBTN.setOnClickListener {
            setTextColour("#ff0000")
        }

        val pinkBTN : Button = view.findViewById(R.id.pinkBTN)
        pinkBTN.setOnClickListener {
            setTextColour("#ff00ff")
        }

        val yellowBTN : Button = view.findViewById(R.id.yellowBTN)
        yellowBTN.setOnClickListener {
            setTextColour("#ffff00")
        }

        val greenBTN : Button = view.findViewById(R.id.greenBTN)
        greenBTN.setOnClickListener {
            setTextColour("#00ff00")
        }

        val lightBlueBTN : Button = view.findViewById(R.id.lightBlueBTN)
        lightBlueBTN.setOnClickListener {
            setTextColour("#00ffff")
        }

        val blueBTN : Button = view.findViewById(R.id.blueBTN)
        blueBTN.setOnClickListener {
            setTextColour("#0000ff")
        }
    }

    private fun setTextColour(hexColour : String)
    {
        colour = Color.parseColor(hexColour)
        colourPreviewIV.setColorFilter(colour)
    }
}