package com.akshar.one.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object CheckPermission {
    val REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124
    val RC_LOCATION_PERMISSION = 111
    val REQUEST_CODE_LOCATION = 1
    val REQUEST_LOCATION_PERMISSION = 444
    val REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS2 = 324
    val REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS3 = 322

    fun checkIsMarshMallowVersion(): Boolean {
        val sdkVersion = Build.VERSION.SDK_INT
        return sdkVersion >= Build.VERSION_CODES.M

    }

    fun requestCallPhonePermission(activity: Activity, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: Int) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.CALL_PHONE
            )
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.CALL_PHONE),
                REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS
            )
        } else {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.CALL_PHONE),
                REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS
            )
        }
    }

    /**
     * Used to check whether Contact permission is provided or not.
     */
    fun checkCallPhonePermission(mContext: Context): Boolean {
        val result = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Used to check whether SMS permission is provided or not.
     */
    fun checkSMSPermission(mContext: Context): Boolean {
        val result = ContextCompat.checkSelfPermission(mContext, Manifest.permission.SEND_SMS)
        return result == PackageManager.PERMISSION_GRANTED
    }


    fun checkCameraPermission(mContext: Context): Boolean {
        val result =
            ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val result1 = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA)
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED

    }

    fun checkContactsPermission(mContext: Context): Boolean {
        val result = ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_CONTACTS)
        return result == PackageManager.PERMISSION_GRANTED

    }

    fun requestCameraPermission(activity: Activity, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: Int) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.CAMERA
            )
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
                REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS
            )
        } else {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
                REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS
            )

        }
    }


    fun checkVideoCallPermission(mContext: Context): Boolean {
        val result = ContextCompat.checkSelfPermission(mContext, Manifest.permission.RECORD_AUDIO)
        val result1 = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA)
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
    }

    fun requestVideoCallPermission(
        activity: Activity,
        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS3: Int
    ) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.CAMERA
            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.RECORD_AUDIO
            )
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO),
                REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS3
            )
        } else {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO),
                REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS3
            )

        }
    }

    /*public static void requestCameraPermission(Fragment fragment, int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS) {
        if (fragment.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                || fragment.shouldShowRequestPermissionRationale( Manifest.permission.CAMERA)) {
            fragment.requestPermissions( new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA}, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        } else {
            fragment.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA}, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);

        }
    }*/

    fun requestContactsPermission(activity: Activity, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: Int) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.READ_CONTACTS
            )
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.READ_CONTACTS),
                REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS
            )
        } else {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.READ_CONTACTS),
                REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS
            )
        }
    }

    /*  public static boolean checkLocationPermission(Context mContext) {
        int result = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestLocationPermission(Activity activity, int REQUEST_CODE_LOCATION) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_LOCATION);
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_LOCATION);

        }
    }
*/

    fun checkLocationPermission(mContext: Context): Boolean {
        val result =
            ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
        val result1 =
            ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED

    }

    fun requestLocationPermission(activity: Activity, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: Int) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS
            )
        } else {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS
            )

        }
    }

    /**
     * request location permission
     *
     * @param activity current application context
     */
    fun requestLocationPermission(activity: Activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                CheckPermission.RC_LOCATION_PERMISSION
            )
        } else {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                CheckPermission.RC_LOCATION_PERMISSION
            )
        }
    }
}
