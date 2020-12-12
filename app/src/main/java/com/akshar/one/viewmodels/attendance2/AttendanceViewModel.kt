package com.akshar.one.viewmodels.attendance2

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.akshar.one.model.*
import com.akshar.one.repository.attendance2.AttendanceRepository
import com.akshar.one.repository.messagecenter.MessageCenterRepository
import com.akshar.one.util.AppUtil
import com.akshar.one.viewmodels.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.lang.Exception

class AttendanceViewModel(application: Application) : BaseViewModel(application){
    private var messageRepository : MessageCenterRepository? = null
    private var attendanceRepository : AttendanceRepository? = null
    private val isLoading = MutableLiveData<Boolean>()
    private val isSuccess = MutableLiveData<Boolean>()
    private var mutuableLiveDataShiftList = MutableLiveData<List<ShiftList>>()
    private var mutuableLiveDataStudentAttendance = MutableLiveData<List<StudentAttendanceModel>>()



    init {
        attendanceRepository = AttendanceRepository()
    }

    fun getIsLoading(): MutableLiveData<Boolean> = isLoading


    fun getShiftListLiveData() : MutableLiveData<List<ShiftList>> = mutuableLiveDataShiftList
    fun getStudentListLiveData() : MutableLiveData<List<StudentAttendanceModel>> = mutuableLiveDataStudentAttendance
    fun getSuccessLiveData() : MutableLiveData<Boolean> = isSuccess

    fun getShiftList(profileType : String, classRoomId : ArrayList<Int>?){
        isLoading.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    isLoading.postValue(false)
                    if(profileType.equals("Student")){
                        val shiftList = attendanceRepository?.getShiftList(profileType,classRoomId!!)
                        shiftList.let {
                            mutuableLiveDataShiftList.postValue(shiftList)
                        }
                    }else{
                        val shiftList = attendanceRepository?.getShiftListEmployee(profileType)
                        shiftList.let {
                            mutuableLiveDataShiftList.postValue(shiftList)
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
    fun getStudentAttendanceByShidtAndClassId(classRoomId : Int?, shiftId : Int, date : String){
        isLoading.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    isLoading.postValue(false)
                    if(classRoomId == null){
                        val shiftList = attendanceRepository?.getEmployeeAttendance(shiftId,date)
                        shiftList.let {
                            mutuableLiveDataStudentAttendance.postValue(shiftList)
                        }
                    }else{
                        val shiftList = attendanceRepository?.getStudentAttendanceByClassRoomId(classRoomId,shiftId,date)
                        shiftList.let {
                            mutuableLiveDataStudentAttendance.postValue(shiftList)
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

    fun saveStudentsAttendance(profileType : String, studentAttendance : ArrayList<StudentAttendanceModel>){
        isLoading.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    isLoading.postValue(false)
                    val shiftList = attendanceRepository?.saveStudentsAttendance(profileType,studentAttendance)
                    isSuccess.postValue(true)

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