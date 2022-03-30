package com.online.academic.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color.DKGRAY
import android.graphics.Color.WHITE
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower
import com.news.weezo.MainApplication
import com.news.weezo.R
import com.news.weezo.model.MediaFile
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class Utils {

    companion object {

        fun showProgressDialog(context: Context): ACProgressFlower {
            val dialog =
                    ACProgressFlower.Builder(context).direction(ACProgressConstant.DIRECT_CLOCKWISE)
                            .themeColor(WHITE).fadeColor(DKGRAY).build()
            dialog.setCancelable(false)
            return dialog
        }

        fun setLocale(lang: String) {
            val myLocale = Locale(lang)
            val res = MainApplication.instance!!.getResources()
            val dm = res.getDisplayMetrics()
            val conf = res.getConfiguration()
            conf.locale = myLocale
            res.updateConfiguration(conf, dm)
        }

        fun isValidEmail(target: String): Boolean =
                android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()

        fun isOnline(): Boolean {
            return try {
                val command = "ping -c 1 google.com"
                Runtime.getRuntime().exec(command).waitFor() == 0
            } catch (e: Exception) {
                Log.i("isOnline : ", e.toString())
                false
            }
//            return true
        }

        fun showAlertDialog(activity: Activity, msg: String, tittle: String) {

            val builder = AlertDialog.Builder(activity)
            builder.setTitle(tittle)
            builder.setMessage(msg)
            builder.setCancelable(false)
            builder.setPositiveButton("Ok") { dialog, which ->
                dialog.cancel()
            }
            val alertDialog = builder.create()
            alertDialog.setOnShowListener {
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(activity.resources.getColor(R.color.colorAccent))
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setTextColor(activity.resources.getColor(R.color.colorAccent))
            }
            alertDialog.show()
        }

        fun readableFileSize(size: Long): String? {
            if (size <= 0) return "0"
            val units = arrayOf("kB", "MB", "GB", "TB")
            val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
            return DecimalFormat("#,##0.#").format(size / Math.pow(1024.0, digitGroups.toDouble()))
                    .toString() + " " + units[digitGroups]
        }

        fun getMimeType(url: String?): String? {
            if (url == null) {
                return ""
            }
            var type: String? = ""
            val extension = MimeTypeMap.getFileExtensionFromUrl(url)
            if (extension != null) {
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
            } else {
                type = "url"
            }
            return type
        }

        fun getFormatedDate(dateTime: String): String {
            var df: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:sss")
            val date: Date = df.parse(dateTime)
            // println("date:$date")
            df = SimpleDateFormat("dd-MMM-yyyy")
            System.out.println("Formated Date:" + df.format(date))
            System.out.println("date.getTime" + date.time)
            return df.format(date);
        }

        fun getNewsTypeList(list: ArrayList<MediaFile>, type: String): ArrayList<MediaFile> {
            var listSorted = ArrayList<MediaFile>()

            for (data in list) {
                if (data.type.equals(type)) {
                    listSorted.add(data)
                }
            }
            return listSorted;

        }


        fun shareShortUrl(fileName: String, descriptoin: String, activity: FragmentActivity) {
            /* var dialog =   showProgressDialog(activity)
                val shortLinkTask = Firebase.dynamicLinks.shortLinkAsync {
                    longLink = Uri.parse(fileName)
                }.addOnSuccessListener {
                    var str =
                        "" + it + "\n source : " + descriptoin + "\n I am getting local news very first via below app :  \n" + Constants.URL_APP_STORE


                    dialog.dismiss()
                }.addOnFailureListener {
                    dialog.dismiss()
                }
    */
            sharewithshortUrl(fileName, descriptoin, activity)


        }

        fun sharewithshortUrl(shortUrl: String, description: String, activity: FragmentActivity) {
            var str =
                    "" + shortUrl + "\n source : " + description + "\n I am getting local news very first via below app :  \n" + Constants.URL_APP_STORE

            val share = Intent(Intent.ACTION_SEND)
            share.type = "text/plain"
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)

            // Add data to the intent, the receiving app will decide
            // what to do with it.

            // Add data to the intent, the receiving app will decide
            // what to do with it.
            // share.putExtra(Intent.EXTRA_SUBJECT, "")
            share.setPackage("com.whatsapp")
            share.putExtra(Intent.EXTRA_TEXT, str)
            try {
                activity!!.startActivity(share)
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(activity, "Whatsapp have not been installed.", Toast.LENGTH_LONG).show()
            }
        }
//         fun showVideoView(file: String,context: Context) {
//            if (!file.isNullOrEmpty()) {
//                val intent = Intent(Intent.ACTION_VIEW)
//                intent.setDataAndType(file?.toUri(), "video/*")
//                context.startActivity(intent)
//            } else {
//                MainApplication.instance!!.showToast("No file found")
//            }
//        }
    }
}