package com.example.editorapp.fragmentCode.editFragments

import android.graphics.Bitmap
import android.graphics.Paint
import java.text.FieldPosition

interface FromFragment {

    //event processor
    public fun fromColourChangeFRG(function : String, paint : Paint)

    //public fun applyColourChangeFRG(paint : Paint)

    //event processor
    public fun fromAddText(paintTxT : Paint, X : Int, Y : Int, message : String)

    //event processor
    public fun fromRotateFRG(function : String, rotation : Float)

    //public fun applyRotate(rotation: Float)

    public fun fromDraw(size : Float, colour : Int)

    //event processor
    public fun fromGradient(paint : Paint)

    //event processor
    public fun fromCropFRG(function : String)

    //event processor
    public fun fromSelectFRG()

    //event processor
    public fun fromBlurFRG(function: String, blurAmount : Int)

    //public fun applyBlur(blurAmount: Int)


    //event processor
    public fun fromFilterFRGOverlay(function: String, overlay : Bitmap?, filter : String)

    //public fun fromFilterFRGFilter()

    //public fun applyFilterFRGFilter(filter: Bitmap)

    //public fun applyFilterFRGOverlay(overlay: Bitmap)

    public fun fromLayerFRGDelete(position: Int)

    public fun fromLayerFRGCopy(position: Int)

    public fun fromLayerFRGView(position: Int)

    public fun fromLayerFRGBuild()


}