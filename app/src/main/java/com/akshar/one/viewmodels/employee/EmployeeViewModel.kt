package com.akshar.one.viewmodels.employee

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.akshar.one.model.EmployeeList
import com.akshar.one.model.ImageModel
import com.akshar.one.model.StudentListModel
import com.akshar.one.repository.employee.Employeerepository
import com.akshar.one.repository.student.StudentRepository
import com.akshar.one.util.AppUtil
import com.akshar.one.viewmodels.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException
import java.lang.Exception

class EmployeeViewModel(application: Application) : BaseViewModel(application){
    private var employeerepository : Employeerepository? = null
    private val isLoading = MutableLiveData<Boolean>()
    private val isSuccess = MutableLiveData<Boolean>()
    private var mutuableLiveDataEmployeetList = MutableLiveData<List<EmployeeList>>()
    private var mutuableLiveDataStudentProfile = MutableLiveData<StudentListModel>()
    private var mutableLiveDataCreateStudentProfile = MutableLiveData<StudentListModel>()
    private var mutableLiveDataImage = MutableLiveData<ImageModel>()

    init {
        employeerepository = Employeerepository()
    }

    fun getIsLoading(): MutableLiveData<Boolean> = isLoading

    fun getEmployeeListLiveData() : MutableLiveData<List<EmployeeList>> = mutuableLiveDataEmployeetList
    fun getStudentProfileLiveData() : MutableLiveData<StudentListModel> = mutuableLiveDataStudentProfile
    fun getSuccessLiveData() : MutableLiveData<Boolean> = isSuccess

    fun getImageLiveData() : MutableLiveData<ImageModel> = mutableLiveDataImage


    fun getEmployeeList(){
        isLoading.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    isLoading.postValue(false)
                    val studentList = employeerepository?.getEmployeeList()
                    studentList.let {
                        mutuableLiveDataEmployeetList.postValue(studentList)
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

    fun updateEmployeeProfile(studentModel : EmployeeList){
        isLoading.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    isLoading.postValue(false)
                    employeerepository?.updateEmployeeProfile(studentModel)
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

    fun uploadEmployeeImage(employeeId : Int) {
        isLoading.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    isLoading.postValue(false)
                    val model = employeerepository?.uploadEmployeeImage(employeeId)
                    model.let {
                        mutableLiveDataImage.postValue(it)
                    }
                }catch(httpException : HttpException){
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

//    fun CreateEmployeeProfile(employeeModel : EmployeeList){
//        isLoading.postValue(true)
//        viewModelScope.launch {
//            withContext(Dispatchers.IO){
//                try {
//                    isLoading.postValue(false)
//                    employeeId?.CreateStudentProfile(employeeModel)
//                    isSuccess.postValue(true)
//                }catch (httpException : HttpException){
//                    isLoading.postValue(false)
//                    val errorResponse  = AppUtil.getErrorResponse(httpException.response()?.errorBody()?.string())
//                    errorResponse?.let { getErrorMutableLiveData().postValue(it) }
//                }catch (e : Exception){
//                    e.printStackTrace()
//                }
//            }
//        }
//    }
}