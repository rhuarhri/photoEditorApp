package com.example.editorapp.imageEditing

import android.graphics.Bitmap
import android.graphics.Paint
import android.util.Log
import android.widget.ImageView
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.uiThread

class EditEventHandler() {

    public fun deleteLayerEvent(preview : ImageView, orignalImage : Bitmap)
    {
        layerEventProcessor("delete")
    }

    private fun layerEventProcessor(type : String)
    {

    }

    public fun colourChangeEventProcessor(function : String, paint : Paint)
    {
        async {
            when (function)
            {
                "apply" ->
                {

                }
                "preview" ->
                {

                }
                else ->
                {
                    Log.e("colour change Event Processor", "$function is not a real function")
                }
            }

        }

    }


}