package com.example.editorapp.slideShowCode


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.editorapp.R
import com.example.editorapp.imageHandling.RetrieveImageHandler

class ShowLayerAdapter(private val context : Context, private val images : Array<String>, private var showLayerListener : ShowLayerListener)
    : RecyclerView.Adapter<ShowLayerViewHolder>()
{

    public lateinit var getImage : RetrieveImageHandler

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowLayerViewHolder {
        //return super.onCreateViewHolder(parent, viewType)
        val myLayout = LayoutInflater.from(context)
        val foundView = myLayout.inflate(R.layout.layer_rv_layout, parent, false)

        getImage = RetrieveImageHandler(context)

        //val activity : Activity = context as Activity
        //val slideShowListener : SlideShowListener = activity as SlideShowListener
        return ShowLayerViewHolder(foundView)
    }

    override fun onBindViewHolder(holder: ShowLayerViewHolder, position: Int) {
        //super.onBindViewHolder(holder, position)

        holder.filterIV.maxWidth = 100
        holder.filterIV.maxHeight = 100


        //special code for displaying filters

        getImage.recyclerViewImageHandler(holder.filterIV, images[position], true)

        holder.filterIV.setOnClickListener {
            //slideShowListener.onItemClick(position)
            Log.d("on image press", "buttons should now be visible")

            holder.viewBTN.visibility = View.VISIBLE
            holder.copyBTN.visibility = View.VISIBLE
            holder.deleteBTN.visibility = View.VISIBLE

        }

        holder.copyBTN.setOnClickListener {
            showLayerListener.onCopyLayer(position)
        }

        holder.deleteBTN.setOnClickListener {
            showLayerListener.onDeleteLayer(position)
        }

        holder.viewBTN.setOnClickListener {
            showLayerListener.onViewLayer(position)
        }

    }

    override fun getItemCount(): Int {
        return images.size
    }

}