package com.akshar.one.viewmodels.timetable

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.akshar.one.model.ClassDropDownModel
import com.akshar.one.repository.timetable.TimeTableRepository
import com.akshar.one.util.AppUtil
import com.akshar.one.viewmodels.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.lang.Exception


class TimeTableViewModel(application: Application) : BaseViewModel(application){

    private var timeTableRepository : TimeTableRepository? = null
    private val isLoading = MutableLiveData<Boolean>()
    private var mutableClassDropDownData = MutableLiveData<List<ClassDropDownModel>>()

    init {
        timeTableRepository = TimeTableRepository()
    }

    fun getIsLoading() : MutableLiveData<Boolean> = isLoading

    fun getClassRoomLiveData() : MutableLiveData<List<ClassDropDownModel>> =
        mutableClassDropDownData


    fun getClassRoomDropdown(){
        isLoading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    isLoading.postValue(false)
                    val list = timeTableRepository?.getClassroomDropdown()
                    list?.let { mutableClassDropDownData.postValue(it) }

                }catch (httpException : HttpException){
                    isLoading.postValue(false)
                    val errorResponse = AppUtil.getErrorResponse(httpException.response()?.errorBody()?.string())
                    errorResponse?.let { getErrorMutableLiveData().postValue(it) }

                }catch (e : Exception){
                    isLoading.postValue(false)
                    e.printStackTrace()
                }
            }
        }
    }
}

