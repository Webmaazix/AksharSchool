package com.akshar.one.viewmodels.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.akshar.one.api.response.ErrorResponse

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
    private var errorResponseMutableLiveData: MutableLiveData<ErrorResponse> = MutableLiveData()
    private var collectionErrorResponseMutableLiveData: MutableLiveData<ErrorResponse> = MutableLiveData()
    private var expenseErrorResponseMutableLiveData: MutableLiveData<ErrorResponse> = MutableLiveData()
    private var examErrorResponseMutableLiveData: MutableLiveData<ErrorResponse> = MutableLiveData()
    private var SkillErrorResponseMutableLiveData: MutableLiveData<ErrorResponse> = MutableLiveData()

    fun getErrorMutableLiveData(): MutableLiveData<ErrorResponse> = errorResponseMutableLiveData
    fun getSkillErrorMutableLiveData(): MutableLiveData<ErrorResponse> = SkillErrorResponseMutableLiveData

    fun getCollectionErrorMutableLiveData() : MutableLiveData<ErrorResponse> = collectionErrorResponseMutableLiveData
    fun getExpenseErrorMutableLiveData() : MutableLiveData<ErrorResponse> = expenseErrorResponseMutableLiveData
    fun getExamDropDownErrorData() : MutableLiveData<ErrorResponse> = examErrorResponseMutableLiveData
}