package com.news.weezo.database

import android.content.Context
import android.content.SharedPreferences
import com.news.weezo.BuildConfig
import com.news.weezo.MainApplication

/**
 * Created by pradeep on 16/05/18.
Mail id : pradeep.kumar@inventum.net
 */
class AppPrefs {


    private val editor: SharedPreferences.Editor
    private val preference: SharedPreferences
    private val KEY_TOKEN = "token";
    private val KEY_USER_ID = "userId";
    private val KEY_IS_USER_REGISTERD = "isUserRegistered";


    private val NAME = BuildConfig.APPLICATION_ID

    init {
        preference = MainApplication.instance!!.getSharedPreferences(NAME, Context.MODE_PRIVATE)
        editor = preference.edit()
    }

    fun clearLocalData() {
        preference.edit().clear().commit()
    }

    fun setUserToken(token: String?) {
        editor.putString(KEY_USER_ID, token).apply()
    }

    fun setNotication(token: String?) {
        editor.putString(KEY_TOKEN, token).apply()
    }
    fun setIsUserRegistered(isUserRegi:Boolean){
        editor.putBoolean(KEY_IS_USER_REGISTERD, isUserRegi).apply()
    }

//    fun saveLoginData(profileModel: ProfileModel) {
////        val jsonObject = JSONObject(response).optJSONObject("response").optJSONObject("result");
//        setToken(profileModel.token)
//        setUserName(profileModel.))
////saveBarCategory(jsonObject)
//    }


    fun getIsUserRegistered():Boolean{
        return preference.getBoolean(KEY_IS_USER_REGISTERD, false)
    }
    fun getNotificationToken():String{
        return preference.getString(KEY_TOKEN, "").toString()
    }
    fun getUserToken():String{
        return preference.getString(KEY_USER_ID, "").toString()
    }



}