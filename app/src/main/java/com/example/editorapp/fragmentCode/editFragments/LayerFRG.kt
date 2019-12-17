package com.example.editorapp.fragmentCode.editFragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.editorapp.R
import com.example.editorapp.slideShowCode.ShowLayerAdapter
import com.example.editorapp.slideShowCode.ShowLayerListener


class LayerFRG : Fragment(), ShowLayerListener {

    private lateinit var appContext: Context

    private lateinit var callback : FromFragment

    private lateinit var layersIconsLocations : Array<String>

    private lateinit var overlayRV : RecyclerView

    override fun onAttach(context: Context) {
        super.onAttach(context)

        appContext = context

        val activity : Activity = context as Activity
        try {
            callback = activity as FromFragment
        }
        catch(e : Exception)
        {
            Toast.makeText(context, "error is ${e.toString()}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (arguments != null)
        {

            layersIconsLocations = arguments!!.getStringArray("layers")!!

            Log.d("layers", "layer is ${layersIconsLocations.size}")

            val showLayerListener = this

            val rvAdapter: RecyclerView.Adapter<*> =
                ShowLayerAdapter(appContext, layersIconsLocations, showLayerListener)
            overlayRV.apply {

                setHasFixedSize(false)
                layoutManager =
                    LinearLayoutManager(appContext, LinearLayoutManager.VERTICAL, false)

                adapter = rvAdapter


            }

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.layer_frg_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

                overlayRV = view.findViewById(R.id.layersRV)

                val buildBTN : ImageButton = view.findViewById(R.id.buildBTN)

                buildBTN.setOnClickListener {
                    callback.fromLayerFRGBuild()
                }

    }

    override fun onDeleteLayer(position: Int) {
        callback.fromLayerFRGDelete(position)
    }

    override fun onCopyLayer(position: Int) {
        callback.fromLayerFRGCopy(position)
    }

    override fun onViewLayer(position: Int) {
        callback.fromLayerFRGView(position)
    }


}