package com.news.weezo.webtask

import com.news.weezo.MainApplication
import com.online.academic.utils.Constants
import org.json.JSONObject


/**
 * Created by shailendra on 3/18/2018.
 */

class VolleyMethodsKotlin {

    companion object {





        fun getLikeVideo(action: String, video_id: String, value: Int, webResponseListener: WebResponseListener) {
            val volleyRequest = MainApplication.instance!!.getVolleyRequest()!!
            val jsonObjectParam = JSONObject()
            jsonObjectParam.put("user_id", MainApplication.instance!!.getPrefs().getUserToken())
            jsonObjectParam.put("video_id", video_id)
            jsonObjectParam.put("value", ""+value)
            volleyRequest.getResponseFromJson(Constants.URL_BASE+action, action, jsonObjectParam, webResponseListener)
        }
        fun updateVideoView(action: String, video_id: String, webResponseListener: WebResponseListener) {
            val volleyRequest = MainApplication.instance!!.getVolleyRequest()!!
            val jsonObjectParam = JSONObject()
            jsonObjectParam.put("user_id", MainApplication.instance!!.getPrefs().getUserToken())
            jsonObjectParam.put("video_id", video_id)
            volleyRequest.getResponseFromJson(Constants.URL_BASE+action, action, jsonObjectParam, webResponseListener)
        }
        fun addUser(action: String, webResponseListener: WebResponseListener) {
            val volleyRequest = MainApplication.instance!!.getVolleyRequest()!!
            val jsonObjectParam = JSONObject()
            jsonObjectParam.put("device_id", MainApplication.instance!!.getPrefs().getUserToken())
            jsonObjectParam.put("device_token", MainApplication.instance!!.getPrefs().getNotificationToken())
            volleyRequest.getResponseFromJson(Constants.URL_BASE+action, action, jsonObjectParam, webResponseListener)
        }

        fun getNews( action: String,page:Int, webResponseListener: WebResponseListener) {
            val volleyRequest = MainApplication.instance!!.getVolleyRequest()!!
           // page=1&device_id=123
            val baseURl = Constants.URL_BASE+action+"?page="+page+"&device_id="+MainApplication.instance!!.getPrefs().getUserToken()
            val jsonObject = JSONObject()
            jsonObject.put("name", action)

            val jsonObjectParam = JSONObject()
//            jsonObjectParam.put("search_by", search_by)
            jsonObject.put("param", jsonObjectParam);

            volleyRequest.getResponseFromJson(baseURl, action, jsonObject, webResponseListener)

        }



    }
}