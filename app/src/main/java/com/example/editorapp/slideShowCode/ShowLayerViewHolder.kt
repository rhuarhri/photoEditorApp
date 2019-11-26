package com.example.editorapp.slideShowCode

import android.view.View
import android.widget.ImageButton
import com.example.editorapp.R
import kotlinx.android.synthetic.main.layer_rv_layout.view.*

class ShowLayerViewHolder(row : View) : SlideShowViewHolder(row) {

    var viewBTN : ImageButton
    var copyBTN : ImageButton
    var deleteBTN : ImageButton

    init {
        this.viewBTN = row.findViewById(R.id.viewBTN)
        this.copyBTN = row.findViewById(R.id.copyBTN)
        this.deleteBTN = row.findViewById(R.id.deleteBTN)
    }
}