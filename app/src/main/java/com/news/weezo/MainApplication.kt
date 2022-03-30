package com.news.weezo

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.os.StrictMode
import android.provider.Settings
import android.text.TextUtils
import android.widget.Toast
import com.advocate.articles.webServices.VolleyRequestKotlin
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.news.weezo.database.AppPrefs


/**
 * Created by pradeep on 16/05/18.
Mail id : pradeep.kumar@inventum.net
 */
open class MainApplication : Application() {

    private var mRequestQueue: RequestQueue? = null

    private val TAG = MainApplication::class.java
    private var prefs: AppPrefs? = null
    companion object {
        var instance: MainApplication? = null
    }

    private var volleyRequest: VolleyRequestKotlin? = null


    override fun onCreate() {
        super.onCreate()
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        instance = this
        getUniqueAndroidDeviceId()
        
    }

    private var gson: Gson? = null
    fun gsonLib(): Gson {
        if (gson == null) {
            gson = Gson()
        }
        return gson!!
    }


    fun getVolleyRequest(): VolleyRequestKotlin? {
        if (volleyRequest == null) {
            volleyRequest = VolleyRequestKotlin()
        }
        return volleyRequest
    }

    fun showToast(message: String) {
        Toast.makeText(instance, message, Toast.LENGTH_SHORT).show()
    }

    fun isNetworkAvailable(): Boolean {
        val connectivityManager = instance!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun getRequestQueue(): RequestQueue {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(applicationContext)
        }
        return mRequestQueue!!
    }

    fun <T> addToRequestQueue(req: Request<T>, tag: String) {
        req.tag = if (TextUtils.isEmpty(tag)) TAG else tag
        getRequestQueue().add(req)
    }

    fun <T> addToRequestQueue(req: Request<T>) {
        req.tag = TAG
        getRequestQueue().add(req)
    }

    fun cancelPendingRequests(tag: Any) {
        if (mRequestQueue != null) {
            mRequestQueue!!.cancelAll(tag)
        }
    }
    fun getPrefs(): AppPrefs {
        if (prefs == null) prefs = AppPrefs()
        return prefs!!
    }
    fun getUniqueAndroidDeviceId(): String? {
        val android_id = Settings.Secure.getString(applicationContext.contentResolver,
                Settings.Secure.ANDROID_ID)
        getPrefs().setUserToken(android_id)
        return android_id
    }

}