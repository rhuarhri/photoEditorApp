package com.example.editorapp

import android.content.Intent
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
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

    //private lateinit var currentImage : Bitmap
    //private lateinit var currentImageCopy : Bitmap

    private lateinit var activityVM : ImagePreviewActVM



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_preview)

        activityVM = ViewModelProviders.of(this).get(ImagePreviewActVM::class.java)

        activityVM.setupUI(intent, applicationContext)



        imagePreviewIV = findViewById(R.id.imagePreviewIV)

        imagePreviewIV.setImageBitmap(activityVM.currentImage)

        pointerIV = findViewById(R.id.pointerIV)

        //layerManager = LayerManager(applicationContext)
        //layerManager.addLayer(activityVM.currentImage)

        /*
        if (photoLayer != null)
        {

            layerManager.addLayer(chosenFilter)
        }*/

        //mScaleDetector = ScaleGestureDetector(applicationContext, scaleListener)

    }

    //var mScaleFactor = 1f

    /*
    private val scaleListener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            mScaleFactor *= detector.scaleFactor

            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f))

            var testImage : Bitmap = currentImage.copy(Bitmap.Config.ARGB_8888, true)

            var testCanvas : Canvas = Canvas(testImage)
            testCanvas.scale(mScaleFactor, mScaleFactor)
            testCanvas.restore()

            imagePreviewIV.setImageBitmap(testImage)

            //invalidate()
            return true
        }
    }

     */

    private lateinit var mScaleDetector : ScaleGestureDetector

    override fun onTouchEvent(event: MotionEvent): Boolean {
        positionX = event.x.roundToInt()
        positionY = event.y.roundToInt()

        pointerIV.x = positionX.toFloat()
        positionY -= 200
        pointerIV.y = positionY.toFloat()

        //mScaleDetector.onTouchEvent(event)

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

    //private var originalHeight : Int = 0
    //private var originalWidth : Int = 0
    fun save(view : View)
    {
        val fragIn : Bundle = Bundle()
        fragIn.putInt("height", activityVM.getOriginalHeight())
        fragIn.putInt("width", activityVM.getOriginalWidth())
        fragIn.putStringArray("layers", activityVM.getLayers())

        val goTo : Intent = Intent(applicationContext, SaveImageActivity::class.java)
        goTo.putExtras(fragIn)
        startActivity(goTo)
    }

    //private lateinit var editHistory : EditHistoryManger
    fun undo(view : View)
    {
        //currentImage = editHistory.undo()!!
        imagePreviewIV.setImageBitmap(activityVM.currentImage)

    }

    private val layerFRG : layerFRG = layerFRG()
    fun layersNav(view : View)
    {

        val fragIn : Bundle = Bundle()
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
            //currentImage = layerManager.combineLayers(layerManager.getLayerList(), originalHeight, originalWidth)
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
        val fragIn : Bundle = Bundle()
        fragIn.putInt("height", activityVM.currentImage.height)
        fragIn.putInt("width", activityVM.currentImage.width)

        filterFRG.arguments = fragIn

        val openFragment : FragmentTransaction = supportFragmentManager.beginTransaction()
        openFragment.replace(R.id.fragmentLocFrL, filterFRG)
        openFragment.commit()

    }

    override fun fromFilterFRGOverlay(function : String, overlay: Bitmap?, filter : String) {

        /*
        doAsync {

            val newImage : Bitmap = addImageOverlay(activityVM.currentImage, overlay)

            uiThread {
                imagePreviewIV.setImageBitmap(newImage)
            }
        }

        val imageFilter : ImageFilters = ImageFilters(applicationContext)

        val newImage : Bitmap = imageFilter.applyFilter(activityVM.currentImage, applicationContext)

        imagePreviewIV.setImageBitmap(newImage)*/

        val imageFilter = ImageFilter(imagePreviewIV, activityVM.currentImage)
        imageFilter.filterEvent(function, overlay, filter)
        if (imageFilter.hasChanged())
        {
            Log.d("overlay image", "has changed is true")
            if (overlay != null) {
                activityVM.addLayer(overlay)
            }
            //TODO save change
        }
        else
        {
            Log.d("overlay image", "no change required")
        }


    }

    /*
    override fun applyFilterFRGOverlay(overlay: Bitmap) {

        doAsync {

            activityVM.currentImage = addImageOverlay(activityVM.currentImage, overlay)

            uiThread {
                imagePreviewIV.setImageBitmap(activityVM.currentImage)
            }
        }

        activityVM.addLayer(overlay)

    }*/

    private val colourChangeFRG : ColourChangeFRG = ColourChangeFRG()
    fun colourChange(view : View)
    {

        val openFragment : FragmentTransaction = supportFragmentManager.beginTransaction()
        openFragment.replace(R.id.fragmentLocFrL, colourChangeFRG)
        openFragment.commit()
    }

    //private var editHandler : EditEventHandler = EditEventHandler()

    override fun fromColourChangeFRG(function : String, paint: Paint) {
        /*async {

            val newImage : Bitmap = addColour(currentImage, paint)

            uiThread {

                imagePreviewIV.setImageBitmap(newImage)
            }
        }*/

        val colourChange = ImageColour(imagePreviewIV, activityVM.currentImage)
        colourChange.colourChangeEventProcessor(function, paint)
        if (colourChange.hasChanged())
        {
            //TODO save change
        }
    }

    /*
    override fun applyColourChangeFRG(paint: Paint) {

        *
        async {

            currentImage = addColour(currentImage, paint)

            uiThread {

                imagePreviewIV.setImageBitmap(currentImage)
            }
        }*

        val colourChange = ImageColour(imagePreviewIV, currentImage)
        colourChange.colourChangeEventProcessor("apply", paint)

    }*/

    private val addTextFRG : AddTextFRG = AddTextFRG()
    fun addText(view : View)
    {
        val fragIn : Bundle = Bundle()
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

            //editHistory.add(newImage)
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

        //val crop : ImageCrop = ImageCrop(imagePreviewIV, currentImage)

        /*
        async {


            val newImage : Bitmap = crop.cropSquare(cropFRG.shapeColour, currentImage, selector/*ImageSelection()*/)

            uiThread {
                imagePreviewIV.setImageBitmap(newImage)
            }
        }*/


        val crop : ImageCrop = ImageCrop(imagePreviewIV, activityVM.currentImage)
        crop.cropEventProcessor(function, cropFRG.shapeColour, selector)
        if (crop.hasChanged())
        {
            //TODO save cropped image
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

        /*

        async {


            val rotatedImage : Bitmap = rotate.rotateImage(currentImage, rotation)


            uiThread {
                imagePreviewIV.setImageBitmap(rotatedImage)
            }
        }*/


        val rotate : ImageRotate = ImageRotate(imagePreviewIV, activityVM.currentImage)
        rotate.rotateEventProcessor(function, rotation)
        if (rotate.hasChanged())
        {
            //TODO save cropped image
        }
    }

    /*
    override fun applyRotate(rotation: Float) {

        async {

            *
            val newImage : Bitmap = Bitmap.createBitmap(currentImage.width, currentImage.height, Bitmap.Config.ARGB_8888)

            val canvas : Canvas = Canvas(newImage)
            canvas.save()
            canvas.rotate(rotation, (canvas.width /2).toFloat(), (canvas.height / 2).toFloat())
            canvas.drawBitmap(currentImage, 0.0f, 0.0f, null)
            canvas.restore()

            //TODO change edit history functionality as it would be quickly filled if saves every time it rotates
            //editHistory.add(newImage)

            currentImage = newImage*

            currentImage = RotateImage(currentImage, rotation)

            uiThread {
                imagePreviewIV.setImageBitmap(currentImage)
            }
        }

    }*/

    private val setDrawingFRG : drawFRG = drawFRG()
    private val setEnableDrawFRG : drawInstructFRG = drawInstructFRG()
    private val imageDraw : ImageDraw = ImageDraw()
    fun drawOnImage(view : View)
    {

        val fragIn : Bundle = Bundle()
        fragIn.putFloat("size", imageDraw.currentSize)
        fragIn.putInt("colour", imageDraw.currentColour)

        setEnableDrawFRG.arguments = fragIn

        val openFragment : FragmentTransaction = supportFragmentManager.beginTransaction()
        openFragment.replace(R.id.fragmentLocFrL, setEnableDrawFRG)

        openFragment.commit()

    }

    override fun fromDraw(size : Float, colour : Int) {


        imageDraw.changeSettings(size, colour)

        /*

        val paint = Paint().apply {
            isAntiAlias = true
            isDither = true
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            color = colour
            strokeWidth = size
        }

        var path : Path = Path()
        var currentX = 0f
        var currentY = 0f

        path.quadTo(currentX, currentY, (positionX + currentX) / 2, (positionY + currentY) / 2)
        //currentX = motionTouchEventX
        //currentY = motionTouchEventY
        var newImage : Bitmap = Bitmap.createBitmap(activityVM.currentImage.width, activityVM.currentImage.height, Bitmap.Config.ARGB_8888)
        var canvas : Canvas = Canvas(newImage)

        canvas.drawPath(path, paint)

        var anotherImage : Bitmap = Bitmap.createBitmap(activityVM.currentImage.width, activityVM.currentImage.height, Bitmap.Config.ARGB_8888)
        canvas = Canvas(anotherImage)


        val pathTwo : Path = Path()

        pathTwo.quadTo(currentX, currentY, 0f, 150f)

        canvas.drawPath(pathTwo, paint)

        canvas.drawBitmap(newImage, 0f, 0f, null)

        var oneMoreImage : Bitmap = Bitmap.createBitmap(activityVM.currentImage.width, activityVM.currentImage.height, Bitmap.Config.ARGB_8888)
        canvas = Canvas(oneMoreImage)

        val pathThree : Path = Path()
        pathThree.quadTo(currentX, currentY, 200f, 20f)

        canvas.drawPath(pathThree, paint)

        canvas.drawBitmap(anotherImage, 0f, 0f, null)

        imagePreviewIV.setImageBitmap(oneMoreImage)*/
    }

    private val addGradientFRG : addGradientFRG = addGradientFRG()
    fun addGradient(view : View)
    {
        val fragIn : Bundle = Bundle()
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

            val canvas : Canvas = Canvas(newImage)
            canvas.drawPaint(paint)

            uiThread {
                activityVM.currentImage = newImage
                imagePreviewIV.setImageBitmap(activityVM.currentImage)
            }
        }
    }

    private val blurFRG : blurFRG = blurFRG()
    public fun blurImage(view : View)
    {
        val openFragment : FragmentTransaction = supportFragmentManager.beginTransaction()
        openFragment.replace(R.id.fragmentLocFrL, blurFRG)

        openFragment.commit()
    }

    override fun fromBlurFRG(function : String, blurAmount: Int) {

        /*
        async {
            val blurredImage : Bitmap = blurImage(applicationContext, currentImage, blurAmount)
            uiThread {
                imagePreviewIV.setImageBitmap(blurredImage)
            }
        }*/

        val blur : ImageBlur = ImageBlur(applicationContext,
            imagePreviewIV,
            activityVM.currentImage)

        blur.blurEventProcessor(function, blurAmount)
        if(blur.hasChanged())
        {
            //TODO saved rotate image
        }

    }

    /*
    override fun applyBlur(blurAmount: Int) {
        async {
            val blurredImage : Bitmap = blurImage(applicationContext, currentImage, blurAmount)
            currentImage = blurredImage
            uiThread {
                imagePreviewIV.setImageBitmap(currentImage)
            }
        }
    }*/

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
