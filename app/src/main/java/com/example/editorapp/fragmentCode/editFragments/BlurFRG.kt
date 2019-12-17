package com.example.editorapp.fragmentCode.editFragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.editorapp.R

class BlurFRG: Fragment() {

    private var blurAmount : Int = 0

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
        val blurSB : SeekBar = view.findViewById(R.id.selectionSB)
        blurSB.max = 25
        blurSB.progress = 0

        blurSB.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                blurAmount = progress
                callback.fromBlurFRG("preview", blurAmount)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })

        val applyBTN : ImageButton = view.findViewById(R.id.applyBTN)

        applyBTN.setOnClickListener {
            callback.fromBlurFRG("apply", blurAmount)
        }
    }
}