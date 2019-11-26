package com.example.editorapp.slideShowCode

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.editorapp.R

open class SlideShowViewHolder(row : View) : RecyclerView.ViewHolder(row)
{

    var filterIV : ImageView

    init {
        this.filterIV = row.findViewById(R.id.filterIV)

    }

}