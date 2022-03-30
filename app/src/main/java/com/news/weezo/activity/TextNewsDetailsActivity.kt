package com.news.weezo.activity

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.news.weezo.R
import com.news.weezo.model.MediaFile
import kotlinx.android.synthetic.main.frg_new_details.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class TextNewsDetailsActivity: AppCompatActivity() {
    var mInterstitialAd:InterstitialAd?=null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.frg_new_details)
        var model = intent.getParcelableExtra<MediaFile>("data")
        tv_title.setText(Html.fromHtml(model?.name).toString().trim())

        //new line added for the date shown on the text news details activity
        var df: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:sss")
        val date: Date = df.parse(model?.datetime)
        // println("date:$date")
        df = SimpleDateFormat("dd-MMM-yyyy")
        var dateResult = df.format(date);
        tv_date.setText("Updated: " + dateResult + " | Edited By: Weezo")
        //new line added for the date shown on the text news details activity

        if (model?.description != null) {

            tv_description.setText(Html.fromHtml(model?.description).toString().trim())
        }
Log.i("image url ",model!!.music_filename)
        Glide.with(this)
            .load(model.music_filename)
            .placeholder(R.drawable.ic_sp_app_icon)
            .into(img_preview)

        if (MainActivity.countNumberAds== 1 ||  MainActivity.countNumberAds%3==0) {
            var adRequest = AdRequest.Builder().build()

            InterstitialAd.load(this, getString(R.string.full_ads), adRequest, object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {

                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {

                    mInterstitialAd = interstitialAd
                    mInterstitialAd!!.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            // Called when fullscreen content is dismissed.
                            Log.d("TAG", "The ad was dismissed.")
                            finish()
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            // Called when fullscreen content failed to show.
                            Log.d("TAG", "The ad failed to show.")
                        }

                        override fun onAdShowedFullScreenContent() {
                            // Called when fullscreen content is shown.
                            // Make sure to set your reference to null so you don't
                            // show it a second time.
                            mInterstitialAd = null
                            Log.d("TAG", "The ad was shown.")
                        }
                    }
                }
            })
        }
        img_back.setOnClickListener {
            if (mInterstitialAd != null) {
                mInterstitialAd?.show(this)
            } else {
                finish()
            }

        }
    }

    override fun onBackPressed() {

        if (mInterstitialAd != null) {
            mInterstitialAd?.show(this)
        } else {
            finish()
        }
    }
}