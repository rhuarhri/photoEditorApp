package com.example.editorapp.slideShowCode

interface ShowLayerListener {

    fun onDeleteLayer(position : Int)

    fun onCopyLayer(position: Int)

    fun onViewLayer(position: Int)
}