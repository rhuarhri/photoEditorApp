package com.example.editorapp

import android.content.Intent
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import com.example.editorapp.fragmentCode.editFragments.*
import com.example.editorapp.fragmentCode.editFragments.drawFragments.drawFRG
import com.example.editorapp.fragmentCode.editFragments.drawFragments.drawInstructFRG
import com.example.editorapp.imageEditing.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import kotlin.math.roundToInt

class ImagePreviewActivity : AppCompatActivity(), FromFragment {

    private lateinit var imagePreviewIV : ImageView
    private lateinit var pointerIV : ImageView
    private var positionY : Int = 0
    private var positionX : Int = 0

    private lateinit var activityVM : ImagePreviewActVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_preview)

        activityVM = ViewModelProviders.of(this).get(ImagePreviewActVM::class.java)

        activityVM.setupUI(intent, applicationContext)

        imagePreviewIV = findViewById(R.id.imagePreviewIV)

        imagePreviewIV.setImageBitmap(activityVM.currentImage)

        pointerIV = findViewById(R.id.pointerIV)

    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        positionX = event.x.roundToInt()
        positionY = event.y.roundToInt()

        pointerIV.x = positionX.toFloat()
        positionY -= 200
        pointerIV.y = positionY.toFloat()

        if (selector.selectionInProgress()) {
            selector.setHeightAndWidth((positionX), (positionY + 200))
            displaySelection()
        }

        if (setEnableDrawFRG.isVisible) { //only draw when fragment informs the user that they can draw
            imageDraw.touchEventHandler(event, positionX, positionY, activityVM.currentImage)
            val newImage : Bitmap? = imageDraw.getDrawOnImage()
            if (newImage != null) {
                imagePreviewIV.setImageBitmap(imageDraw.getDrawOnImage())
            }
        }

        return true
    }


    fun save(view : View)
    {
        val fragIn = Bundle()
        fragIn.putInt("height", activityVM.getOriginalHeight())
        fragIn.putInt("width", activityVM.getOriginalWidth())
        fragIn.putStringArray("layers", activityVM.getLayers())

        val goTo = Intent(applicationContext, SaveImageActivity::class.java)
        goTo.putExtras(fragIn)
        startActivity(goTo)
    }

    fun undo(view : View)
    {
        activityVM.undo()
        imagePreviewIV.setImageBitmap(activityVM.currentImage)

    }

    private val layerFRG : layerFRG = layerFRG()
    fun layersNav(view : View)
    {

        val fragIn = Bundle()
        Log.d("layers", "layer amount is ${activityVM.getLayers().size}")
        fragIn.putStringArray("layers", activityVM.getLayers())

        layerFRG.arguments = fragIn

        val openFragment : FragmentTransaction = supportFragmentManager.beginTransaction()
        openFragment.replace(R.id.layerFRGLocationFrl, layerFRG)
        openFragment.commit()

    }

    private fun hideLayerFRGAfterAction()
    {
        val closeFragment : FragmentTransaction = supportFragmentManager.beginTransaction()
        closeFragment.remove(layerFRG)
        closeFragment.commit()
    }

    override fun fromLayerFRGDelete(position: Int) {
        doAsync {
            activityVM.removeLayer(position)
        }
        Log.d("Layer deleted", "layer deleted at position $position")
        hideLayerFRGAfterAction()
    }

    override fun fromLayerFRGCopy(position: Int) {
        activityVM.copyLayer(position)
        Log.d("Layer copied", "layer copied at position $position")
        hideLayerFRGAfterAction()
    }

    override fun fromLayerFRGView(position: Int) {
        doAsync{
            val image : Bitmap = activityVM.viewLayer(position)
            uiThread {
                imagePreviewIV.setImageBitmap(image)
            }
        }
        hideLayerFRGAfterAction()
    }

    override fun fromLayerFRGBuild() {
        doAsync{
            val image : Bitmap = activityVM.combineLayer()
            uiThread {
                imagePreviewIV.setImageBitmap(image)
            }
        }
    }

    private val selector : ImageSelection = ImageSelection()
    private val selectFRG : selectFRG = selectFRG()
    fun selectImage(view : View)
    {
        selector.setPosition(positionX, positionY)

        val openFragment : FragmentTransaction = supportFragmentManager.beginTransaction()
        openFragment.replace(R.id.fragmentLocFrL, selectFRG)
        openFragment.commit()
    }

    override fun fromSelectFRG() {
        selector.stopSelecting()
    }

    private fun displaySelection()
    {
        doAsync {

            val newImage : Bitmap = selector.displayImage(activityVM.currentImage)

            uiThread {
                imagePreviewIV.setImageBitmap(newImage)
            }
        }
    }

    private val filterFRG : filterFRG = filterFRG()
    fun addFilter(view : View)
    {
        val fragIn = Bundle()
        fragIn.putInt("height", activityVM.currentImage.height)
        fragIn.putInt("width", activityVM.currentImage.width)

        filterFRG.arguments = fragIn

        val openFragment : FragmentTransaction = supportFragmentManager.beginTransaction()
        openFragment.replace(R.id.fragmentLocFrL, filterFRG)
        openFragment.commit()

    }

    override fun fromFilterFRGOverlay(function : String, overlay: Bitmap?, filter : String) {

        doAsync {
            val imageFilter = ImageFilter(imagePreviewIV, activityVM.currentImage)
            imageFilter.filterEvent(function, overlay, filter)
            uiThread {
                if (imageFilter.hasChanged()) {
                    Log.d("overlay image", "has changed is true")
                    if (overlay != null) {
                        activityVM.addLayer(overlay)
                    }
                    //TODO save change
                } else {
                    Log.d("overlay image", "no change required")
                }
            }
        }


    }

    private val colourChangeFRG : ColourChangeFRG = ColourChangeFRG()
    fun colourChange(view : View)
    {

        val openFragment : FragmentTransaction = supportFragmentManager.beginTransaction()
        openFragment.replace(R.id.fragmentLocFrL, colourChangeFRG)
        openFragment.commit()
    }

    override fun fromColourChangeFRG(function : String, paint: Paint) {

        doAsync {
            val colourChange = ImageColour(imagePreviewIV, activityVM.currentImage)
            colourChange.colourChangeEventProcessor(function, paint)

            uiThread {
                if (colourChange.hasChanged()) {
                    activityVM.currentImage = colourChange.getChangedImage()
                    activityVM.addToHistory(activityVM.currentImage)
                    Log.d("colour change", "image changed")
                } else {
                    Log.d("colour change", "no change required")
                }
            }
        }

    }

    private val addTextFRG : AddTextFRG = AddTextFRG()
    fun addText(view : View)
    {
        val fragIn = Bundle()
        fragIn.putInt("posY", positionY)
        fragIn.putInt("posX", positionX)

        addTextFRG.arguments = fragIn

        val openFragment : FragmentTransaction = supportFragmentManager.beginTransaction()
        openFragment.replace(R.id.fragmentLocFrL, addTextFRG)

        openFragment.commit()
    }

    override fun fromAddText(paintTxT: Paint, X : Int, Y : Int, message : String) {

        doAsync {

            val newImage : Bitmap = addText(activityVM.currentImage, message, X, Y, paintTxT)

            activityVM.addToHistory(newImage)

            activityVM.currentImage = newImage

            uiThread {
                imagePreviewIV.setImageBitmap(activityVM.currentImage)
            }
        }
    }

    private val cropFRG : cropFRG = cropFRG()
    fun cropImage(view : View)
    {
        val openFragment : FragmentTransaction = supportFragmentManager.beginTransaction()
        openFragment.replace(R.id.fragmentLocFrL, cropFRG)

        openFragment.commit()
    }

    override fun fromCropFRG(function : String) {

        doAsync {
            val crop = ImageCrop(imagePreviewIV, activityVM.currentImage)
            crop.cropEventProcessor(function, cropFRG.shapeColour, selector)
            uiThread {
                if (crop.hasChanged()) {
                    //TODO save cropped image
                    activityVM.currentImage = crop.getChangedImage()
                    activityVM.addToHistory(activityVM.currentImage)
                    Log.d("crop change", "image changed")
                } else {
                    Log.d("crop image", "no change required")
                }
            }
        }

    }

    private val rotateImageFRG : rotateFRG = rotateFRG()
    fun rotateImage(view : View)
    {
        val openFragment : FragmentTransaction = supportFragmentManager.beginTransaction()
        openFragment.replace(R.id.fragmentLocFrL, rotateImageFRG)

        openFragment.commit()
    }

    override fun fromRotateFRG(function : String, rotation: Float) {

        doAsync {
            val rotate = ImageRotate(imagePreviewIV, activityVM.currentImage)
            rotate.rotateEventProcessor(function, rotation)

            uiThread {
                if (rotate.hasChanged()) {
                    //TODO save cropped image
                    activityVM.currentImage = rotate.getChangedImage()
                    activityVM.addToHistory(activityVM.currentImage)
                    Log.d("rotate change", "image changed")
                }
                else {
                    Log.d("rotate change", "no change required")
                }
            }
        }

    }

    private val setDrawingFRG : drawFRG = drawFRG()
    private val setEnableDrawFRG : drawInstructFRG = drawInstructFRG()
    private val imageDraw : ImageDraw = ImageDraw()
    fun drawOnImage(view : View)
    {

        val fragIn = Bundle()
        fragIn.putFloat("size", imageDraw.currentSize)
        fragIn.putInt("colour", imageDraw.currentColour)

        setEnableDrawFRG.arguments = fragIn

        val openFragment : FragmentTransaction = supportFragmentManager.beginTransaction()
        openFragment.replace(R.id.fragmentLocFrL, setEnableDrawFRG)

        openFragment.commit()

    }

    override fun fromDraw(size : Float, colour : Int) {
        imageDraw.changeSettings(size, colour)
    }

    override fun saveDraw() {
        activityVM.currentImage = imageDraw.getDrawOnImage()!!
        imagePreviewIV.setImageBitmap(activityVM.currentImage)
        activityVM.addToHistory(activityVM.currentImage)
    }

    private val addGradientFRG : addGradientFRG = addGradientFRG()
    fun addGradient(view : View)
    {
        val fragIn = Bundle()
        fragIn.putFloat("height", activityVM.currentImage.height.toFloat())
        fragIn.putFloat("width", activityVM.currentImage.width.toFloat())

        addGradientFRG.arguments = fragIn

        Log.d("gradient code", "input height is ${activityVM.currentImage.height} input width is ${activityVM.currentImage.width}")

        val openFragment : FragmentTransaction = supportFragmentManager.beginTransaction()
        openFragment.replace(R.id.fragmentLocFrL, addGradientFRG)

        openFragment.commit()
    }

    override fun fromGradient(paint: Paint) {
        doAsync {

            val newImage : Bitmap = activityVM.currentImage.copy(Bitmap.Config.ARGB_8888, true)

            val canvas = Canvas(newImage)
            canvas.drawPaint(paint)

            uiThread {
                activityVM.currentImage = newImage
                imagePreviewIV.setImageBitmap(activityVM.currentImage)
            }
        }
    }

    private val blurFRG : blurFRG = blurFRG()
    fun blurImage(view : View)
    {
        val openFragment : FragmentTransaction = supportFragmentManager.beginTransaction()
        openFragment.replace(R.id.fragmentLocFrL, blurFRG)

        openFragment.commit()
    }

    override fun fromBlurFRG(function : String, blurAmount: Int) {

        val blur = ImageBlur(applicationContext,
            imagePreviewIV,
            activityVM.currentImage)

        doAsync {

            blur.blurEventProcessor(function, blurAmount)
            uiThread {
                if (blur.hasChanged()) {
                    //TODO saved rotate image
                    activityVM.currentImage = blur.getChangedImage()
                    activityVM.addToHistory(activityVM.currentImage)
                    Log.d("blur change", "image changed")
                }
                else {
                    Log.d("blur change", "no change required")
                }
            }
        }

    }

    fun hideCurrentFragment(view : View)
    {
        val closeFragment : FragmentTransaction = supportFragmentManager.beginTransaction()

        if (layerFRG.isVisible)
        {
            closeFragment.remove(layerFRG)
        }
        if (selectFRG.isVisible)
        {
            selector.stopSelecting()
            closeFragment.remove(selectFRG)
        }
        if(filterFRG.isVisible)
        {
            closeFragment.remove(filterFRG)
        }
        if (addTextFRG.isVisible)
        {
            closeFragment.remove(addTextFRG)
        }
        if (cropFRG.isVisible)
        {
            closeFragment.remove(cropFRG)
        }
        if (colourChangeFRG.isVisible)
        {
            closeFragment.remove(colourChangeFRG)
        }
        if (rotateImageFRG.isVisible)
        {
            closeFragment.remove(rotateImageFRG)
        }
        if (setDrawingFRG.isVisible)
        {
            closeFragment.remove(setDrawingFRG)
        }
        if (setEnableDrawFRG.isVisible)
        {
            closeFragment.remove(setEnableDrawFRG)
        }
        if (addGradientFRG.isVisible)
        {
            closeFragment.remove(addGradientFRG)
        }
        if(blurFRG.isVisible)
        {
            closeFragment.remove(blurFRG)
        }
        closeFragment.commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        activityVM.recordLocation()
    }
}