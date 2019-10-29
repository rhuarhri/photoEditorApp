package com.example.editorapp.fragmentCode

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
import com.example.editorapp.R

class AddTextFRG : Fragment() {

    private val paintText : Paint  = Paint()
    private lateinit var messageET : EditText
    private lateinit var sizeET : EditText
    private lateinit var colourPreviewIV : ImageView


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

    private var positionY : Int = 0
    private var positionX : Int = 0

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (arguments != null)
        {
            positionY = arguments!!.getInt("posY")!!
            positionX = arguments!!.getInt("posX")!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.add_text_frag_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        messageET = view.findViewById(R.id.messageET)

        sizeET = view.findViewById(R.id.textSizeET)

        colourPreviewIV = view.findViewById(R.id.colorPreviewIV)

        setupColourButtons(view)

        val addTextBTN : ImageButton = view.findViewById(R.id.addTextBTN)
        addTextBTN.setOnClickListener {
            paintText.textSize = sizeET.text.toString().toFloat()
            callback.fromAddText(paintText, positionX, positionY, messageET.text.toString())
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
        val colour : Int = Color.parseColor(hexColour)
        paintText.color = colour
        colourPreviewIV.setColorFilter(colour)
    }

    private fun setTextSize()
    {
        val size : Int = sizeET.text.toString().toInt()
        paintText.textSize = size.toFloat()

    }

}