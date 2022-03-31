package com.news.weezo.activity

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.gms.ads.*
import com.google.android.gms.ads.initialization.InitializationStatus
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.google.firebase.iid.FirebaseInstanceId
import com.news.weezo.BuildConfig
import com.news.weezo.MainApplication
import com.news.weezo.R
import com.news.weezo.adapter.MyPagerAdapter
import com.news.weezo.adapter.VideoAdapter
import com.news.weezo.dialog.DialogRating
import com.news.weezo.utils.AppConfigs.Companion.MY_FILE_DATA
import com.news.weezo.utils.AppConfigs.Companion.RATED_IN_STORE
import com.news.weezo.utils.AppConfigs.Companion.TIME_INTO_APP
import com.news.weezo.utils.VideoFragmentInterface
import com.news.weezo.utils.VideoPlayerInterface
import com.news.weezo.webtask.ParseJson
import com.news.weezo.webtask.VolleyMethodsKotlin
import com.news.weezo.webtask.WebResponseListener
import com.online.academic.utils.Constants
import com.online.academic.utils.Utils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.row_text_news.*
import java.util.*


class MainActivity : AppCompatActivity(), WebResponseListener, VideoPlayerInterface, NavigationView.OnNavigationItemSelectedListener{
    private lateinit var adapter: VideoAdapter
    var page = 0
    var isLoadingData = false
    var isLastPageData = false
    var TOTAL_PAGES = 5
    var type = ""
    private var isRate = false

    companion object {
        var countNumberAds = 0;
    }

    private lateinit var mAdView: AdView
    private var isShow = true;
    private var videoFragmentInterface: VideoFragmentInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //	MobileAds.initialize(this, "ca-app-pub-7526249640656829~6243823694");
//		MobileAds.initialize(this, new OnInitializationCompleteListener() {
//			@Override
//			public void onInitializationComplete(InitializationStatus initializationStatus) {
//			}
//		});

//        val imageSlider = findViewById<ImageSlider>(R.id.imageSlide)
//        val imageList = ArrayList<SlideModel>()
//        imageList.add(SlideModel(R.drawable.ic_app_icon))
//        imageList.add(SlideModel(R.drawable.ic_app_icon))
//        imageList.add(SlideModel(R.drawable.ic_app_icon))
//
//        imageSlider.setImageList(imageList,ScaleTypes.FIT)

        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        navigationView.setNavigationItemSelectedListener(this)

        MobileAds.initialize(this, object : OnInitializationCompleteListener {
            override fun onInitializationComplete(initializationStatus: InitializationStatus?) {}
        })
        mAdView = findViewById(R.id.adView)
        mAdView?.adListener = object : AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Code to be executed when an ad request fails.
                mAdView?.visibility = View.GONE;
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        }

        if (MainApplication.instance!!.getPrefs().getIsUserRegistered()) {
            // getNewsVideo()
        } else {
            doAddUser()
        }
        if (intent.hasExtra("type")) {
            type = intent.getStringExtra("type")!!
        }
//        img_contact_us.visibility = View.GONE
//        img_contact_us.setOnClickListener {
//            startActivity(Intent(this, ContactUSPage::class.java))
//        }
//        img_share.setOnClickListener {
//            var str =
//                "Download WeeZo android app to get latest local news very first form below link :\n" + Constants.URL_APP_STORE
//            shareVideo(str)
//        }
//        img_about.setOnClickListener {
//            startActivity(Intent(this, AboutUsActivity::class.java))
//        }

        getFirebaseInstanceId()

