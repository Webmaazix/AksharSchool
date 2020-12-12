package com.akshar.one.viewmodels.messagecenter

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.akshar.one.model.*
import com.akshar.one.repository.messagecenter.MessageCenterRepository
import com.akshar.one.repository.student.StudentRepository
import com.akshar.one.util.AppUtil
import com.akshar.one.viewmodels.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException
import java.lang.Exception

class MessageCenterViewModel(application: Application) : BaseViewModel(application){
    private var messageRepository : MessageCenterRepository? = null
    private val isLoading = MutableLiveData<Boolean>()
    private val isSuccess = MutableLiveData<Boolean>()
    private var mutuableLiveDataStudentList = MutableLiveData<List<AbsentStudentList>>()
    private var mutuableLiveDataMarksStudentList = MutableLiveData<List<StudentListModel>>()
    private var mutuableLiveDataShiftList = MutableLiveData<List<ShiftList>>()
    private var mutuableLiveDataFeeHeadTermList = MutableLiveData<List<FeeHeadTermList>>()
    private var mutuableLiveDataStudentListModel = MutableLiveData<List<FeeStudentList>>()
    private var mutuableLiveDataAbsentReport = MutableLiveData<Boolean>()
    private var mutuableLiveDataEmployeeDepartment = MutableLiveData<List<EmployeeDepartmentList>>()
    private var mutuableLiveDataEmployeeList = MutableLiveData<List<EmployeeList>>()
    private var mutuableLiveDataSendNotificationData = MutableLiveData<SendGeneralNotification>()


    init {
        messageRepository = MessageCenterRepository()
    }

    fun getIsLoading(): MutableLiveData<Boolean> = isLoading

    fun getStudentListLiveData() : MutableLiveData<List<AbsentStudentList>> = mutuableLiveDataStudentList
    fun getStudentListModelLiveData() : MutableLiveData<List<FeeStudentList>> = mutuableLiveDataStudentListModel
    fun getMarksStudentListLiveData() : MutableLiveData<List<StudentListModel>> = mutuableLiveDataMarksStudentList
    fun getShiftListLiveData() : MutableLiveData<List<ShiftList>> = mutuableLiveDataShiftList
    fun getFeeHeadTermListLiveData() : MutableLiveData<List<FeeHeadTermList>> = mutuableLiveDataFeeHeadTermList
    fun getAbsentReportLiveData() : MutableLiveData<Boolean> = mutuableLiveDataAbsentReport
    fun getEmployeeDepartmentLiveData() : MutableLiveData<List<EmployeeDepartmentList>> = mutuableLiveDataEmployeeDepartment
    fun getEmployeeListLiveData() : MutableLiveData<List<EmployeeList>> = mutuableLiveDataEmployeeList
    fun getSentNotificationLiveData() : MutableLiveData<SendGeneralNotification> = mutuableLiveDataSendNotificationData
    fun getSuccessLiveData() : MutableLiveData<Boolean> = isSuccess



    fun getStudentList(getAbsentRequest : GetAbsentRequest){
        isLoading.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    isLoading.postValue(false)
                    val studentList = messageRepository?.getAbsentStudentList(getAbsentRequest)
                    studentList.let {
                        mutuableLiveDataStudentList.postValue(studentList)
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

//    fun getMarksStudentList(){
//        isLoading.postValue(true)
//        viewModelScope.launch {
//            withContext(Dispatchers.IO){
//                try {
//                    isLoading.postValue(false)
//                    val studentList = messageRepository?.getMarksStudentList()
//                    studentList.let {
//                        mutuableLiveDataMarksStudentList.postValue(studentList)
//                    }
//
//                }catch (httpException : HttpException){
//                    isLoading.postValue(false)
//                    val errorResponse  = AppUtil.getErrorResponse(httpException.response()?.errorBody()?.string())
//                    errorResponse.let { getErrorMutableLiveData().postValue(it) }
//
//                }catch (e : Exception){
//                    isLoading.postValue(false)
//                    e.printStackTrace()
//                }
//            }
//        }
//    }
    fun getAbsentEmployeeList(getAbsentRequest : GetAbsentRequest){
        isLoading.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    isLoading.postValue(false)
                    val studentList = messageRepository?.getAbsentEmployeeList(getAbsentRequest)
                    studentList.let {
                        mutuableLiveDataStudentList.postValue(studentList)
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

    fun getLateStudentList(getAbsentRequest : GetAbsentRequest){
        isLoading.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    isLoading.postValue(false)
                    val studentList = messageRepository?.getLateStudentList(getAbsentRequest)
                    studentList.let {
                        mutuableLiveDataStudentList.postValue(studentList)
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


    fun getLateEmployeeList(getAbsentRequest : GetAbsentRequest){
        isLoading.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    isLoading.postValue(false)
                    val studentList = messageRepository?.getLateEmployeeList(getAbsentRequest)
                    studentList.let {
                        mutuableLiveDataStudentList.postValue(studentList)
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
    fun getEmployeeDepartments(designation : String, isDropDown : Boolean){
        isLoading.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    isLoading.postValue(false)
                    val studentList = messageRepository?.getEmployeeDepartments(designation,isDropDown)
                    studentList.let {
                        mutuableLiveDataEmployeeDepartment.postValue(studentList)
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
    fun getEmployeeProfilesByDepartmentList(departmentList : ArrayList<String>){
        isLoading.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    isLoading.postValue(false)
                    val list = messageRepository?.getEmployeeProfilesByDepartmentList(departmentList)
                    list.let {
                        mutuableLiveDataEmployeeList.postValue(list)
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
    fun sendAttendanceReport(getAbsentRequest : GetAbsentRequest){
        isLoading.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    isLoading.postValue(false)
                    val studentList = messageRepository?.sendAttendanceReport(getAbsentRequest)
                    mutuableLiveDataAbsentReport.postValue(true)
//                    studentList.let {
//                        mutuableLiveDataAbsentReport.postValue(studentList)
//                    }

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
    fun sendGeneralNotification(sendGeneralNotification : SendGeneralNotification){
        isLoading.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    isLoading.postValue(false)
                    val sendNotificationData = messageRepository?.sendGeneralNotification(sendGeneralNotification)
                    sendNotificationData.let {
                        mutuableLiveDataSendNotificationData.postValue(sendNotificationData)
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

    fun getShiftList(profileType : String, classRoomId : ArrayList<Int>?){
        isLoading.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    isLoading.postValue(false)
                    val shiftList = messageRepository?.getShiftList(profileType,classRoomId)
                    shiftList.let {
                        mutuableLiveDataShiftList.postValue(shiftList)
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
    fun getFeeHeadTermList(dueDate : String){
        isLoading.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    isLoading.postValue(false)
                    if(dueDate.equals("")){
                        val list = messageRepository?.getFeeHeadTermListOverDue()
                        list.let {
                            mutuableLiveDataFeeHeadTermList.postValue(list)
                        }
                    }else{
                        val list = messageRepository?.getFeeHeadTermList(dueDate)
                        list.let {
                            mutuableLiveDataFeeHeadTermList.postValue(list)
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
    fun getPendingFeeStudentList(model : PendingFeeStudentRequest){
        isLoading.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    isLoading.postValue(false)
                    val list = messageRepository?.getPendingFeeStudentList(model)
                    list.let {
                        mutuableLiveDataStudentListModel.postValue(list!!.content)
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