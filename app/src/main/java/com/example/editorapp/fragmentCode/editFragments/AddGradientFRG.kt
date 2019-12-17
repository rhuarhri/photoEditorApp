package com.example.editorapp.fragmentCode.editFragments

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.editorapp.R
import com.example.nn4wchallenge.database.external.DataTranslation
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class AddGradientFRG() : Fragment(), AdapterView.OnItemSelectedListener, Parcelable {

    private var gradientType : String = ""
    private var gradientColour : Int = Color.parseColor("#00ffffff")
    private var red : Int = 0
    private var green : Int = 0
    private var blue : Int = 0

    private val hexTranslator : DataTranslation = DataTranslation()

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

    private var imageHeight : Float = 0f
    private var imageWidth : Float = 0f

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (arguments != null)
        {
            imageHeight = arguments!!.getFloat("height")
            imageWidth = arguments!!.getFloat("width")

            Log.d("gradient code", "got height is $imageHeight got width is $imageWidth")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.gradient_frg_layout, container, false)
    }


    private lateinit var gradientPreview : ImageView
    private fun updatePreview()
    {
        doAsync {

            val gradientImage : Bitmap = Bitmap.createBitmap(gradientPreview.width, gradientPreview.height, Bitmap.Config.ARGB_8888)

            val canvas = Canvas(gradientImage)

            val paint = Paint()
            paint.isAntiAlias = true
            paint.shader = createGradient(gradientPreview.height.toFloat(), gradientPreview.width.toFloat())

            canvas.drawPaint(paint)

            uiThread {
                gradientPreview.setImageBitmap(gradientImage)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gradientPreview = view.findViewById(R.id.gradientPreviewIV)

        updatePreview()

        val gradientSPn : Spinner = view.findViewById(R.id.gradientSPn)

        ArrayAdapter.createFromResource(this.context!!, R.array.gradients, android.R.layout.simple_spinner_item)
            .also {
                arrayAdapter ->

                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                gradientSPn.adapter = arrayAdapter
            }

        gradientSPn.onItemSelectedListener = this

        val colourIV : ImageView = view.findViewById(R.id.colourPreviewIV)

        val redSB : SeekBar = view.findViewById(R.id.redSB)
        redSB.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                red = progress
                gradientColour = Color.parseColor(hexTranslator.rgbToHexString(red, green, blue))
                colourIV.setColorFilter(gradientColour)
                updatePreview()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        val greenSB : SeekBar = view.findViewById(R.id.greenSB)
        greenSB.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                green = progress
                gradientColour = Color.parseColor(hexTranslator.rgbToHexString(red, green, blue))
                colourIV.setColorFilter(gradientColour)
                updatePreview()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        val blueSB : SeekBar = view.findViewById(R.id.blueSB)
        blueSB.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                blue = progress
                gradientColour = Color.parseColor(hexTranslator.rgbToHexString(red, green, blue))
                colourIV.setColorFilter(gradientColour)
                updatePreview()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        val applyBTN : Button = view.findViewById(R.id.applyBTN)
        applyBTN.setOnClickListener {

            if (gradientType != "") {
                val paintGradient = Paint()

                paintGradient.isAntiAlias = true

                paintGradient.shader = createGradient(imageHeight, imageWidth)

                callback.fromGradient(paintGradient)
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        gradientType = ""
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        gradientType = parent!!.getItemAtPosition(position).toString()
        updatePreview()
    }

    private val transparentColour : Int = Color.parseColor("#00ffffff")

    constructor(parcel: Parcel) : this() {
        gradientType = parcel.readString().toString()
        gradientColour = parcel.readInt()
        red = parcel.readInt()
        green = parcel.readInt()
        blue = parcel.readInt()
        imageHeight = parcel.readFloat()
        imageWidth = parcel.readFloat()
    }

    private fun createGradient(imageHeight : Float, imageWidth : Float) : Shader
    {
        val gradient : Shader =

        when(gradientType)
        {
            "Center out" -> RadialGradient(
                (imageWidth /2), (imageHeight /2), (imageWidth /2),
                gradientColour, transparentColour, Shader.TileMode.MIRROR)

            "Center in" -> RadialGradient(
                (imageWidth /2), (imageHeight /2), (imageWidth /2),
            transparentColour, gradientColour, Shader.TileMode.MIRROR)

            "Top to bottom" -> LinearGradient( 0.0f, 0.0f, 0.0f, imageHeight,
                gradientColour, transparentColour, Shader.TileMode.MIRROR)

            "Bottom to top" -> LinearGradient( 0.0f, 0.0f, 0.0f, imageHeight,
                 transparentColour, gradientColour, Shader.TileMode.MIRROR)

            "Striped" ->LinearGradient(
                (imageWidth /2), (imageWidth /2), (imageWidth /2), imageHeight,
                gradientColour, transparentColour, Shader.TileMode.MIRROR)

            else -> LinearGradient( 0.0f, 0.0f, 0.0f, imageHeight,
                gradientColour, transparentColour, Shader.TileMode.MIRROR)
        }

        return gradient
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(gradientType)
        parcel.writeInt(gradientColour)
        parcel.writeInt(red)
        parcel.writeInt(green)
        parcel.writeInt(blue)
        parcel.writeFloat(imageHeight)
        parcel.writeFloat(imageWidth)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AddGradientFRG> {
        override fun createFromParcel(parcel: Parcel): AddGradientFRG {
            return AddGradientFRG(parcel)
        }

        override fun newArray(size: Int): Array<AddGradientFRG?> {
            return arrayOfNulls(size)
        }
    }

}