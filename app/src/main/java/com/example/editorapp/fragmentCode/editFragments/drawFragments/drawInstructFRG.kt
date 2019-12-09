package com.example.editorapp.fragmentCode.editFragments.drawFragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.editorapp.R
import com.example.editorapp.fragmentCode.editFragments.FromFragment

class drawInstructFRG : Fragment() {

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

    private var displayedSize : Float = 0f
    private var displayedColour : Int = 0

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (arguments != null)
        {
            displayedSize = arguments!!.getFloat("size")
            displaySizeTXT.text = "$displayedSize"

            displayedColour = arguments!!.getInt("colour")
            displayColourIV.setColorFilter(displayedColour)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.draw_enabled_frg_layout, container, false)
    }

    private lateinit var displaySizeTXT : TextView
    private lateinit var displayColourIV : ImageView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        displaySizeTXT = view.findViewById(R.id.drawSizePreviewTXT)


        displayColourIV = view.findViewById(R.id.drawColourPreviewIV)


        val settingsBTN : ImageButton = view.findViewById(R.id.drawSettingsBTN)
        settingsBTN.setOnClickListener {
            val setDrawingFRG : drawFRG = drawFRG()
            val openFragment : FragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
            openFragment.replace(R.id.fragmentLocFrL, setDrawingFRG)

            openFragment.commit()
        }
    }


}