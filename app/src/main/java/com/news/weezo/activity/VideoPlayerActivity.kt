package com.news.weezo.activity

import android.app.PictureInPictureParams
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.news.weezo.R
import kotlinx.android.synthetic.main.activity_video_play.*


class VideoPlayerActivity : AppCompatActivity() {
    private var onStopCalled: Boolean = false
    private var videoPosition: Long=0
    private lateinit  var player: SimpleExoPlayer
    var module_name=""
    var mUrl = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_play)
        mUrl = intent?.getStringExtra("url")!!
        module_name=intent?.getStringExtra("name")!!

        //mUrl="http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"

    }

    override fun onPause() {
        super.onPause()
    }
    override fun onResume() {
        super.onResume()
        hideSystemUi()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        mUrl = intent?.getStringExtra("url")!!
        module_name=intent?.getStringExtra("name")!!



        val dataSourceFactory =
                DefaultDataSourceFactory(this,
                        Util.getUserAgent(this,
                                module_name))

        when (Util.inferContentType(Uri.parse(mUrl))) {
            C.TYPE_HLS -> {
                val mediaSource = HlsMediaSource
                        .Factory(dataSourceFactory)
                        .createMediaSource(Uri.parse(mUrl))
                player.prepare(mediaSource)
            }

            C.TYPE_OTHER -> {
                val mediaSource = ExtractorMediaSource
                        .Factory(dataSourceFactory)

                        .createMediaSource(Uri.parse(mUrl))
                player.prepare(mediaSource)
            }

            else -> {
                //This is to catch SmoothStreaming and
                //DASH types which we won't support currently, exit
               // finish()
            }
        }
      //  player.playWhenReady = true
        playerView.useController=true
       // hideSystemUi()
       // playerView.invalidate()

    }
    private fun hideSystemUi() {
        playerView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    override fun onStart() {
        super.onStart()
        player = ExoPlayerFactory
                .newSimpleInstance(this, DefaultTrackSelector())

        playerView.player = player

        val dataSourceFactory =
                DefaultDataSourceFactory(this,
                        Util.getUserAgent(this,
                               module_name))

        when (Util.inferContentType(Uri.parse(mUrl))) {
            C.TYPE_HLS -> {
                val mediaSource = HlsMediaSource
                        .Factory(dataSourceFactory)
                        .createMediaSource(Uri.parse(mUrl))
                player.prepare(mediaSource)
            }

            C.TYPE_OTHER -> {
                val mediaSource = ExtractorMediaSource
                        .Factory(dataSourceFactory)

                        .createMediaSource(Uri.parse(mUrl))
                player.prepare(mediaSource)
            }

            else -> {
                //This is to catch SmoothStreaming and
                //DASH types which we won't support currently, exit
                finish()
            }
        }
        player.playWhenReady = true
    }
    override fun onStop() {
        super.onStop()

        playerView.player = null
        player.release()
        onStopCalled = true


    }


    @Suppress("DEPRECATION")
    fun enterPIPMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                && packageManager
                        .hasSystemFeature(
                                PackageManager.FEATURE_PICTURE_IN_PICTURE)) {
            videoPosition = player.currentPosition
            playerView.useController = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                 var params = PictureInPictureParams.Builder()
                this.enterPictureInPictureMode(params.build())
            } else {
                this.enterPictureInPictureMode()
            }
        }
    }

    override fun onBackPressed() {
       // super.onBackPressed()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                && packageManager
                        .hasSystemFeature(
                                PackageManager.FEATURE_PICTURE_IN_PICTURE)){
            enterPIPMode()
        } else {
            super.onBackPressed()
        }

    }

}