package com.example.editorapp.slideShowCode

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.editorapp.R
import com.example.editorapp.imageHandling.RetrieveImageHandler

open class SlideShowAdapter(private val context : Context, private val images : Array<String>, private var slideShowListener : SlideShowListener)
    : RecyclerView.Adapter<SlideShowViewHolder>()
{
    open val getImage : RetrieveImageHandler = RetrieveImageHandler(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideShowViewHolder {
        val myLayout = LayoutInflater.from(context)
        val foundView = myLayout.inflate(R.layout.slide_show_layout, parent, false)

        return SlideShowViewHolder(foundView)
    }

    override fun onBindViewHolder(holder: SlideShowViewHolder, position: Int) {

        holder.filterIV.maxWidth = 100
        holder.filterIV.maxHeight = 100

        getImage.recyclerViewImageHandler(holder.filterIV, images[position], true)

        holder.filterIV.setOnClickListener {
            slideShowListener.onItemClick(position)
        }

    }

    override fun getItemCount(): Int {
        return images.size
    }
}