package com.example.editorapp

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.editorapp.fragmentCode.*
import com.example.editorapp.imageEditing.ImageFilters
import com.example.editorapp.imageHandling.EditHistoryManger
import com.example.editorapp.imageHandling.RetrieveImageHandler
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.uiThread
import java.net.URI
import kotlin.math.roundToInt

class ImagePreviewActivity : AppCompatActivity(), FromFragment {


    private lateinit var imagePreviewIV : ImageView
    private lateinit var pointerIV : ImageView
    private var positionY : Int = 0
    private var positionX : Int = 0

    private lateinit var undoBTN : Button
    private lateinit var saveBTN : Button

    private lateinit var currentImage : Bitmap
    private lateinit var currentImageCopy : Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_preview)

        val photoLoc = intent.getStringExtra("photo")
        val photoH = intent.getIntExtra("height", 0)
        val photoW = intent.getIntExtra("width", 0)

        //val photoUri : Uri = photoLoc?.toUri()!!


        imagePreviewIV = findViewById(R.id.imagePreviewIV)

        val getImage : RetrieveImageHandler = RetrieveImageHandler(applicationContext)

        currentImage = getImage.getBitmapFromFile(photoLoc, photoH, photoW)//MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri)

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

        return true
    }



    public fun save(view : View)
    {

    }

    private lateinit var editHistory : EditHistoryManger
    public fun undo(view : View)
    {
        currentImage = editHistory.undo()!!
        imagePreviewIV.setImageBitmap(currentImage)

    }

    public fun addFilter(view : View)
    {
        var imageFilter : ImageFilters = ImageFilters(applicationContext)

        currentImage = imageFilter.applyFilter(currentImage)

        imagePreviewIV.setImageBitmap(currentImage)

    }

    private val colourChangeFRG : ColourChangeFRG = ColourChangeFRG()
    public fun colourChange(view : View)
    {
        currentImageCopy = currentImage.copy(Bitmap.Config.ARGB_8888, true)

        val openFragment : FragmentTransaction = supportFragmentManager.beginTransaction()
        openFragment.replace(R.id.fragmentLocFrL, colourChangeFRG)
        openFragment.commit()
    }

    override fun fromColourChangeFRG(paint: Paint) {
        async {

            val newImage : Bitmap = Bitmap.createBitmap(currentImage.width, currentImage.height, Bitmap.Config.ARGB_8888)

            val canvas : Canvas = Canvas(newImage)
            canvas.drawBitmap(currentImage, 0.0f, 0.0f, paint)

            uiThread {
                currentImageCopy = newImage
                imagePreviewIV.setImageBitmap(currentImageCopy)
            }
        }
    }

    override fun applyColourChangeFRG(paint: Paint) {

        async {

            val newImage : Bitmap = Bitmap.createBitmap(currentImage.width, currentImage.height, Bitmap.Config.ARGB_8888)

            val canvas : Canvas = Canvas(newImage)
            canvas.drawBitmap(currentImage, 0.0f, 0.0f, paint)

            uiThread {
                currentImage = newImage
                imagePreviewIV.setImageBitmap(currentImage)
            }
        }

    }

    private val addTextFRG : AddTextFRG = AddTextFRG()
    public fun addText(view : View)
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

            val newImage : Bitmap = Bitmap.createBitmap(currentImage.width, currentImage.height, Bitmap.Config.ARGB_8888)

            val canvas : Canvas = Canvas(newImage)
            canvas.drawBitmap(currentImage, 0.0f, 0.0f, null)
            canvas.drawText(message, X.toFloat(), Y.toFloat(), paintTxT)

            editHistory.add(newImage)

            currentImage = newImage

            uiThread {
                imagePreviewIV.setImageBitmap(currentImage)
            }
        }
    }

    private val rotateImageFRG : rotateFRG = rotateFRG()
    public fun rotateImage(view : View)
    {

        currentImageCopy = currentImage.copy(Bitmap.Config.ARGB_8888, true)
        val openFragment : FragmentTransaction = supportFragmentManager.beginTransaction()
        openFragment.replace(R.id.fragmentLocFrL, rotateImageFRG)

        openFragment.commit()
    }

    override fun fromRotate(rotation: Float) {

        async {


            //parts of the image will always be lost this is because android cannot make a bitmap that big enough

            val newImage : Bitmap = Bitmap.createBitmap(currentImage.width, currentImage.height, Bitmap.Config.ARGB_8888)

            val canvas : Canvas = Canvas(newImage)
            canvas.save()
            canvas.rotate(rotation, (canvas.width /2).toFloat(), (canvas.height / 2).toFloat())
            canvas.drawBitmap(currentImageCopy, 0.0f, 0.0f, null)
            canvas.restore()

            currentImageCopy = newImage

            uiThread {
                imagePreviewIV.setImageBitmap(currentImageCopy)
            }
        }
    }

    override fun applyRotate(rotation: Float) {

        async {

            val newImage : Bitmap = Bitmap.createBitmap(currentImage.width, currentImage.height, Bitmap.Config.ARGB_8888)

            val canvas : Canvas = Canvas(newImage)
            canvas.save()
            canvas.rotate(rotation, (canvas.width /2).toFloat(), (canvas.height / 2).toFloat())
            canvas.drawBitmap(currentImage, 0.0f, 0.0f, null)
            canvas.restore()

            //TODO change edit history functionality as it would be quickly filled if saves every time it rotates
            //editHistory.add(newImage)

            currentImage = newImage

            uiThread {
                imagePreviewIV.setImageBitmap(currentImage)
            }
        }

    }

    private val addGradientFRG : addGradientFRG = addGradientFRG()
    public fun addGradient(view : View)
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

    public fun hideCurrentFragment(view : View)
    {
        val closeFragment : FragmentTransaction = supportFragmentManager.beginTransaction()
        if (addTextFRG.isVisible)
        {
            closeFragment.remove(addTextFRG)
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
        closeFragment.commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        editHistory.recordLocation()
    }
}
