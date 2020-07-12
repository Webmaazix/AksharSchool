package com.akshar.one.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.media.ExifInterface
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager

import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

object CommonUtils {
    private val TAG = "CommonUtils"
    private val mongoDateTimeFormat =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
    private val displayDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
    private val displayDateTimeFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)

    val filename: String
        get() {
            val file = File(Environment.getExternalStorageDirectory().path, "MyFolder/Images")
            if (!file.exists()) {
                file.mkdirs()
            }
            return file.absolutePath + "/" + System.currentTimeMillis() + ".jpg"

        }


    //================Days Ago==============================//
    private val SECOND_MILLIS = 1000
    private val MINUTE_MILLIS = 60 * SECOND_MILLIS
    private val HOUR_MILLIS = 60 * MINUTE_MILLIS
    private val DAY_MILLIS = 24 * HOUR_MILLIS


    fun isOnline(mContext: Context): Boolean {
        val connectivity =
            mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivity != null) {
            val info = connectivity.allNetworkInfo
            if (info != null)
                for (i in info.indices)
                    if (info[i].state == NetworkInfo.State.CONNECTED) {
                        return true
                    }
        }
        return false
    }

    fun getUTCTime(timeStr: String): String {
        mongoDateTimeFormat.timeZone = TimeZone.getTimeZone("UTC")
        try {
            val myDate = mongoDateTimeFormat.parse(timeStr)
            displayDateTimeFormat.timeZone = TimeZone.getDefault()
            return if (myDate != null) {
                displayDateTimeFormat.format(myDate)
            } else {
                timeStr
            }


        } catch (e: ParseException) {
            e.printStackTrace()
            return timeStr
        }

    }

    fun hideKeyboard(activity: Activity) {
        try {
            val `in` = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val view = activity.findViewById<View>(android.R.id.content)
            `in`.hideSoftInputFromWindow(
                view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        } catch (e: Throwable) {
            e.printStackTrace()
        }

    }

    fun startActivity(
        activity: Activity,
        clazz: Class<*>,
        isClearPrevious: Boolean,
        isClearStack: Boolean
    ) {
        val intent = Intent(activity, clazz)
        activity.startActivity(intent)
        if (isClearStack && isClearPrevious || isClearStack)
            ActivityCompat.finishAffinity(activity)
        if (isClearPrevious)
            activity.finish()
    }

    fun startActivityWithBundle(
        activity: Activity,
        clazz: Class<*>,
        bundle: Bundle,
        isClearPrevious: Boolean
    ) {
        val intent = Intent(activity, clazz)
        intent.putExtras(bundle)
        activity.startActivity(intent)
        if (isClearPrevious)
            activity.finish()
    }


    fun setFragment(
        fragment: Fragment,
        removeStack: Boolean,
        activity: FragmentActivity,
        mContainer: Int
    ) {
        val fragmentManager = activity.supportFragmentManager
        val ftTransaction = fragmentManager.beginTransaction()
        if (removeStack) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            ftTransaction.replace(mContainer, fragment)
        } else {
            ftTransaction.replace(mContainer, fragment)
            ftTransaction.addToBackStack(null)
        }
        ftTransaction.commit()
    }

    fun setFragmentWithBundle(
        fragment: Fragment,
        bundle: Bundle,
        removeStack: Boolean,
        activity: FragmentActivity,
        mContainer: Int
    ) {
        val fragmentManager = activity.supportFragmentManager
        val ftTransaction = fragmentManager.beginTransaction()
        if (removeStack) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            ftTransaction.replace(mContainer, fragment)
            fragment.arguments = bundle
        } else {
            ftTransaction.replace(mContainer, fragment)
            ftTransaction.addToBackStack(null)
            fragment.arguments = bundle
        }
        ftTransaction.commit()
    }

    fun setFragmentWithBundle2(
        fragment: Fragment,
        bundle: Bundle,
        removeStack: Boolean,
        fragmentManager: FragmentManager,
        mContainer: Int
    ) {
        //FragmentManager fragmentManager = activity.getSupportFragmentManager();
        val ftTransaction = fragmentManager.beginTransaction()
        if (removeStack) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            ftTransaction.replace(mContainer, fragment)
            fragment.arguments = bundle
        } else {
            ftTransaction.replace(mContainer, fragment)
            ftTransaction.addToBackStack(null)
            fragment.arguments = bundle
        }
        ftTransaction.commit()
    }


    fun compressImage(imageUri: String, activity: Activity): String {
        val filePath = getRealPathFromURI(imageUri, activity)
        var scaledBitmap: Bitmap? = null
        val options = BitmapFactory.Options()
        //      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
        //      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true
        var bmp = BitmapFactory.decodeFile(filePath, options)

        var actualHeight = options.outHeight
        var actualWidth = options.outWidth

        //      max Height and width values of the compressed image is taken as 816x612

        val maxHeight = 816.0f
        val maxWidth = 612.0f
        //try {
        var imgRatio = (actualWidth / actualHeight).toFloat()
        val maxRatio = maxWidth / maxHeight
        /*}catch (ArithmeticException e){
            e.printStackTrace();
            Log.d(TAG, "compressImage: ");
        }*/

        //      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight
                actualWidth = (imgRatio * actualWidth).toInt()
                actualHeight = maxHeight.toInt()
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth
                actualHeight = (imgRatio * actualHeight).toInt()
                actualWidth = maxWidth.toInt()
            } else {
                actualHeight = maxHeight.toInt()
                actualWidth = maxWidth.toInt()

            }
        }

        //      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)

        //      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false

        //      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true
        options.inInputShareable = true
        options.inTempStorage = ByteArray(16 * 1024)

        try {
            //          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()

        }

        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }

        val ratioX = actualWidth / options.outWidth.toFloat()
        val ratioY = actualHeight / options.outHeight.toFloat()
        val middleX = actualWidth / 2.0f
        val middleY = actualHeight / 2.0f

        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)

        val canvas = Canvas(scaledBitmap!!)
        canvas.setMatrix(scaleMatrix)
        canvas.drawBitmap(
            bmp,
            middleX - bmp.width / 2,
            middleY - bmp.height / 2,
            Paint(Paint.FILTER_BITMAP_FLAG)
        )

        //      check the rotation of the image and display it properly
        val exif: ExifInterface
        try {
            exif = ExifInterface(filePath!!)

            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION, 0
            )
            Log.d("EXIF", "Exif: $orientation")
            val matrix = Matrix()
            if (orientation == 6) {
                matrix.postRotate(90f)
                Log.d("EXIF", "Exif: $orientation")
            } else if (orientation == 3) {
                matrix.postRotate(180f)
                Log.d("EXIF", "Exif: $orientation")
            } else if (orientation == 8) {
                matrix.postRotate(270f)
                Log.d("EXIF", "Exif: $orientation")
            }
            scaledBitmap = Bitmap.createBitmap(
                scaledBitmap, 0, 0,
                scaledBitmap.width, scaledBitmap.height, matrix,
                true
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }

        var out: FileOutputStream? = null
        val filename = filename
        try {
            out = FileOutputStream(filename)

            //          write the compressed bitmap at the destination specified by filename.
            scaledBitmap!!.compress(Bitmap.CompressFormat.JPEG, 80, out)

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        return filename

    }

    private fun getRealPathFromURI(contentURI: String, activity: Activity): String? {
        val contentUri = Uri.parse(contentURI)
        val cursor = activity.contentResolver.query(contentUri, null, null, null, null)
        if (cursor == null) {
            return contentUri.path
        } else {
            cursor.moveToFirst()
            val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            return cursor.getString(index)
        }
    }

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        val totalPixels = (width * height).toFloat()
        val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++
        }

        return inSampleSize
    }

    fun getTimeAgo(time: Long): String? {
        var time = time
        if (time < 1000000000000L) {
            time *= 1000
        }
        val now = System.currentTimeMillis()
        if (time > now || time <= 0) {
            return null
        }
        val diff = now - time
        return if (diff < MINUTE_MILLIS) {
            "just now"
        } else if (diff < 2 * MINUTE_MILLIS) {
            "a minute ago"
        } else if (diff < 50 * MINUTE_MILLIS) {
            (diff / MINUTE_MILLIS).toString() + " minutes ago"
        } else if (diff < 90 * MINUTE_MILLIS) {
            "an hour ago"
        } else if (diff < 24 * HOUR_MILLIS) {
            (diff / HOUR_MILLIS).toString() + " hours ago"
        } else if (diff < 48 * HOUR_MILLIS) {
            "yesterday"
        } else {
            (diff / DAY_MILLIS).toString() + " days ago"
        }
    }


}
