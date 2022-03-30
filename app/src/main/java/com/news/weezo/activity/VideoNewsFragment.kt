package com.news.weezo.activity


import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.material.tabs.TabLayout
import com.news.weezo.MainApplication
import com.news.weezo.R
import com.news.weezo.adapter.VideoAdapter
import com.news.weezo.model.MediaFile
import com.news.weezo.utils.PaginationScrollListener
import com.news.weezo.utils.SpacesItemDecoration
import com.news.weezo.utils.VideoFragmentInterface
import com.news.weezo.utils.VideoPlayerInterface
import com.news.weezo.webtask.ParseJson
import com.news.weezo.webtask.VolleyMethodsKotlin
import com.news.weezo.webtask.WebResponseListener
import com.online.academic.utils.Constants
import com.online.academic.utils.OnRecycleItemClick
import com.online.academic.utils.Utils
import kotlinx.android.synthetic.main.exo_playback_control_view.*
import kotlinx.android.synthetic.main.frg_video.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class VideoNewsFragment : Fragment(), OnRecycleItemClick, WebResponseListener,
    View.OnClickListener, VideoFragmentInterface {
    private var selectedModel: MediaFile? = null
    private var fullscreen: Boolean = false
    private lateinit var dataSourceFactory: DefaultDataSourceFactory

    //  private lateinit var adapter: VideoAdapter
    private lateinit var adapter: VideoAdapter

    private lateinit var videoPlayerInterface: VideoPlayerInterface
    var page = 0

    var isLoadingData = false
    var isLastPageData = false
    var TOTAL_PAGES = 5
    var isPlayingVideo = false;
    private var scaleGestureDetector: ScaleGestureDetector? = null
    private lateinit var fullscreenButton : ImageView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        videoPlayerInterface = context as VideoPlayerInterface
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        var view = inflater.inflate(R.layout.frg_video, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        videoPlayerInterface.setListener(this)
        sw_refresh.setOnRefreshListener {
            getNewsVideo()
        }
        val mLayoutManager1Linear = androidx.recyclerview.widget.LinearLayoutManager(
            activity,
            androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
            false
        )
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.space2)

        rec_video.addItemDecoration(SpacesItemDecoration(spacingInPixels))
        rec_video.setLayoutManager(mLayoutManager1Linear)
        adapter = VideoAdapter(activity!!, R.layout.row_video_horizontal, ArrayList(), this)
        rec_video.adapter = adapter
        rec_video.addOnScrollListener(object : PaginationScrollListener(mLayoutManager1Linear) {
            override fun isLastPage(): Boolean {
                return isLastPageData;
            }

            override fun loadMoreItems() {

                getNewsVideo()
            }

            override fun getTotalPageCount(): Int {
                return TOTAL_PAGES;
            }

            override fun isLoading(): Boolean {
                return isLoadingData;
            }
            /* protected fun loadMoreItems() {
                 isLoading = true
                 currentPage += 1
                 loadNextPage()
             }

             val totalPageCount: Int
                 get() = MainActivity.TOTAL_PAGES

             val isLastPage: Boolean

             val isLoading: Boolean*/
        })

        img_like.setOnClickListener(this)
        img_share.setOnClickListener(this)
        getNewsVideo()
        player = ExoPlayerFactory
            .newSimpleInstance(activity, DefaultTrackSelector())

        playerView.player = player
        scaleGestureDetector =
            ScaleGestureDetector(activity!!, CustomOnScaleGestureListener(playerView))
        dataSourceFactory =
            DefaultDataSourceFactory(
                activity,
                Util.getUserAgent(
                    activity,
                    module_name
                )
            )
        initforFullmode(view)

    }

    private fun initforFullmode(view: View) {
        var tabLayout = activity?.findViewById<TabLayout>(R.id.tabLayout)
        var playerView = view.findViewById<PlayerView>(R.id.playerView);
        fullscreenButton = view.findViewById(R.id.exo_fullscreen_icon)
        var header = activity?.findViewById<RelativeLayout>(R.id.ll_header)
        fullscreenButton.setOnClickListener(View.OnClickListener {
            if (fullscreen) {
                if (activity?.findViewById<RelativeLayout>(R.id.ll_header) != null) {
                    activity?.actionBar?.show();
                }
                videoPlayerInterface.exitFullScreenMode()
                fullscreenButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        activity!!.applicationContext,
                        R.drawable.ic_full_screen
                    )
                )
                activity!!.getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE)

                activity?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                val params = playerView.layoutParams as LinearLayout.LayoutParams
                params.width = ViewGroup.LayoutParams.MATCH_PARENT
                params.height =
                    (200 * MainApplication.instance?.getApplicationContext()?.getResources()
                        ?.getDisplayMetrics()!!.density).toInt()
                playerView.layoutParams = params
                fullscreen = false
                //playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
            } else {

                videoPlayerInterface.enterInFullScreenMode()

                fullscreenButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        activity!!.applicationContext,
                        R.drawable.ic_exitfull_screen
                    )
                )
                activity?.getWindow()?.getDecorView()?.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                )

                activity?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
                val params = playerView.layoutParams as LinearLayout.LayoutParams
                params.width = ViewGroup.LayoutParams.MATCH_PARENT
                params.height = ViewGroup.LayoutParams.MATCH_PARENT
                playerView.layoutParams = params
                //  playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                fullscreen = true
            }
        })
        playerView.player = player
        playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT


    }

    private var player: SimpleExoPlayer? = null
    var module_name = ""
    var mUrl = ""
    private fun initplayer(isPlay: Boolean, model: MediaFile) {

        selectedModel = model;
        tv_media_title.setText(model.description)
        tv_total_likes.setText("" + Integer.parseInt(model.likes))
        tv_total_views.setText("" + Integer.parseInt(model.views))

        var df: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:sss")
        val date: Date = df.parse(model.datetime)
        // println("date:$date")
        df = SimpleDateFormat("dd-MMM-yyyy")
        var dateResult = df.format(date);
        tv_date_time.setText("" + dateResult)

        mUrl = model.music_filename

        val dataSourceFactory: DataSource.Factory =
            DefaultDataSourceFactory(
                MainApplication.instance!!.getApplicationContext(),
                Util.getUserAgent(
                    MainApplication.instance!!.getApplicationContext(),
                    MainApplication.instance?.getApplicationContext()
                        ?.getString(com.news.weezo.R.string.app_name)
                )
            )

        val videoSource: MediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(Uri.parse(mUrl))
        player?.prepare(videoSource);
        player?.setPlayWhenReady(isPlay);

    }


    override fun onPause() {
        player?.playWhenReady = false
        super.onPause()
    }

    override fun onResume() {
        player?.playWhenReady = false
        super.onResume()
    }

    private fun getNewsVideo() {

        sw_refresh.isRefreshing = false
        if (!isLastPageData && !isLoadingData) {
            progressbar.visibility = View.VISIBLE
            VolleyMethodsKotlin.getNews(Constants.WEB_ACTION_GET_VIDEO, ++page, this)
        }
    }

    override fun onResponseReceived(error: String?, action: String, response: String?) {
        Log.i("tag : ", "" + action)
        Log.i("response : ", response!!)
        Log.i("error : ", "" + error)
        activity!!.runOnUiThread {
            progressbar.visibility = View.GONE
            isLoadingData = false
        }
        if (error == null) {
            if (action.equals(Constants.WEB_ACTION_GET_VIDEO)) {
                if (ParseJson.isSuccess(response)) {
                    activity!!.runOnUiThread {
                        ll_player_view.visibility = View.VISIBLE
                        var list = ParseJson.parseVideoList(response)
                        adapter.list = Utils.getNewsTypeList(list, Constants.VIDEO_NEWS)
                        adapter.notifyDataSetChanged()
                        if (!isPlayingVideo) {
                            isPlayingVideo = true

                            initplayer(false, adapter.list.get(0))
                        }

                    }
                } else {
                    isLastPageData = true
                }
            } else if (action.equals(Constants.WEB_ACTION_ADD_USER)) {
                activity!!.runOnUiThread {
                    getNewsVideo()
                }

            }

        } else {
            Utils.showAlertDialog(activity!!, error, "")
        }
    }

    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)
        if (!menuVisible && player != null) {
            exo_pause.performClick()

        }

    }

    override fun onItemClick(position: Int, view: View) {
        var model = adapter.list.get(position)
        if (view.id == R.id.img_share) {
            /*var str =
                "" + model!!.music_filename + "\n source : " + model.description + "\n I am getting local news very first via below app :  \n" + Constants.URL_APP_STORE
            shareVideo(str)*/

            Utils.shareShortUrl(model!!.music_filename, model!!.description, activity!!)
        } else if (view.id == R.id.img_like) {
            var islike = 0

            if (model.is_liked.toInt() == 0) {
                islike = 1
                model.likes = "" + (model.likes.toInt() + 1)
            } else {
                islike = 0
                if (model.likes.toInt() > 0) {
                    model.likes = "" + (model.likes.toInt() - 1)
                }
            }
            model.is_liked = islike.toString()

            adapter.notifyItemChanged(position)
            VolleyMethodsKotlin.getLikeVideo(Constants.WEB_ACTION_LIKEVIDEO, model.id, islike, this)

        } else if (view.id == R.id.tv_src) {
            val url = getString(R.string.src_url)
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        } else {
            navigateToVideoPlayer(position)
        }

    }

    private fun navigateToVideoPlayer(position: Int) {
        var mediaFile = adapter.list.get(position)
        mediaFile.views = "" + (mediaFile.views.toInt() + 1)
        adapter.notifyItemChanged(position)
        VolleyMethodsKotlin.updateVideoView(Constants.WEB_ACTION_VIEWVIDEO, mediaFile.id, this)
        /* var intent = Intent(activity, VideoPlayerActivity::class.java)
         intent.putExtra("url", mediaFile.music_filename)
         intent.putExtra("name", mediaFile.name)
         startActivity(intent)*/
        selectedModel = adapter.list.get(position)
        mUrl = mediaFile.music_filename
        module_name = mediaFile.name
        tv_media_title.setText(mediaFile.description)
        initplayer(true, selectedModel!!)
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

    private class CustomOnScaleGestureListener(
        private val player: PlayerView,
    ) : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        private var scaleFactor = 0f

        override fun onScale(
            detector: ScaleGestureDetector,
        ): Boolean {
            scaleFactor = detector.scaleFactor
            return true
        }

        override fun onScaleBegin(
            detector: ScaleGestureDetector,
        ): Boolean {
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector) {
            if (scaleFactor > 1) {
                player.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
            } else {
                player.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
            }
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.img_share -> {


                Utils.shareShortUrl(
                    selectedModel!!.music_filename,
                    selectedModel!!.description,
                    activity!!
                )
            }

            R.id.img_like -> {

                var islike = 0

                if (selectedModel!!.is_liked.toInt() == 0) {
                    islike = 1
                    selectedModel!!.likes = "" + (selectedModel!!.likes.toInt() + 1)
                } else {
                    islike = 0
                    if (selectedModel!!.likes.toInt() > 0) {
                        selectedModel!!.likes = "" + (selectedModel!!.likes.toInt() - 1)
                    }
                }
                selectedModel!!.is_liked = islike.toString()

                tv_total_likes.setText("" + Integer.parseInt(selectedModel!!.likes))
                // adapter.notifyItemChanged(position)
                VolleyMethodsKotlin.getLikeVideo(
                    Constants.WEB_ACTION_LIKEVIDEO,
                    selectedModel!!.id,
                    islike,
                    this
                )


            }
        }
    }

    override fun isInFullScreen(): Boolean = fullscreen

    override fun exitFullScreen() {
        if (activity?.findViewById<RelativeLayout>(R.id.ll_header) != null) {
            activity?.actionBar?.show();
        }
        videoPlayerInterface.exitFullScreenMode()
        fullscreenButton.setImageDrawable(
            ContextCompat.getDrawable(
                activity!!.applicationContext,
                R.drawable.ic_full_screen
            )
        )
        activity!!.getWindow().getDecorView()
            .setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE)

        activity?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        val params = playerView.layoutParams as LinearLayout.LayoutParams
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height =
            (200 * MainApplication.instance?.getApplicationContext()?.getResources()
                ?.getDisplayMetrics()!!.density).toInt()
        playerView.layoutParams = params
        fullscreen = false
    }

}