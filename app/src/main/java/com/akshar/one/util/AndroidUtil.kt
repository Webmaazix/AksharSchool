package com.akshar.one.util

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.akshar.one.R

object AndroidUtil {

    fun isInternetAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    fun startActivity(context: Context, cls: Class<*>?, bundle: Bundle? = null) {
        cls?.let {
            val intent = Intent(context, cls)
            bundle?.let { intent.putExtras(it)  }
            context.startActivity(intent)
        }
    }

    fun hideKeyboard(context: Context?, view: View?) {
        context?.let { ctx ->
            view?.let {
                val inputManager =
                    ctx.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(it.windowToken, 0)
            }
        }
    }

    fun showKeyboard(context: Context?, view: View?) {
        context?.let { ctx -> view?.let {
            val inputManager =
                ctx.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.showSoftInput(it, InputMethodManager.SHOW_FORCED)
        } }
    }

    fun showMessageDialog(context: Context?, message: String?) {
        if (context != null && !message.isNullOrEmpty()) {
            val builder = AlertDialog.Builder(context)
            builder.setCancelable(false)
            builder.setPositiveButton(context.getString(R.string.str_ok)) { dialog: DialogInterface, which: Int -> dialog.dismiss() }
            builder.setMessage(message)
            val messageDialog = builder.create()
            messageDialog.setCanceledOnTouchOutside(false)
            messageDialog.show()
        }
    }

    fun showToast(activity: Context?, message: String?, isError: Boolean) {
        if (activity != null) {
            val toast = Toast(activity)

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER.toFloat()
            )
            val textView = TextView(activity)
            textView.setPadding(15, 15, 15, 15)
            textView.setTypeface(textView.typeface, Typeface.BOLD)
            textView.setTextColor(ContextCompat.getColor(activity, R.color.white))
            textView.text = message
            if (isError)
                textView.setBackgroundColor(ContextCompat.getColor(activity, R.color.red))
            else
                textView.setBackgroundColor(ContextCompat.getColor(activity, R.color.green_normal))
            textView.layoutParams = params

            toast.view = textView
            toast.duration = Toast.LENGTH_LONG
            toast.show()
        }
    }

}