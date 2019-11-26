package com.example.editorapp.slideShowCode

import android.content.Context

class ShowOverlaysAdapter(private val context : Context, private val images : Array<String>, private var slideShowListener : SlideShowListener)
    :  SlideShowAdapter(context, images, slideShowListener)
{
    override fun onBindViewHolder(holder: SlideShowViewHolder, position: Int) {

        holder.filterIV.maxWidth = 100
        holder.filterIV.maxHeight = 100

        getImage.recyclerViewImageHandler(holder.filterIV, images[position], false)

        holder.filterIV.setOnClickListener {
            slideShowListener.onItemClick(position)
        }

    }

}