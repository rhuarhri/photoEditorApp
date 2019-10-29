package com.example.editorapp.fragmentCode

import android.graphics.Paint

interface FromFragment {

    public fun fromColourChangeFRG(paint : Paint)

    public fun applyColourChangeFRG(paint : Paint)

    public fun fromAddText(paintTxT : Paint, X : Int, Y : Int, message : String)

    public fun fromRotate(rotation : Float)

    public fun applyRotate(rotation: Float)

    public fun fromGradient(paint : Paint)
}