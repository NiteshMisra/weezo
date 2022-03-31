package com.news.weezo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.news.weezo.R
import com.news.weezo.model.MediaFile

class BannerAdapter(
    private val context: Context,
    private val bannerList: List<MediaFile>
) : PagerAdapter() {

    override fun getCount(): Int {
        return bannerList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val currentItem: MediaFile = bannerList[position]
        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v: View = inflater.inflate(R.layout.item_banner, container, false)
        val image = v.findViewById<ImageView>(R.id.image_iv)
        val titleTv = v.findViewById<TextView>(R.id.title_tv)
        Glide.with(context)
            .load(currentItem.music_filename)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(image)
        titleTv.text = currentItem.name
        container.addView(v)
        return v
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }
}