package com.advocate.articles.webServices

import android.util.Log
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.error.AuthFailureError
import com.android.volley.error.NoConnectionError
import com.android.volley.request.JsonObjectRequest
import com.news.weezo.MainApplication
import com.news.weezo.webtask.WebResponseListener
import org.json.JSONObject
import java.io.UnsupportedEncodingException


/**
 * Created by shailendra on 3/18/2018.
 */

class VolleyRequestKotlin {


    fun getResponseFromJson(url: String, tag: String, parm: JSONObject?, webResponseListner: WebResponseListener) {

        Log.i("TAG", "action : " + tag)
        Log.i("TAG", "URL_BASE : " + url)
        Log.i("TAG", "parm : " + parm)


        val req = object : JsonObjectRequest(Request.Method.POST, url, parm, Response.Listener { response ->
            if (response != null) {
                webResponseListner.onResponseReceived(null, tag, response!!.toString())
            } else {
                webResponseListner.onResponseReceived("error", tag, response!!.toString())
            }
        }, Response.ErrorListener { error ->
            var mess = "Error"

            try {

                mess = error.toString()
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }


            if (error is NoConnectionError) {
                webResponseListner!!.onResponseReceived("No Network", "", tag)
            } else if (mess.contains("Invalid Credentials")) {
                webResponseListner!!.onResponseReceived(mess, "", tag)
            } else {
                webResponseListner!!.onResponseReceived("Internal server error", "", tag)
            }
        }) {


          /*  @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {

                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer " + MainApplication.instance!!.getPrefs().getToken()
//                headers["authorization"]="bearer"

                return headers
            }
*/
            @Throws(AuthFailureError::class)
            override fun getBodyContentType(): String {
                return "application/json"
            }
        }
        req.setShouldCache(false)// todo added this to remove cache from request
        val TIME_OUT = 80000
        req.retryPolicy = DefaultRetryPolicy(TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        MainApplication.instance!!.addToRequestQueue(req, tag = url)
    }


}
