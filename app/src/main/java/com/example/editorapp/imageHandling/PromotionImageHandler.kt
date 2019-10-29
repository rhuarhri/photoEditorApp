package com.example.editorapp.imageHandling

/*
import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.nn4wchallenge.database.internal.DatabaseCommands
import com.example.nn4wchallenge.database.internal.DatabaseManager
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class PromotionImageHandler(context : Context) {

    private val commands : DatabaseCommands = DatabaseCommands()
    private val imageHandler : RetrieveImageHandler = RetrieveImageHandler(context)

    //This will be used to display an image on the welcome screen
    //in order to inform the user of available offers

    //these URLs come from the river island website
    //these URLS would come from a json file but are here for testing and demonstration reasons

    private val menPromotionImageURL = "https://images.riverisland.com/is/image/RiverIsland/c20190626-mega-menu-mw-promo_DNT?scl=1"
    private val womenPromotionImageURL = "https://images.riverisland.com/is/image/RiverIsland/c20190626-mega-menu-ww-promo_DNT?SCL=1"
    private val girlPromotionImageURL = "https://images.riverisland.com/is/image/RiverIsland/c20190621_megamenu_summer_kw_DNT_gw_?scl=1"
    private val boyPromotionImageURL = "https://images.riverisland.com/is/image/RiverIsland/c20190621_megamenu_summer_kw_DNT_bw?scl=1"

    fun setPromotionImage(owner : LifecycleOwner, promotionIV : ImageView)
    {

        val inputData : Data = Data.Builder()
            .putString(commands.User_DB, commands.User_DB)
            .putString(commands.User_Get, commands.User_Get)
            .build()

        val getUserInfoWorker = OneTimeWorkRequestBuilder<DatabaseManager>().setInputData(inputData).build()

        WorkManager.getInstance().enqueue(getUserInfoWorker)

        WorkManager.getInstance().getWorkInfoByIdLiveData(getUserInfoWorker.id).observe(owner, Observer {

            workInfo ->

            if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED)
            {

                val foundGender : String? = workInfo.outputData.getString(commands.User_gender)
                val foundAge : String? = workInfo.outputData.getString(commands.User_age)

                if (foundGender == null || foundAge == null)
                {
                    //do nothing
                }
                else
                {
                    if (foundGender == "female")
                    {
                        if (foundAge == "adult")
                        {
                            displayImage(promotionIV, womenPromotionImageURL)
                        }
                        else
                        {
                            displayImage(promotionIV, girlPromotionImageURL)
                        }
                    }
                    else
                    {
                        if (foundAge == "adult")
                        {
                            displayImage(promotionIV, menPromotionImageURL)
                        }
                        else
                        {
                            displayImage(promotionIV, boyPromotionImageURL)
                        }

                    }
                }


            }

        })

    }

    private fun displayImage(promotionIV : ImageView, imageURL : String)
    {
        doAsync {

            val image : Bitmap = imageHandler.getBitmapFromURL(imageURL, promotionIV.height, promotionIV.width)

            uiThread {

                promotionIV.setImageBitmap(image)
            }
        }
    }
}*/