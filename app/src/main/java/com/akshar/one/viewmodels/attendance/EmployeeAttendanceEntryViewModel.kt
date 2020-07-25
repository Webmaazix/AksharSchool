package com.akshar.one.viewmodels.attendance

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.akshar.one.database.entity.ShiftEntity
import com.akshar.one.model.ShiftModel
import com.akshar.one.model.StudentAttendanceModel
import com.akshar.one.repository.attendance.AttendanceRepository
import com.akshar.one.util.AppConstant
import com.akshar.one.util.AppUtil
import com.akshar.one.view.attendance.AttendanceCategoryListener
import com.akshar.one.view.attendance.employee.adapter.EmployeeAttendanceAdapter
import com.akshar.one.viewmodels.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class EmployeeAttendanceEntryViewModel(application: Application) : BaseViewModel(application) {

    companion object {
        val PRESENT = "P"
        val ABSENT = "A"
        val WEEK_OFF = "W"
        val HOLIDAY = "H"
        val LATE_ENTRY = "E"
        val LEAVE = "L"
    }

    private var attendanceRepository: AttendanceRepository? = null

    private var employeeAttendanceAdapter: EmployeeAttendanceAdapter? = null

    private var studentAttendanceListMutableLiveData =
        MutableLiveData<List<StudentAttendanceModel>>()
    private val isLoading = MutableLiveData<Boolean>()

    init {
        attendanceRepository = AttendanceRepository(application)
        employeeAttendanceAdapter =
            EmployeeAttendanceAdapter(
                this
            )
    }

    fun getIsLoading(): MutableLiveData<Boolean> = isLoading

    fun getStudentAttendanceListMutableLiveData(): MutableLiveData<List<StudentAttendanceModel>> =
        studentAttendanceListMutableLiveData

    fun getEmployeeAttendanceAdapter(): EmployeeAttendanceAdapter? = employeeAttendanceAdapter

    fun getEmployeeAt(position: Int): StudentAttendanceModel? =
        studentAttendanceListMutableLiveData.value?.getOrNull(position)

    fun setEmployeeAttendanceListInAdapter(studentAttendanceList: List<StudentAttendanceModel>?) {
        employeeAttendanceAdapter?.setStudentAttendanceList(studentAttendanceList)
    }

    fun updateAttendance(position: Int, status: String) {
        val student = getEmployeeAt(position)
        student?.lateEntryFlag = null
        when (status) {
            PRESENT -> {
                student?.attendanceInd = PRESENT
            }
            ABSENT -> {
                student?.attendanceInd = ABSENT
            }
            LATE_ENTRY -> {
                student?.attendanceInd = PRESENT
                student?.lateEntryFlag = LATE_ENTRY
            }
            LEAVE -> {
                student?.attendanceInd = LEAVE
            }
        }
    }

    fun saveEmployeeAttendance() {
        studentAttendanceListMutableLiveData.value?.let {
            isLoading.value = true
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    try {
                        isLoading.postValue(true)
                        attendanceRepository?.saveAttendanceStudent(
                            AttendanceRepository.EMPLOYEE_PROFILE_TYPE,
                            it
                        )
                        isLoading.postValue(false)
                    } catch (httpException: HttpException) {
                        isLoading.postValue(false)
                        val errorResponse =
                            AppUtil.getErrorResponse(
                                httpException.response()?.errorBody()?.string()
                            )
                        errorResponse?.let { getErrorMutableLiveData().postValue(it) }
                    } catch (e: Exception) {
                        isLoading.postValue(false)
                        Log.d(AppConstant.TAG, "Save Employee Attendance  Exception : $e")
                    }
                }

            }
        }

    }

    fun markAll(status: String) {
        studentAttendanceListMutableLiveData.value?.let {
            for (student in it) {
                student.lateEntryFlag = null
                student.attendanceInd = status
            }
            employeeAttendanceAdapter?.notifyDataSetChanged()
        }
    }

    fun getShifts(
        attendanceCategoryListener: AttendanceCategoryListener
    ): List<ShiftEntity>? {
        var shiftEntityList: List<ShiftEntity>? = ArrayList()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    isLoading.postValue(true)
                    shiftEntityList =
                        attendanceRepository?.getEmployeeShiftsFromDB()
                    if (shiftEntityList.isNullOrEmpty()) {
                        val shiftList =
                            attendanceRepository?.getShifts(
                                AttendanceRepository.EMPLOYEE_PROFILE_TYPE
                            )
                        shiftList?.let { list ->
                            insertAttendanceShiftInDB(list)
                        }
                        shiftEntityList =
                            attendanceRepository?.getEmployeeShiftsFromDB()
                    }

                    withContext(Dispatchers.Main) {
                        attendanceCategoryListener.updateAttendanceCategory(
                            shiftEntityList
                        )
                    }
                    isLoading.postValue(false)

                } catch (httpException: HttpException) {
                    isLoading.postValue(false)
                    val errorResponse =
                        AppUtil.getErrorResponse(
                            httpException.response()?.errorBody()?.string()
                        )
                    errorResponse?.let { getErrorMutableLiveData().postValue(it) }
                } catch (e: Exception) {
                    isLoading.postValue(false)
                    Log.d(AppConstant.TAG, "AttendanceCategories Exception : $e")
                }
            }
        }

        return shiftEntityList
    }

    private suspend fun insertAttendanceShiftInDB(
        shiftModelList: List<ShiftModel>?
    ) {
        if (!shiftModelList.isNullOrEmpty()) {
            for (shift in shiftModelList) {
                val shiftEntity = ShiftEntity(
                    shiftId = shift.shiftId,
                    schoolCode = shift.schoolCd,
                    profileType = shift.profileType,
                    name = shift.name,
                    startTime = shift.startTime,
                    endTime = shift.endTime
                )
                attendanceRepository?.insertShiftEntity(shiftEntity)
            }
        }
    }

    fun fetchEmployeeShifts(shiftId: Int, date: String) {
        isLoading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    isLoading.postValue(true)
                    val studentAttendanceList =
                        attendanceRepository?.getEmployeeAttendance(
                            shiftId = shiftId,
                            date = date
                        )
                    studentAttendanceList?.let {
                        for (student in it) {
                            student.date = date
                            student.attendanceInd = PRESENT
                        }
                    }

                    studentAttendanceListMutableLiveData.postValue(studentAttendanceList)
                    isLoading.postValue(false)

                } catch (httpException: HttpException) {
                    isLoading.postValue(false)
                    val errorResponse =
                        AppUtil.getErrorResponse(httpException.response()?.errorBody()?.string())
                    errorResponse?.let { getErrorMutableLiveData().postValue(it) }
                } catch (e: Exception) {
                    isLoading.postValue(false)
                    Log.d(AppConstant.TAG, "Courses Exception : $e")
                }
            }
        }
    }
}