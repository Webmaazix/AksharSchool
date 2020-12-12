package com.akshar.one.viewmodels.examination

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.akshar.one.model.*
import com.akshar.one.repository.examination.ExaminationRepository
import com.akshar.one.util.AppUtil
import com.akshar.one.viewmodels.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException
import java.lang.Exception

class ExamViewModel(application: Application) : BaseViewModel(application){
    private var examRepository : ExaminationRepository? = null
    private val isLoading = MutableLiveData<Boolean>()
    private val isSuccess = MutableLiveData<Boolean>()
    private var mutuableLiveDataExamList = MutableLiveData<List<ExaminationDropDownModel>>()
    private var mutuableLiveDataExaminationScheduleList = MutableLiveData<ExaminationScheduleList>()
    private var mutableLiveDataCreateStudentProfile = MutableLiveData<StudentListModel>()
    private var mutableLiveDataImage = MutableLiveData<ImageModel>()

    init {
        examRepository = ExaminationRepository()
    }

    fun getIsLoading(): MutableLiveData<Boolean> = isLoading

    fun getExamDropDownLiveData() : MutableLiveData<List<ExaminationDropDownModel>> = mutuableLiveDataExamList
    fun getExamLiveData() : MutableLiveData<ExaminationScheduleList> = mutuableLiveDataExaminationScheduleList
    fun getSuccessLiveData() : MutableLiveData<Boolean> = isSuccess

    fun getImageLiveData() : MutableLiveData<ImageModel> = mutableLiveDataImage


    fun getExaminationDropDown(classRoomId : Int?){
        isLoading.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    isLoading.postValue(false)
                    if(classRoomId == null){
                        val studentList = examRepository?.getExaminationDropDownForParent()
                        studentList.let {
                            mutuableLiveDataExamList.postValue(studentList)
                        }
                    }else{
                        val studentList = examRepository?.getExaminationDropDown(classRoomId)
                        studentList.let {
                            mutuableLiveDataExamList.postValue(studentList)
                        }
                    }


                }catch (httpException : HttpException){
                    isLoading.postValue(false)
                    val errorResponse  = AppUtil.getErrorResponse(httpException.response()?.errorBody()?.string())
                    errorResponse.let { getExamDropDownErrorData().postValue(it) }

                }catch (e : Exception){
                    isLoading.postValue(false)
                    e.printStackTrace()
                }
            }
        }
    }
    fun createExamSchedule(examModel : ExamPostModel){
        isLoading.postValue(true)
        isSuccess.postValue(false)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    isLoading.postValue(false)
                    isSuccess.postValue(true)
                     examRepository?.createExamSchedule(examModel)

                }catch (httpException : HttpException){
                    isLoading.postValue(false)
                    isSuccess.postValue(false)
                    val errorResponse  = AppUtil.getErrorResponse(httpException.response()?.errorBody()?.string())
                    errorResponse.let { getErrorMutableLiveData().postValue(it) }

                }catch (e : Exception){
                    isSuccess.postValue(false)
                    isLoading.postValue(false)
                    e.printStackTrace()
                }
            }
        }
    }
    fun updateExamSchedule(scheduleId : Int,examModel : ExamPostModel){
        isLoading.postValue(true)
        isSuccess.postValue(false)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    isLoading.postValue(false)

                    val data =  examRepository?.updateExamSchedule(scheduleId,examModel)
                    if(data!= null){
                        isSuccess.postValue(true)
                    }else{
                        isSuccess.postValue(false)
                    }


                }catch (httpException : HttpException){
                    isLoading.postValue(false)
                    isSuccess.postValue(false)
                    val errorResponse  = AppUtil.getErrorResponse(httpException.response()?.errorBody()?.string())
                    errorResponse.let { getErrorMutableLiveData().postValue(it) }

                }catch (e : Exception){
                    isSuccess.postValue(false)
                    isLoading.postValue(false)
                    e.printStackTrace()
                }
            }
        }
    }
    fun deleteExamSchedule(scheduleId : Int){
        isLoading.postValue(true)
        isSuccess.postValue(false)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    isLoading.postValue(false)
                    isSuccess.postValue(true)
                     examRepository?.deleteExamSchedule(scheduleId)

                }catch (httpException : HttpException){
                    isLoading.postValue(false)
                    isSuccess.postValue(false)
                    val errorResponse  = AppUtil.getErrorResponse(httpException.response()?.errorBody()?.string())
                    errorResponse.let { getErrorMutableLiveData().postValue(it) }

                }catch (e : Exception){
                    isSuccess.postValue(false)
                    isLoading.postValue(false)
                    e.printStackTrace()
                }
            }
        }
    }

    fun getExaminations(examId : Int,testId : Int){
        isLoading.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    isLoading.postValue(false)
                    var  examList : ExaminationScheduleList? = null
                    if(testId != 0){
                        examList  = examRepository?.getExaminationsTest(testId)
                        examList.let {
                            mutuableLiveDataExaminationScheduleList.postValue(examList)
                        }


                    }else{
                        examList  = examRepository?.getExaminations(examId)
                        examList.let {
                            mutuableLiveDataExaminationScheduleList.postValue(examList)
                        }
                    }


                }catch (httpException : HttpException){
                    isLoading.postValue(false)
                    val errorResponse  = AppUtil.getErrorResponse(httpException.response()?.errorBody()?.string())
                    errorResponse.let { getErrorMutableLiveData().postValue(it) }

                }catch (e : Exception){
                    isLoading.postValue(false)
                    e.printStackTrace()
                }
            }
        }
    }



}