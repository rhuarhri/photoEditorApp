package com.example.editorapp.fragmentCode.editFragments

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.editorapp.R
import com.example.editorapp.imageHandling.RetrieveImageHandler
import com.example.editorapp.slideShowCode.ShowOverlaysAdapter
import com.example.editorapp.slideShowCode.SlideShowAdapter
import com.example.editorapp.slideShowCode.SlideShowListener
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.uiThread

class filterFRG : Fragment(), SlideShowListener {

    private lateinit var appContext: Context

    private lateinit var callback : FromFragment

    private val images = arrayOf((R.drawable.aim_filter).toString(), (R.drawable.film_filter).toString(),
        (R.drawable.fire_filter).toString(), (R.drawable.jail_filter).toString(),
        (R.drawable.money_filter).toString(), (R.drawable.photo_filter).toString(),
        (R.drawable.rose_filter).toString(), (R.drawable.stage_filter).toString()
    )

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

    private var height : Int = 0
    private var width : Int = 0
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (arguments != null) {
            height = arguments!!.getInt("height")
            width = arguments!!.getInt("width")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.filter_frg_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val overlayBTN : Button = view.findViewById(R.id.overlayBTN)
        val filterBTN : Button = view.findViewById(R.id.filterBTN)
        val applyBTN : ImageButton = view.findViewById(R.id.applyBTN)

        overlayBTN.setOnClickListener {

        }

        filterBTN.setOnClickListener {


        }

        applyBTN.setOnClickListener {

            val imageGetter: RetrieveImageHandler = RetrieveImageHandler(appContext)

            val chosenFilter : Bitmap = imageGetter.formatBitmapFromResources(images[imagePosition].toInt(), height, width)

            callback.applyFilterFRGOverlay(chosenFilter)
        }

        val slideShowListener = this

                val filterRV: RecyclerView = view.findViewById(R.id.filterRV)
                val rvAdapter: RecyclerView.Adapter<*> =
                    ShowOverlaysAdapter(appContext, images, slideShowListener)
                filterRV.apply {

                    setHasFixedSize(false)
                    layoutManager =
                        LinearLayoutManager(appContext, LinearLayoutManager.HORIZONTAL, false)

                    adapter = rvAdapter
                }

    }

    private var imagePosition : Int = 0
    override fun onItemClick(position: Int) {

        val imageGetter: RetrieveImageHandler = RetrieveImageHandler(appContext)

        val chosenFilter : Bitmap = imageGetter.formatBitmapFromResources(images[position].toInt(), height, width)
        imagePosition = position

        callback.fromFilterFRGOverlay(chosenFilter)

    }

}