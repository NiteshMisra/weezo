package com.news.weezo.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.news.weezo.R
import com.online.academic.utils.Constants
import kotlinx.android.synthetic.main.activity_contact_us.*


open  class ContactUSPage: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)
        tv_web.setOnClickListener {
            val url = Constants.URL_NEWS
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)

        }
        tv_phone.setOnClickListener {
            val phone = "+91 7827162141"
            val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
            startActivity(intent)
        }
        tv_email.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:weezonews@gmail.com")
            }
            startActivity(Intent.createChooser(emailIntent, "Send Email"))
        }
    }
}