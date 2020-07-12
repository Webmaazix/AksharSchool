package com.akshar.one.util


import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.os.Build
import android.os.Environment
import android.provider.Settings

import android.text.Html
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import com.akshar.one.R

import com.squareup.picasso.Picasso
import java.io.File



import java.text.SimpleDateFormat
import java.util.*


object AppUtils {

    @kotlin.jvm.JvmStatic
    var serverUTCDateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    @kotlin.jvm.JvmStatic
    var serverChatUTCDateTimeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

    var serverDateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    var targetDate = SimpleDateFormat("MM/dd/yyyy")
    var displayDateTimeformat = SimpleDateFormat("dd MMM, hh:mm a")

    var eventMonthDateFormat = SimpleDateFormat("MMM \n dd")
    var eventDayTimeFormat = SimpleDateFormat("EEE hh:mm a")

    var newsDayTimeFormat = SimpleDateFormat("EEE dd MMM, yy 'at' hh:mm a")

    @kotlin.jvm.JvmStatic
    var groupDateFormat = SimpleDateFormat("dd MMMM yyyy\nhh:mm a")

    var postDateTimeFormat = SimpleDateFormat("EEE dd MMM, yy 'at' hh:mm a")
    @kotlin.jvm.JvmStatic
    var transcationDate = SimpleDateFormat("MMM dd, yyyy")

    @kotlin.jvm.JvmStatic
    var transcationDateTime = SimpleDateFormat("MMM dd, yyyy=hh:mm a")

    init {
        serverUTCDateTimeFormat.timeZone = TimeZone.getTimeZone("UTC")
        serverChatUTCDateTimeFormat.timeZone = TimeZone.getTimeZone("UTC")

        serverDateTimeFormat.timeZone = TimeZone.getDefault()
        targetDate.timeZone = TimeZone.getDefault()
        displayDateTimeformat.timeZone = TimeZone.getDefault()
        eventMonthDateFormat.timeZone = TimeZone.getDefault()
        eventDayTimeFormat.timeZone = TimeZone.getDefault()
        newsDayTimeFormat.timeZone = TimeZone.getDefault()
        groupDateFormat.timeZone = TimeZone.getDefault()
        postDateTimeFormat.timeZone = TimeZone.getDefault()
    }


    fun isEmpty( value:String):Boolean{
        if(value==null){
            return true;
        }else if(value.length==0){
            return true;
        }else{
            return false;
        }
    }


    fun hideProgress(overlayDialog: Dialog) {
        if (overlayDialog.isShowing) {
            overlayDialog.dismiss()
        }
    }

    fun hideKeyboard(activity: Activity?) {
        if (activity != null && activity.currentFocus != null && activity.currentFocus!!.windowToken != null) {
            val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            try {
                inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
            } catch (ignored: NullPointerException) {

            }
        }
    }


      @JvmStatic
     public fun convertDateFormat(dateTimeString: String?, originalFormat: SimpleDateFormat, targetFormat: SimpleDateFormat): String {
        if (dateTimeString == null || dateTimeString.equals("null", ignoreCase = true))
            return ""

        var targetDateString = dateTimeString
        try {
            val originalDate = originalFormat.parse(dateTimeString)
            val originalDateString = originalFormat.format(originalDate)
            val targetDate = originalFormat.parse(originalDateString)
            targetDateString = targetFormat.format(targetDate)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            return targetDateString!!
        }
    }

