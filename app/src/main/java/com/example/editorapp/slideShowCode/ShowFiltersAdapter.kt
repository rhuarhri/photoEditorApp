package com.example.editorapp.slideShowCode

import android.content.Context

class ShowFiltersAdapter(private val context : Context, private val images : Array<String>, private val areFiles : Boolean, private var slideShowListener : SlideShowListener)
    : SlideShowAdapter(context, images, slideShowListener) {

    override fun onBindViewHolder(holder: SlideShowViewHolder, position: Int) {
        //super.onBindViewHolder(holder, position)

        holder.filterIV.maxWidth = 100
        holder.filterIV.maxHeight = 100

        //special code for displaying filters
    }
}