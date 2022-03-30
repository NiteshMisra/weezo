package com.news.weezo.activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.news.weezo.BuildConfig
import com.news.weezo.R
import kotlinx.android.synthetic.main.activity_about_us.*

class AboutUsActivity : AppCompatActivity() {
    var toolbar: Toolbar? = null
    var tvPrivacy: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)
        tvPrivacy = findViewById<View>(R.id.tv_privacy) as TextView?
        val content = SpannableString(getString(R.string.privacy_policy))
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        tvPrivacy!!.text = content
        tvPrivacy!!.setOnClickListener {
            val url = "https://sites.google.com/view/shprivacypolicies/home"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
        (findViewById<View>(R.id.ll_rate_5stars) as LinearLayout).setOnClickListener { rateApp(this@AboutUsActivity) }
        (findViewById<View>(R.id.ll_invite_friends) as LinearLayout).setOnClickListener { shareApp() }
        (findViewById<View>(R.id.ll_feedback) as LinearLayout).setOnClickListener { sendFeedback() }
        (findViewById<View>(R.id.ll_about_us) as LinearLayout).setOnClickListener { aboutUs(this@AboutUsActivity) }
        (findViewById<View>(R.id.tv_version) as TextView).setText(getString(R.string.version).toString() + ":" + BuildConfig.VERSION_NAME)
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar?
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayShowTitleEnabled(false);
      //  getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        if (Build.VERSION.SDK_INT >= 21) {
            val window: Window = this.getWindow()
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.getResources().getColor(R.color.black)
        }
        (findViewById<View>(R.id.img_back) as ImageView).setOnClickListener {
            finish()
        }
    }
    private fun setMenuClickListener(toolbar: Toolbar) = with(toolbar) {
        setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == android.R.id.home) {
                finish()
                return@setOnMenuItemClickListener true
            }
            //this is a lambda so it can be just false,
            //added return to make it explicit
            return@setOnMenuItemClickListener false
        }
    }
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.home -> finish()
//            else -> {
//            }
//        }
//        return true
//    }

    open fun lauchStore(packageName: String) {
        val uri = Uri.parse("market://details?id=$packageName")
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=$packageName")))
        }
    }

    open fun sendFeedback() {
        val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "weezonews@gmail.com", null))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for " + getString(R.string.app_name))
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi Dev,\n I have some errors...\n")
        startActivity(Intent.createChooser(emailIntent, getString(R.string.send_email)))
    }

    open fun aboutUs(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.getString(R.string.about))
        val view: View = LayoutInflater.from(context).inflate(R.layout.dialog_about_us, null)
        builder.setView(view)
        builder.setPositiveButton(R.string.close, null)
        val alertDialog = builder.create()
        alertDialog.show()
        val tvVersion = view.findViewById<View>(R.id.tv_version) as TextView
        val imgEmail = view.findViewById<View>(R.id.img_email) as ImageView
        imgEmail.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.black))
        tvVersion.setText(getString(R.string.version) + BuildConfig.VERSION_NAME)
    }

    private fun shareApp() {
        try {
            val i = Intent(Intent.ACTION_SEND)
            i.type = "text/plain"
            i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
            var sAux = """
            
            ${getString(R.string.share_mgs)}
            
            
            """.trimIndent()
            sAux = """
            ${sAux}https://play.google.com/store/apps/details?id=$packageName
            
            
            """.trimIndent()
            i.putExtra(Intent.EXTRA_TEXT, sAux)
            startActivity(Intent.createChooser(i, getString(R.string.share_via)))
        } catch (e: Exception) {
            //e.toString();
        }
    }


    open fun rateApp(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.getString(R.string.rating))
        val view: View = LayoutInflater.from(context).inflate(R.layout.dialog_rate_us, null)
        builder.setView(view)
        builder.setPositiveButton(getString(R.string.rate), DialogInterface.OnClickListener { dialogInterface, i ->
            val uri = Uri.parse("market://details?id=" + context.packageName)
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            try {
                startActivity(goToMarket)
            } catch (e: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + context.packageName)))
            }
        })
        builder.setNegativeButton(getString(R.string.cancel), null)
        builder.create().show()
    }
}