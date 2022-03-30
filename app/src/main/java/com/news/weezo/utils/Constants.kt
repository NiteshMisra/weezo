package com.online.academic.utils

import com.news.weezo.BuildConfig


/**
 * Created by abc on 6/3/2018.
 */

interface Constants {
    companion object {

        //val URL_BASE = "https://codefriend.in/development/video/api/"
        val URL_BASE = "https://weezonews.com/api/"
        val WEB_ACTION_GET_VIDEO = "getDataList"
        val WEB_ACTION_ADD_USER = "addUser"
        val WEB_ACTION_LIKEVIDEO = "likeVideo"
        val WEB_ACTION_VIEWVIDEO = "viewVideo"
        var URL_APP_STORE =
            "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID
        var URL_NEWS = "https://www.himachalxpresstv.com/"
        var TEXT_NEWS = "Text"
        var VIDEO_NEWS = "Video"

    }


}