    fun isValidFormat(dateTimeString: String?, format: SimpleDateFormat): Boolean {
        return try {
            format.parse(dateTimeString)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun convertNullToEmpty(vararg texts: String?): String {
        var text = ""
        texts
                .filterNotNull()
                .forEach { text += it }
        return text.trim { it <= ' ' }
    }

    const val IMAGE_TYPE_CITIES = "Cities"
    const val IMAGE_TYPE_CITIES_AIRPOT = "CityAirports"
    const val IMAGE_TYPE_CITIES_RILWAY = "CityRailways"
    const val IMAGE_TYPE_CITIES_BUSTAND = "CityBusTerminals"
    const val IMAGE_TYPE_USER_IMAGE = "UserProfileImg"
    const val IMAGE_TYPE_CITY_CATEGORY = "CitiesCategories"
    const val IMAGE_TYPE_ADS = "addresImages"

    const val AWS_IMAGE = "awsImage"



    fun getOutPutMediaFile(context: Context) : File?{

        val mediaStorageDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                context.getString(R.string.app_name))
        if(!mediaStorageDir.exists()){
            if(!mediaStorageDir.mkdir()){
                return null
            }
        }

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        return  File(mediaStorageDir.path+File.separator+"IMG_"+timeStamp+".jpg")
    }


    fun loadImageCrop(url: String?, imageView: ImageView, placeHolder: Int, targetHeight: Int, targetWidth: Int) {
        //        System.out.println("IMAGE URL IS= " + url);
        if (url != null && !url.isEmpty() && !url.equals("null", ignoreCase = true)) {
            Picasso.get().load(url).resize(targetWidth, targetHeight).centerCrop().placeholder(placeHolder).into(imageView)
        } else {
            Picasso.get().load(placeHolder).resize(targetWidth, targetHeight).centerCrop().into(imageView)
        }
    }

    fun loadImageInside(url: String?, imageView: ImageView, placeHolder: Int, targetHeight: Int, targetWidth: Int) {
        //        System.out.println("IMAGE URL IS= " + url);
        if (url != null && !url.isEmpty() && !url.equals("null", ignoreCase = true)) {
            Picasso.get().load(url).resize(targetWidth, targetHeight).centerInside().placeholder(placeHolder).into(imageView)
        } else {
            Picasso.get().load(placeHolder).resize(targetWidth, targetHeight).centerInside().into(imageView)
        }
    }


    fun setText(textView: TextView, text: String?) {
        if (text != null && !text.equals("null", ignoreCase = true)) {
            textView.text = text
        } else {
            textView.text = ""
        }
    }

    fun setText(textView: TextView, text: String?, defaultText: String) {
        if (text != null && !text.isEmpty() && !text.equals("null", ignoreCase = true)) {
            textView.text = text
        } else {
            textView.text = defaultText
        }
    }


    fun setText(textView: EditText, text: String?) {
        if (text != null && !text.equals("null", ignoreCase = true)) {
            textView.setText(text)
        } else {
            textView.setText("")
        }
    }


    fun setText(textView: EditText, text: Double?) {
        if (text != null) {
            textView.setText(text.toString())
        } else {
            textView.setText("")
        }
    }


    fun setTexts(textView: TextView, vararg texts: String) {
        var text = ""
        for (test in texts) {
            if (test != null && !test.equals("null", ignoreCase = true))
                text += test
            else
                text = ""
        }

        text = text.replace(", ,".toRegex(), ",")
        textView.text = Html.fromHtml(text.trim { it <= ' ' })
    }

    fun convertToString(value: Any): String {
        return if (value == 0 || value == 0L || value == 0.0 || value == 0f)
            ""
        else
            value.toString()
    }

    fun showToast(activity: Activity?, message: String, isError: Boolean) {
        if (activity != null) {
            val toast = Toast(activity)

            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER.toFloat())
            val textView = TextView(activity)
            textView.setPadding(15, 15, 15, 15)
            textView.setTypeface(textView.typeface, Typeface.BOLD)
            textView.setTextColor(activity.resources.getColor(R.color.white))
            textView.text = message
            if (isError)
                textView.setBackgroundColor(activity.resources.getColor(R.color.red))
            else
                textView.setBackgroundColor(activity.resources.getColor(R.color.colorAccent))
            textView.layoutParams = params

            toast.view = textView
            toast.duration = Toast.LENGTH_LONG
            toast.show()
        }
    }

    private var dialog: Dialog? = null

    private fun dialogInternet(activity: Activity, fragment: Fragment?, requestCode: Int) {
        if (dialog != null && dialog!!.isShowing)
            dialog!!.dismiss()

        val ad = AlertDialog.Builder(activity)
        ad.setTitle(activity.getString(R.string.noConnection))
        ad.setMessage(activity.getString(R.string.turnOnInternet))
        //        ad.setCancelable(false);
        ad.setNegativeButton(activity.getString(R.string.mobileData)) { dialog, which ->
            val i = Intent(Settings.ACTION_DATA_ROAMING_SETTINGS)
            if (fragment == null) {
                activity.startActivityForResult(i, requestCode)
            } else {
                fragment.startActivityForResult(i, requestCode)
            }
        }
        ad.setPositiveButton(activity.getString(R.string.Wifi)) { dialog, which ->
            val i = Intent(Settings.ACTION_WIFI_SETTINGS)
            if (fragment == null) {
                activity.startActivityForResult(i, requestCode)
            } else {
                fragment.startActivityForResult(i, requestCode)
            }
        }
        dialog = ad.show()
    }

    fun isInternetOn(activity: Activity, fragment: Fragment?, requestCode: Int): Boolean {

        var flag = false
        // get Connectivity Manager object to check connection
        val connec = activity.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

        if (connec.getNetworkInfo(0).state == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(1).state == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(0).state == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).state == android.net.NetworkInfo.State.CONNECTED) {

            flag = true

        } else if (connec.getNetworkInfo(0).state == android.net.NetworkInfo.State.DISCONNECTED || connec.getNetworkInfo(1).state == android.net.NetworkInfo.State.DISCONNECTED) {

            dialogInternet(activity, fragment, requestCode)
            flag = false
        }
        return flag
    }

    fun convertDpToPixel(dp: Float, context: Context): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    @SuppressLint("NewApi")
    fun isAppIsInBackground(context: Context): Boolean {
        var isInBackground = true
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.DONUT) {
            val runningProcesses = am.runningAppProcesses
            try {
                for (processInfo in runningProcesses) {
                    if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        for (activeProcess in processInfo.pkgList) {
                            if (activeProcess == context.packageName) {
                                isInBackground = false
                            }
                        }
                    }
                }
            } catch (e: Exception) {
            }

        } else {
            try {
                val taskInfo = am.getRunningTasks(1)
                val componentInfo = taskInfo[0].topActivity
                if (componentInfo?.packageName == context.packageName) {
                    isInBackground = false
                }
            } catch (e: Exception) {
            }

        }
        return isInBackground
    }


}
