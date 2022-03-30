package com.news.weezo.dialog

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.news.weezo.R
import com.news.weezo.utils.AppConfigs


class DialogRating(context: Context) : AlertDialog(context), View.OnClickListener {
    private var ivRate_star_1: ImageView? = null
    private var ivRate_star_2: ImageView? = null
    private var ivRate_star_3: ImageView? = null
    private var ivRate_star_4: ImageView? = null
    private var ivRate_star_5: ImageView? = null
    private var btnAsk: TextView?= null;
    private var btnRemid: TextView? = null;
  //  private var rootView: View? = null
    init {
        setCancelable(true);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_rating_app)
        ivRate_star_1 = findViewById<View>(R.id.iv_start_1) as ImageView
        ivRate_star_2 = findViewById<View>(R.id.iv_start_2) as ImageView
        ivRate_star_3 = findViewById<View>(R.id.iv_start_3) as ImageView
        ivRate_star_4 = findViewById<View>(R.id.iv_start_4) as ImageView
        ivRate_star_5 = findViewById<View>(R.id.iv_start_5) as ImageView
        btnAsk = findViewById<View>(R.id.btn_ask) as TextView
        btnRemid = findViewById<View>(R.id.btn_remid) as TextView
        ivRate_star_1!!.setOnClickListener(this)
        ivRate_star_2!!.setOnClickListener(this)
        ivRate_star_3!!.setOnClickListener(this)
        ivRate_star_4!!.setOnClickListener(this)
        ivRate_star_5!!.setOnClickListener(this)
        btnAsk!!.setOnClickListener(this)
        btnRemid!!.setOnClickListener(this)
    }

    fun launchMarket() {
        try {
            val marketUri: Uri = Uri.parse("market://details?id=" + context!!.packageName)
            val marketIntent = Intent(Intent.ACTION_VIEW, marketUri)
            context!!.startActivity(marketIntent)
        } catch (e: ActivityNotFoundException) {
            val marketUri: Uri = Uri.parse("https://play.google.com/store/apps/details?id=" + context!!.packageName)
            val marketIntent = Intent(Intent.ACTION_VIEW, marketUri)
            context!!.startActivity(marketIntent)
        }
    }
    fun sendFeedback() {
        Toast.makeText(context, "Thanks you!", Toast.LENGTH_LONG).show()
      //  exitApp()
    }

    override fun onClick(view: View) {
        context!!.getSharedPreferences(AppConfigs.MY_FILE_DATA, 0).edit().putBoolean(AppConfigs.RATED_IN_STORE, true).apply()
        when (view.id) {
            R.id.iv_start_1 -> {
                ivRate_star_1!!.setImageResource(R.drawable.ic_star_true)
                ivRate_star_2!!.setImageResource(R.drawable.ic_star_false)
                ivRate_star_3!!.setImageResource(R.drawable.ic_star_false)
                ivRate_star_4!!.setImageResource(R.drawable.ic_star_false)
                ivRate_star_5!!.setImageResource(R.drawable.ic_star_false)
                sendFeedback()
                dismiss()
                return
            }
            R.id.iv_start_2 -> {
                ivRate_star_1!!.setImageResource(R.drawable.ic_star_true)
                ivRate_star_2!!.setImageResource(R.drawable.ic_star_true)
                ivRate_star_3!!.setImageResource(R.drawable.ic_star_false)
                ivRate_star_4!!.setImageResource(R.drawable.ic_star_false)
                ivRate_star_5!!.setImageResource(R.drawable.ic_star_false)
                sendFeedback()
                dismiss()
                return
            }
            R.id.iv_start_3 -> {
                ivRate_star_1!!.setImageResource(R.drawable.ic_star_true)
                ivRate_star_2!!.setImageResource(R.drawable.ic_star_true)
                ivRate_star_3!!.setImageResource(R.drawable.ic_star_true)
                ivRate_star_4!!.setImageResource(R.drawable.ic_star_false)
                ivRate_star_5!!.setImageResource(R.drawable.ic_star_false)
                sendFeedback()
                dismiss()
                return
            }
            R.id.iv_start_4 -> {
                ivRate_star_1!!.setImageResource(R.drawable.ic_star_true)
                ivRate_star_2!!.setImageResource(R.drawable.ic_star_true)
                ivRate_star_3!!.setImageResource(R.drawable.ic_star_true)
                ivRate_star_4!!.setImageResource(R.drawable.ic_star_true)
                ivRate_star_5!!.setImageResource(R.drawable.ic_star_false)
               // sendFeedback()
                launchMarket()
                dismiss()
                return
            }
            R.id.iv_start_5 -> {
                ivRate_star_1!!.setImageResource(R.drawable.ic_star_true)
                ivRate_star_2!!.setImageResource(R.drawable.ic_star_true)
                ivRate_star_3!!.setImageResource(R.drawable.ic_star_true)
                ivRate_star_4!!.setImageResource(R.drawable.ic_star_true)
                ivRate_star_5!!.setImageResource(R.drawable.ic_star_true)
                launchMarket()
                dismiss()
                return
            }
            R.id.btn_ask->{
                context.getSharedPreferences(AppConfigs.MY_FILE_DATA, 0).edit().putBoolean(AppConfigs.RATED_IN_STORE, true).apply()
                dismiss()
                return
            }
            R.id.btn_remid->{
                dismiss()
            }
            else -> return
        }
    }
}