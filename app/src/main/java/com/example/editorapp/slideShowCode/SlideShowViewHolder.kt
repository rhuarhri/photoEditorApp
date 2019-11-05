package com.example.editorapp.slideShowCode

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.editorapp.R

class SlideShowViewHolder(row : View/*, private var slideShowListener : SlideShowListener*/) : RecyclerView.ViewHolder(row) //, View.OnClickListener
{
    //private lateinit var slideShowListener : SlideShowListener

    /*
    override fun onClick(p0: View?) {
        //slideShowListener.onItemClick(adapterPosition)
        if (p0 != null) {
            Toast.makeText(p0.context, "clicked num $adapterPosition", Toast.LENGTH_LONG).show()
        }
    }*/

    var filterIV : ImageView

    init {
        this.filterIV = row.findViewById(R.id.filterIV)

    }

}