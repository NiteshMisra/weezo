package com.news.weezo.webtask

/**
 * Created by pradeep on 25/05/18.
 * Mail id : pradeep.kumar@inventum.net
 */
interface WebResponseListener {

    fun onResponseReceived(error: String?,action: String, response: String?)
}
