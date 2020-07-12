package com.akshar.one.viewmodels.student

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.akshar.one.model.ImageModel
import com.akshar.one.model.StudentListModel
import com.akshar.one.repository.student.StudentRepository
import com.akshar.one.util.AppUtil
import com.akshar.one.viewmodels.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException
import java.lang.Exception

class StudentViewModel(application: Application) : BaseViewModel(application){
    private var studentRepository : StudentRepository? = null
    private val isLoading = MutableLiveData<Boolean>()
    private val isSuccess = MutableLiveData<Boolean>()
    private var mutuableLiveDataStudentList = MutableLiveData<List<StudentListModel>>()
    private var mutableLiveDataCreateStudentProfile = MutableLiveData<StudentListModel>()
    private var mutableLiveDataImage = MutableLiveData<ImageModel>()

    init {
        studentRepository = StudentRepository()
    }

    fun getIsLoading(): MutableLiveData<Boolean> = isLoading

    fun getStudentListLiveData() : MutableLiveData<List<StudentListModel>> = mutuableLiveDataStudentList
    fun getSuccessLiveData() : MutableLiveData<Boolean> = isSuccess

    fun getImageLiveData() : MutableLiveData<ImageModel> = mutableLiveDataImage


    fun getStudentList(classRoomId : Int){
        isLoading.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    isLoading.postValue(false)
                    val studentList = studentRepository?.getStudentListByClassId(classRoomId)
                    studentList.let {
                        mutuableLiveDataStudentList.postValue(studentList?.list)
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

    fun updateStudentProfile(studentId : Int,studentModel : StudentListModel){
        isLoading.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    isLoading.postValue(false)
                    studentRepository?.UpdateStudentProfile(studentId,studentModel)
                    isSuccess.postValue(true)
                }catch (httpException : HttpException){
                    isLoading.postValue(false)
                    val errorResponse = AppUtil.getErrorResponse(httpException.response()?.errorBody()?.string())
                    errorResponse.let {
                        getErrorMutableLiveData().postValue(it)
                    }
                }catch (e : Exception){
                    e.printStackTrace()
                    isLoading.postValue(false)
                }
            }
        }
    }

    fun uploadImage(studentId: Int) {
        isLoading.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    isLoading.postValue(false)
                    val model = studentRepository?.uploadImage(studentId)
                    model.let {
                        mutableLiveDataImage.postValue(it)


                    }

                }catch (httpException : HttpException){
                    isLoading.postValue(false)
                    val errorResponse = AppUtil.getErrorResponse(httpException.response()?.errorBody()?.string())
                    errorResponse.let {
                        getErrorMutableLiveData().postValue(it)
                    }
                }catch (e : Exception){
                    isLoading.postValue(false)
                    e.printStackTrace()
                }
            }
        }
    }

    fun CreateStudentProfile(studentModel : StudentListModel){
        isLoading.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    isLoading.postValue(false)
                    studentRepository?.CreateStudentProfile(studentModel)
                    isSuccess.postValue(true)
                }catch (httpException : HttpException){
                    isLoading.postValue(false)
                    val errorResponse  = AppUtil.getErrorResponse(httpException.response()?.errorBody()?.string())
                    errorResponse?.let { getErrorMutableLiveData().postValue(it) }
                }catch (e : Exception){
                    e.printStackTrace()
                }
            }
        }
    }
}