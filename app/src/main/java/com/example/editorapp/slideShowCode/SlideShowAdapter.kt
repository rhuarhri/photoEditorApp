package com.example.editorapp.slideShowCode

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.editorapp.R
import com.example.editorapp.slideShowCode.SlideShowListener
import com.example.editorapp.slideShowCode.SlideShowViewHolder

class SlideShowAdapter(private val context : Context, private val images : Array<Bitmap>, private var slideShowListener : SlideShowListener)
    : RecyclerView.Adapter<SlideShowViewHolder>()
{
    //private val imageHandler : RetrieveImageHandler = RetrieveImageHandler(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideShowViewHolder {
        val myLayout = LayoutInflater.from(context)
        val foundView = myLayout.inflate(R.layout.slide_show_layout, parent, false)

        //val activity : Activity = context as Activity
        //val slideShowListener : SlideShowListener = activity as SlideShowListener
        return SlideShowViewHolder(foundView)
    }

    override fun onBindViewHolder(holder: SlideShowViewHolder, position: Int) {

        holder.filterIV.maxWidth = 100//holder.filterIV.height
        holder.filterIV.maxHeight = 100
        //imageHandler.recyclerViewImageHandler(holder.filterIV, images[position], true)
        holder.filterIV.setImageBitmap(images[position])
        holder.filterIV.setOnClickListener {
            slideShowListener.onItemClick(position)
        }

    }

    override fun getItemCount(): Int {
        return images.size
    }
}