//            .setIcon(R.drawable.ic_sp_app_icon));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.shorts_hindi));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.video_hindi));
        val adapter = MyPagerAdapter(this, supportFragmentManager, tabLayout.tabCount)
        view_pager.setAdapter(adapter)

        view_pager.addOnPageChangeListener(TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                view_pager.setCurrentItem(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        if (type.isEmpty() || type.equals(Constants.TEXT_NEWS, true)) {
            view_pager.setCurrentItem(0, true)
        } else {
            view_pager.setCurrentItem(1, true)
        }

        //dev kiet add
        var sharedPreferences = getSharedPreferences(MY_FILE_DATA, 0)
        val str: String = TIME_INTO_APP
        var i: Int = sharedPreferences.getInt(str, 0)
        if (i < 2) {
            isRate = false
            sharedPreferences.edit().putInt(str, i + 1).apply()
            // ShowInterAdMaster.show(context);
        } else if (!sharedPreferences.getBoolean(RATED_IN_STORE, false)) {
            isRate = true
            DialogRating((this as Activity)!!).show()
        }

        hamburger_icon_iv.setOnClickListener { hamburgerClicked() }

        navClickHandle()
    }

    private fun navClickHandle() {
        video_tv.setOnClickListener {
            hamburgerClicked()
            view_pager.currentItem = 1
        }

        shorts_tv.setOnClickListener {
            hamburgerClicked()
            view_pager.currentItem = 0
        }

        rate_us_tv.setOnClickListener {
            hamburgerClicked()
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${BuildConfig.APPLICATION_ID}")))
            } catch (e: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}")))
            }
        }

        share_tv.setOnClickListener {
            hamburgerClicked()
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT,
                "Download WeeZo android app to get latest local news very first form below link :\n${BuildConfig.APPLICATION_ID}"
            )
            startActivity(Intent.createChooser(intent, title))
        }

        about_us_tv.setOnClickListener {
            hamburgerClicked()
            startActivity(Intent(this,AboutUsActivity::class.java))
        }
    }


    private fun doAddUser() {
        //  progressbar.visibility = View.VISIBLE
        VolleyMethodsKotlin.addUser(Constants.WEB_ACTION_ADD_USER, this)
    }

    private fun getNewsVideo() {

        if (!isLastPageData && !isLoadingData) {
            VolleyMethodsKotlin.getNews(Constants.WEB_ACTION_GET_VIDEO, ++page, this)
        }
    }

    override fun onResponseReceived(error: String?, action: String, response: String?) {
        Log.i("tag : ", "" + action)
        Log.i("response : ", response!!)
        Log.i("error : ", "" + error)
        runOnUiThread {
            //   progressbar.visibility = View.GONE
            isLoadingData = false
        }
        if (error == null) {
            if (action.equals(Constants.WEB_ACTION_GET_VIDEO)) {
                if (ParseJson.isSuccess(response)) {
                    runOnUiThread {
                        var list = ParseJson.parseVideoList(response)
                        adapter.list = list
                    }
                } else {
                    isLastPageData = true
                }
            } else if (action.equals(Constants.WEB_ACTION_ADD_USER)) {
                runOnUiThread {
                    // getNewsVideo()
                }

            }
            if (isShow) {
                isShow = false;
                val adRequest = AdRequest.Builder().build()
                if (mAdView != null)
                    mAdView?.loadAd(adRequest)
            }
        } else {
            Utils.showAlertDialog(this, error, "")
        }
    }


    private fun shareVideo(str: String) {
        val share = Intent(Intent.ACTION_SEND)
        share.type = "text/plain"
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)

        // Add data to the intent, the receiving app will decide
        // what to do with it.

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        // share.putExtra(Intent.EXTRA_SUBJECT, "")
        share.putExtra(Intent.EXTRA_TEXT, str)
        startActivity(Intent.createChooser(share, "Share News!"))
    }

    private fun navigateToVideoPlayer(position: Int) {
        var mediaFile = adapter.list.get(position)
        mediaFile.views = "" + (mediaFile.views.toInt() + 1)
        adapter.notifyItemChanged(position)
        VolleyMethodsKotlin.updateVideoView(Constants.WEB_ACTION_VIEWVIDEO, mediaFile.id, this)
        var intent = Intent(this@MainActivity, VideoPlayerActivity::class.java)
        intent.putExtra("url", mediaFile.music_filename)
        intent.putExtra("name", mediaFile.name)
        startActivity(intent)

    }

    private fun getFirebaseInstanceId() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("FirebaseToke", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }
                // Get new Instance ID token
                var firebaseToken = task.result!!.token
                MainApplication.instance!!.getPrefs().setNotication(firebaseToken)
                Log.i("++toke", "")
                // Log and toast

                //   Toast.makeText(this@LoginActivity, token, Toast.LENGTH_SHORT).show()
            })
    }

    //  var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
            return
        }

        if (videoFragmentInterface?.isInFullScreen() == true){
            videoFragmentInterface?.exitFullScreen()
            return
        }

        if (!getSharedPreferences(MY_FILE_DATA, 0).getBoolean(RATED_IN_STORE, false)) {
            DialogRating(this).show()
        } else {
            showCloseDialog()
//            if (doubleBackToExitPressedOnce) {
//                super.onBackPressed()
//                return
//            }
//            doubleBackToExitPressedOnce = true
//            Toast.makeText(this, getString(R.string.back_again), Toast.LENGTH_SHORT).show()
//            Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
        }
    }

    private fun showCloseDialog() {
        val dialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.app_namefull))
            .setIcon(R.drawable.ic_app_icon)
            .setMessage(getString(R.string.msg_close_app))
            .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                // Do stuff
                super.onBackPressed()
            }.setNegativeButton(getString(R.string.no)) { dialog, _ ->
                // Do stuff
                dialog.dismiss()
            }.create()

        dialog.setCancelable(false)


        dialog.show()
    }

    override fun setListener(videoFragmentInterface: VideoFragmentInterface) {
        this.videoFragmentInterface = videoFragmentInterface
    }

    fun hamburgerClicked() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    override fun enterInFullScreenMode() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        mAdView.visibility = View.GONE
        tabLayout.visibility = View.GONE
        ll_header.visibility = View.GONE
    }

    override fun exitFullScreenMode() {
        mAdView.visibility = View.VISIBLE
        tabLayout.visibility = View.VISIBLE
        ll_header.visibility = View.VISIBLE
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return true
    }
}