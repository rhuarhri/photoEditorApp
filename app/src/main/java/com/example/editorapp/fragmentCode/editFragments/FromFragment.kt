package com.example.editorapp.fragmentCode.editFragments

import android.graphics.Bitmap
import android.graphics.Paint

interface FromFragment {

    fun fromColourChangeFRG(function : String, paint : Paint)

    fun fromAddText(paintTxT : Paint, X : Int, Y : Int, message : String)

    fun fromRotateFRG(function : String, rotation : Float)

    fun fromDraw(size : Float, colour : Int)

    fun saveDraw()

    fun fromGradient(paint : Paint)

    fun fromCropFRG(function : String)

    fun fromSelectFRG()

    fun fromBlurFRG(function: String, blurAmount : Int)

    fun fromFilterFRGOverlay(function: String, overlay : Bitmap?, filter : String)

    fun fromLayerFRGDelete(position: Int)

    fun fromLayerFRGCopy(position: Int)

    fun fromLayerFRGView(position: Int)

    fun fromLayerFRGBuild()

}