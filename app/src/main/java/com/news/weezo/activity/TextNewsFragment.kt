package com.news.weezo.activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.news.weezo.R
import com.news.weezo.adapter.BannerAdapter
import com.news.weezo.adapter.TextNewsAdapter
import com.news.weezo.adapter.VideoAdapter
import com.news.weezo.model.MediaFile
import com.news.weezo.utils.PaginationScrollListener
import com.news.weezo.utils.SpacesItemDecoration
import com.news.weezo.webtask.ParseJson
import com.news.weezo.webtask.VolleyMethodsKotlin
import com.news.weezo.webtask.WebResponseListener
import com.online.academic.utils.Constants
import com.online.academic.utils.OnRecycleItemClick
import com.online.academic.utils.Utils
import kotlinx.android.synthetic.main.frg_video.*
import java.lang.Exception

class TextNewsFragment : Fragment(), OnRecycleItemClick, WebResponseListener {
    private lateinit var adapter: TextNewsAdapter
    var page = 0
    var isLoadingData = false
    var isLastPageData = false
    var TOTAL_PAGES = 5

    private var bannerList = ArrayList<MediaFile>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.frg_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sw_refresh.setOnRefreshListener {
            getNewsVideo()
        }
        ll_player_view.visibility = View.GONE
        tv_media_title.visibility =View.GONE
        val mLayoutManager1Linear = androidx.recyclerview.widget.LinearLayoutManager(
            activity,
            androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
            false
        )
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.space1)

        rec_video.addItemDecoration(SpacesItemDecoration(spacingInPixels))
        rec_video.setLayoutManager(mLayoutManager1Linear)
        adapter = TextNewsAdapter(activity!!, R.layout.row_text_news, ArrayList(), this)
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
        rec_video.adapter = adapter

        getNewsVideo()

        banner_view_pager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                pos: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(pos: Int) {
                bannerBottomDots(layout_dots, bannerList.size, pos % bannerList.size)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

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
        activity?.runOnUiThread {
            progressbar.visibility = View.GONE
            isLoadingData = false
        }
        if (error == null) {
            if (action.equals(Constants.WEB_ACTION_GET_VIDEO)) {
                if (ParseJson.isSuccess(response)) {
                    activity?.runOnUiThread {
                        val list = ParseJson.parseVideoList(response)
                        val newsList =  ArrayList<MediaFile>()
                        val completeNewsList = Utils.getNewsTypeList(list,Constants.TEXT_NEWS)
                        if (page == 1){
                            bannerList =  ArrayList()
                            for ((i, item) in completeNewsList.withIndex()){
                                if (i < 5){
                                    bannerList.add(item)
                                }else{
                                    newsList.add(item)
                                }
                            }
                        }else{
                            newsList.addAll(completeNewsList)
                        }
                        adapter.list = newsList

                        if (page == 1){
                            val bannerAdapter = BannerAdapter(requireContext(), bannerList)
                            banner_view_pager.adapter = bannerAdapter
                            bannerAdapter.notifyDataSetChanged()

                            bannerBottomDots(layout_dots, bannerList.size, 0)
                        }

                        if (newsList.size == 0){
                            getNewsVideo()
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

    override fun onItemClick(position: Int, view: View) {
        var model = adapter.list.get(position)
        if (view.id == R.id.img_share) {
            var str =
                "" + model!!.music_filename + "\n source : " + model.description + "\n I am getting local news very first via below app :  \n" + Constants.URL_APP_STORE
            shareVideo(str)
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
        } else if(view.id == R.id.tv_click_toread_more){
            MainActivity.countNumberAds ++;
            VolleyMethodsKotlin.updateVideoView(Constants.WEB_ACTION_VIEWVIDEO, model.id, this)
            var intent = Intent(activity!!,TextNewsDetailsActivity::class.java)
            intent.putExtra("data",model)
            startActivity(intent)
        }

        else {
           // navigateToVideoPlayer(position)
        }

    }
    private fun navigateToVideoPlayer(position: Int) {
        var mediaFile=  adapter.list.get(position)
        mediaFile.views=""+(mediaFile.views.toInt()+1)
        adapter.notifyItemChanged(position)
        VolleyMethodsKotlin.updateVideoView(Constants.WEB_ACTION_VIEWVIDEO, mediaFile.id, this)
        var intent = Intent(activity, VideoPlayerActivity::class.java)
        intent.putExtra("url", mediaFile.music_filename)
        intent.putExtra("name", mediaFile.name)
        startActivity(intent)

    }
    private fun shareVideo(str: String) {
        val share = Intent(Intent.ACTION_SEND)
        share.type = "text/plain"
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
        share.setPackage("com.whatsapp")
        share.putExtra(Intent.EXTRA_TEXT, str)
        try {
            activity?.startActivity(share)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(activity,"Whatsapp have not been installed.",Toast.LENGTH_LONG).show()
        }
        // Add data to the intent, the receiving app will decide
        // what to do with it.

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        // share.putExtra(Intent.EXTRA_SUBJECT, "")

    }

    private fun bannerBottomDots(layout_dots: LinearLayout, size: Int, current: Int) {
        val dots = arrayOfNulls<ImageView>(size)
        layout_dots.removeAllViews()
        try {
            for (i in dots.indices) {
                dots[i] = ImageView(activity)
                val width_height = 17
                val params =
                    LinearLayout.LayoutParams(ViewGroup.LayoutParams(width_height, width_height))
                params.setMargins(10, 0, 10, 0)
                dots[i]!!.layoutParams = params
                dots[i]!!.setImageResource(R.drawable.banner_dots)
                dots[i]!!.setColorFilter(
                    ContextCompat.getColor(activity!!, R.color.bannerDotsColor),
                    PorterDuff.Mode.SRC_ATOP
                )
                layout_dots.addView(dots[i])
            }
            if (dots.size > 0) {
                dots[current]!!.setImageResource(R.drawable.banner_dots)
                dots[current]!!.setColorFilter(
                    ContextCompat.getColor(activity!!, R.color.bannerSelectedColor),
                    PorterDuff.Mode.SRC_ATOP
                )
            }
        } catch (e: Exception) {
            //to be handled later or to change dots layout
        }
    }

}