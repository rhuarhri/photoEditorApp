package com.example.editorapp.fragmentCode.editFragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.editorapp.R
import com.example.editorapp.imageHandling.RetrieveImageHandler
import com.example.editorapp.slideShowCode.SlideShowAdapter
import com.example.editorapp.slideShowCode.SlideShowListener
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.uiThread

class layerFRG : Fragment(), SlideShowListener {

    private lateinit var appContext: Context

    private lateinit var callback : FromFragment

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.layer_frg_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val slideShowListener = this

        async {
            var imageGetter: RetrieveImageHandler = RetrieveImageHandler(appContext)

            var images = arrayOf(
                imageGetter.formatBitmapFromResources(R.drawable.no_image_icon, 100, 100),
                imageGetter.formatBitmapFromResources(R.drawable.no_image_icon, 100, 100),
                imageGetter.formatBitmapFromResources(R.drawable.no_image_icon, 100, 100),
                imageGetter.formatBitmapFromResources(R.drawable.no_image_icon, 100, 100),
                imageGetter.formatBitmapFromResources(R.drawable.no_image_icon, 100, 100),
                imageGetter.formatBitmapFromResources(R.drawable.no_image_icon, 100, 100),
                imageGetter.formatBitmapFromResources(R.drawable.no_image_icon, 100, 100),
                imageGetter.formatBitmapFromResources(R.drawable.no_image_icon, 100, 100)
            )

            uiThread {
                var overlayRV: RecyclerView = view.findViewById(R.id.layersRV)
                val rvAdapter: RecyclerView.Adapter<*> =
                    SlideShowAdapter(appContext, images, slideShowListener)
                overlayRV.apply {

                    setHasFixedSize(false)
                    layoutManager =
                        LinearLayoutManager(appContext, LinearLayoutManager.VERTICAL, false)

                    adapter = rvAdapter
                }
            }
            }

    }


    override fun onItemClick(position: Int) {

    }


}