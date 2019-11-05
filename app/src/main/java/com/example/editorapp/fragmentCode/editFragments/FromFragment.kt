package com.example.editorapp.fragmentCode.editFragments

import android.graphics.Paint

interface FromFragment {

    public fun fromColourChangeFRG(paint : Paint)

    public fun applyColourChangeFRG(paint : Paint)

    public fun fromAddText(paintTxT : Paint, X : Int, Y : Int, message : String)

    public fun fromRotate(rotation : Float)

    public fun applyRotate(rotation: Float)

    public fun fromGradient(paint : Paint)

    public fun fromCropFRGSquare()

    public fun fromSelectFRG()

    public fun fromBlur(blurAmount : Int)

    public fun applyBlur(blurAmount: Int)

}