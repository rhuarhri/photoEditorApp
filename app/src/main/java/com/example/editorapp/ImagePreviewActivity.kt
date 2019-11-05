package com.example.editorapp

import android.content.Intent
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.FragmentTransaction
import com.example.editorapp.fragmentCode.editFragments.*
import com.example.editorapp.imageEditing.*
import com.example.editorapp.imageHandling.EditHistoryManger
import com.example.editorapp.imageHandling.RetrieveImageHandler
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.uiThread
import kotlin.math.roundToInt

class ImagePreviewActivity : AppCompatActivity(), FromFragment {


    private lateinit var imagePreviewIV : ImageView
    private lateinit var pointerIV : ImageView
    private var positionY : Int = 0
    private var positionX : Int = 0

    private lateinit var currentImage : Bitmap
    //private lateinit var currentImageCopy : Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_preview)

        val photoLoc = intent.getStringExtra("photo")
        val photoH = intent.getIntExtra("height", 0)
        val photoW = intent.getIntExtra("width", 0)

        //val photoUri : Uri = photoLoc?.toUri()!!


        imagePreviewIV = findViewById(R.id.imagePreviewIV)

        val getImage : RetrieveImageHandler = RetrieveImageHandler(applicationContext)

        currentImage = getImage.getBitmapFromFile(photoLoc)//MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri)

        editHistory = EditHistoryManger(applicationContext, photoH, photoW)

        async {
            editHistory.add(currentImage)
        }

        imagePreviewIV.setImageBitmap(currentImage)

        pointerIV = findViewById(R.id.pointerIV)

        //mScaleDetector = ScaleGestureDetector(applicationContext, scaleListener)

    }

    //private var mScaleFactor = 1f

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

        return true
    }


    fun save(view : View)
    {
        val goTo : Intent = Intent(applicationContext, SaveImageActivity::class.java)
        startActivity(goTo)
    }

    private lateinit var editHistory : EditHistoryManger
    fun undo(view : View)
    {
        currentImage = editHistory.undo()!!
        imagePreviewIV.setImageBitmap(currentImage)

    }

    private val layerFRG : layerFRG = layerFRG()
    fun layersNav(view : View)
    {
        val openFragment : FragmentTransaction = supportFragmentManager.beginTransaction()
        openFragment.replace(R.id.layerFRGLocationFrl, layerFRG)
        openFragment.commit()
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
        async {

            val newImage : Bitmap = selector.displayImage(currentImage)

            uiThread {
                imagePreviewIV.setImageBitmap(newImage)
            }
        }
    }

    private val filterFRG : filterFRG = filterFRG()
    fun addFilter(view : View)
    {
        /*
        var imageFilter : ImageFilters = ImageFilters(applicationContext)

        currentImage = imageFilter.applyFilter(currentImage, applicationContext)

        imagePreviewIV.setImageBitmap(currentImage)*/

        val openFragment : FragmentTransaction = supportFragmentManager.beginTransaction()
        openFragment.replace(R.id.fragmentLocFrL, filterFRG)
        openFragment.commit()

    }

    private val colourChangeFRG : ColourChangeFRG = ColourChangeFRG()
    fun colourChange(view : View)
    {

        val openFragment : FragmentTransaction = supportFragmentManager.beginTransaction()
        openFragment.replace(R.id.fragmentLocFrL, colourChangeFRG)
        openFragment.commit()
    }

    override fun fromColourChangeFRG(paint: Paint) {
        async {

            val newImage : Bitmap = addColour(currentImage, paint)

            uiThread {

                imagePreviewIV.setImageBitmap(newImage)
            }
        }
    }

    override fun applyColourChangeFRG(paint: Paint) {

        async {


            currentImage = addColour(currentImage, paint)

            uiThread {

                imagePreviewIV.setImageBitmap(currentImage)
            }
        }

    }

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

        async {

            val newImage : Bitmap = addText(currentImage, message, X, Y, paintTxT)

            editHistory.add(newImage)

            currentImage = newImage

            uiThread {
                imagePreviewIV.setImageBitmap(currentImage)
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

    override fun fromCropFRGSquare() {

        async {


            val newImage : Bitmap = cropSquare(cropFRG.shapeColour, currentImage, ImageSelection())

            uiThread {
                imagePreviewIV.setImageBitmap(newImage)
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

    override fun fromRotate(rotation: Float) {

        async {


            val rotatedImage : Bitmap = RotateImage(currentImage, rotation)


            uiThread {
                imagePreviewIV.setImageBitmap(rotatedImage)
            }
        }
    }

    override fun applyRotate(rotation: Float) {

        async {

            /*
            val newImage : Bitmap = Bitmap.createBitmap(currentImage.width, currentImage.height, Bitmap.Config.ARGB_8888)

            val canvas : Canvas = Canvas(newImage)
            canvas.save()
            canvas.rotate(rotation, (canvas.width /2).toFloat(), (canvas.height / 2).toFloat())
            canvas.drawBitmap(currentImage, 0.0f, 0.0f, null)
            canvas.restore()

            //TODO change edit history functionality as it would be quickly filled if saves every time it rotates
            //editHistory.add(newImage)

            currentImage = newImage*/

            currentImage = RotateImage(currentImage, rotation)

            uiThread {
                imagePreviewIV.setImageBitmap(currentImage)
            }
        }

    }

    private val addGradientFRG : addGradientFRG = addGradientFRG()
    fun addGradient(view : View)
    {
        val fragIn : Bundle = Bundle()
        fragIn.putFloat("height", currentImage.height.toFloat())
        fragIn.putFloat("width", currentImage.width.toFloat())

        addGradientFRG.arguments = fragIn

        Log.d("gradient code", "input height is ${currentImage.height} input width is ${currentImage.width}")

        val openFragment : FragmentTransaction = supportFragmentManager.beginTransaction()
        openFragment.replace(R.id.fragmentLocFrL, addGradientFRG)

        openFragment.commit()
    }

    override fun fromGradient(paint: Paint) {
        async {

            val newImage : Bitmap = currentImage.copy(Bitmap.Config.ARGB_8888, true)

            val canvas : Canvas = Canvas(newImage)
            canvas.drawPaint(paint)

            uiThread {
                currentImage = newImage
                imagePreviewIV.setImageBitmap(currentImage)
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

    override fun fromBlur(blurAmount: Int) {

        async {
            val blurredImage : Bitmap = blurImage(applicationContext, currentImage, blurAmount)
            uiThread {
                imagePreviewIV.setImageBitmap(blurredImage)
            }
        }

    }

    override fun applyBlur(blurAmount: Int) {
        async {
            val blurredImage : Bitmap = blurImage(applicationContext, currentImage, blurAmount)
            currentImage = blurredImage
            uiThread {
                imagePreviewIV.setImageBitmap(currentImage)
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
        editHistory.recordLocation()
    }
}
