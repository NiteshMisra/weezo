package com.news.weezo.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.news.weezo.MainApplication
import com.news.weezo.R

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splsh)
        getFirebaseInstanceId()
        Handler().postDelayed(Runnable {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2500)


    }


    private fun getFirebaseInstanceId() {
        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w("FirebaseToke", "getInstanceId failed", task.exception)
                        return@OnCompleteListener

                    }
                    // Get new Instance ID token
                    var  firebaseToken  = task.result!!.token
                    MainApplication.instance!!.getPrefs().setNotication(firebaseToken)
                    Log.i("++toke","")
                    // Log and toast

                    //   Toast.makeText(this@LoginActivity, token, Toast.LENGTH_SHORT).show()
                })
    }
}