package com.example.editorapp.fragmentCode.editFragments

import android.app.Activity
import android.content.Context
import android.graphics.ColorFilter
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.editorapp.R

class ColourChangeFRG : Fragment()
{


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

    //private val converter : DataTranslation = DataTranslation()
    private var redAmount : Int = 100
    private var blueAmount : Int = 100
    private var greenAmount : Int = 100

    var paint : Paint = Paint()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.colour_change_frag_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val colorR : SeekBar = view.findViewById(R.id.redSB)
        val colorG : SeekBar = view.findViewById(R.id.greenSB)
        val colorB : SeekBar = view.findViewById(R.id.blueSB)
        val applyBTN : ImageButton = view.findViewById(R.id.applyBTN)

        val redAmountTXT : TextView = view.findViewById(R.id.redAmountTXT)
        redAmountTXT.text = "$redAmount"
        val greenAmountTXT : TextView = view.findViewById(R.id.greenAmountTXT)
        greenAmountTXT.text = "$greenAmount"
        val blueAmountTXT : TextView = view.findViewById(R.id.blueAmountTXT)
        blueAmountTXT.text = "$blueAmount"


        colorR.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                redAmount = progress
                redAmountTXT.text = "$redAmount"
                paint = createPaint(redAmount, greenAmount, blueAmount)
                callback.fromColourChangeFRG("preview", paint)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        colorG.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                greenAmount = progress
                greenAmountTXT.text = "$greenAmount"
                paint = createPaint(redAmount, greenAmount, blueAmount)
                callback.fromColourChangeFRG("preview", paint)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        colorB.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                blueAmount = progress
                blueAmountTXT.text = "$blueAmount"
                paint = createPaint(redAmount, greenAmount, blueAmount)
                callback.fromColourChangeFRG("preview", paint)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        applyBTN.setOnClickListener {
            //callback.applyColourChangeFRG(paint)
            callback.fromColourChangeFRG("apply", paint)
        }

    }

    private fun createPaint(red : Int, green : Int, blue : Int) : Paint
    {
        //0 is none of the colour 1 is all of it
        val redValue : Float = red / 100f

        val greenValue : Float = green / 100f

        val blueValue : Float = blue / 100f


        val changeCM : ColorMatrix = ColorMatrix(floatArrayOf(
            redValue,    0f,            0f,        0f, 0f,
            0f,          greenValue,    0f,        0f, 0f,
            0f,          0f,            blueValue, 0f, 0f,
            0f,          0f,            0f,        1f, 0f,
            0f,          0f,            0f,        0f, 1f))

        val colourChangeFilter : ColorFilter = ColorMatrixColorFilter(changeCM)

        val paint : Paint = Paint()
        paint.colorFilter = colourChangeFilter

        return paint

    }
}