package com.example.editorapp.fragmentCode.editFragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.editorapp.R

class RotateFRG : Fragment() {

    private var degrees : Int = 0


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
        return inflater.inflate(R.layout.rotate_image_frag_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rotateSB : SeekBar = view.findViewById(R.id.selectionSB)
        rotateSB.max = 360
        rotateSB.progress = 0

        rotateSB.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                var difference : Int = 0

                if (degrees > progress)
                {
                    difference = 0 - Math.abs((degrees - progress))
                    degrees += difference
                }
                else if (degrees < progress)
                {
                    difference = 0 + Math.abs((degrees - progress))
                    degrees += difference
                }

                callback.fromRotateFRG("preview", degrees.toFloat())

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        val applyBTN : ImageButton = view.findViewById(R.id.applyBTN)

        applyBTN.setOnClickListener {

            callback.fromRotateFRG("apply", degrees.toFloat())

        }
    }

}