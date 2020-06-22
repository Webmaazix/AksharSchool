package com.akshar.one.viewmodels.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.akshar.one.api.response.ErrorResponse

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
    private var errorResponseMutableLiveData: MutableLiveData<ErrorResponse> = MutableLiveData()

    fun getErrorMutableLiveData(): MutableLiveData<ErrorResponse> = errorResponseMutableLiveData
}