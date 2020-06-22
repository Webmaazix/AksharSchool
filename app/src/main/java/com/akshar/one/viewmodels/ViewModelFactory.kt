package com.akshar.one.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.akshar.one.viewmodels.attendance.AttendanceClassRoomViewModel
import com.akshar.one.viewmodels.attendance.AttendanceCourseViewModel
import com.akshar.one.viewmodels.login.LoginViewModel
import com.akshar.one.viewmodels.login.LoginWithPhoneViewModel
import com.akshar.one.viewmodels.login.OTPViewModel

class ViewModelFactory(private val application: Application) :
    ViewModelProvider.AndroidViewModelFactory(application) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        when (modelClass) {
            LoginViewModel::class.java -> return LoginViewModel(application) as T
            LoginWithPhoneViewModel::class.java -> return LoginWithPhoneViewModel(application) as T
            OTPViewModel::class.java -> return OTPViewModel(application) as T
            AttendanceCourseViewModel::class.java -> return AttendanceCourseViewModel(application) as T
            AttendanceClassRoomViewModel::class.java -> return AttendanceClassRoomViewModel(application) as T
        }

        return super.create(modelClass)
    }
}