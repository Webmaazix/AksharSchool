package com.akshar.one.viewmodels.attendance

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.akshar.one.database.entity.ClassRoomEntity
import com.akshar.one.database.entity.ShiftEntity
import com.akshar.one.manager.SessionManager
import com.akshar.one.model.ShiftModel
import com.akshar.one.model.StudentAttendanceModel
import com.akshar.one.repository.attendance.AttendanceRepository
import com.akshar.one.util.AppConstant
import com.akshar.one.util.AppUtil
import com.akshar.one.view.attendance.AttendanceCategoryListener
import com.akshar.one.view.attendance.student.adapter.StudentAttendanceAdapter
import com.akshar.one.viewmodels.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class AttendanceEntryViewModel(application: Application) : BaseViewModel(application) {

    companion object {
        val PRESENT = "P"
        val ABSENT = "A"
        val WEEK_OFF = "W"
        val HOLIDAY = "H"
        val LATE_ENTRY = "E"
        val LEAVE = "L"
    }

    private var attendanceRepository: AttendanceRepository? = null

    private var studentAttendanceAdapter: StudentAttendanceAdapter? = null

    private var studentAttendanceListMutableLiveData =
        MutableLiveData<List<StudentAttendanceModel>>()
    private val isLoading = MutableLiveData<Boolean>()

    init {
        attendanceRepository = AttendanceRepository(application)
        studentAttendanceAdapter =
            StudentAttendanceAdapter(
                this
            )
    }

    fun getIsLoading(): MutableLiveData<Boolean> = isLoading

    fun getStudentAttendanceListMutableLiveData(): MutableLiveData<List<StudentAttendanceModel>> =
        studentAttendanceListMutableLiveData

    fun getStudentAttendanceAdapter(): StudentAttendanceAdapter? = studentAttendanceAdapter

    fun getStudentAt(position: Int): StudentAttendanceModel? =
        studentAttendanceListMutableLiveData.value?.getOrNull(position)

    fun setStudentAttendanceListInAdapter(studentAttendanceList: List<StudentAttendanceModel>?) {
        studentAttendanceAdapter?.setStudentAttendanceList(studentAttendanceList)
    }

    fun getStudentAttendanceByClassRoomId(
        classRoomId: Int,
        shiftId: Int,
        date: String
    ) {
        isLoading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    isLoading.postValue(true)
                    val studentAttendanceList =
                        attendanceRepository?.getStudentAttendanceByClassRoomId(
                            classRoomId = classRoomId,
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

    fun updateAttendance(position: Int, status: String) {
        val student = getStudentAt(position)
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

    fun saveStudentAttendance() {
        studentAttendanceListMutableLiveData.value?.let {
            isLoading.value = true
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    try {
                        isLoading.postValue(true)
                        attendanceRepository?.saveAttendanceStudent(
                            AttendanceRepository.STUDENT_PROFILE_TYPE,
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
                        Log.d(AppConstant.TAG, "Save Student Attendance  Exception : $e")
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
            studentAttendanceAdapter?.notifyDataSetChanged()
        }
    }

    fun getAttendanceCategories(
        classRoomEntity: ClassRoomEntity?,
        attendanceCategoryListener: AttendanceCategoryListener
    ): List<ShiftEntity>? {
        var shiftEntityList: List<ShiftEntity>? = ArrayList()
        classRoomEntity?.let { classroom ->
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    try {
                        isLoading.postValue(true)
                        val schoolCode = SessionManager.getLoginModel()?.appsList?.get(0)?.orgCodes
                        shiftEntityList =
                            attendanceRepository?.getStudentsShiftsByClassRoomIdFromDB(
                                classroom.classroomId, classroom.schoolCode
                            )
                        if (shiftEntityList.isNullOrEmpty()) {
                            val categoryList =
                                attendanceRepository?.getShifts(
                                    AttendanceRepository.STUDENT_PROFILE_TYPE,
                                    classroom.classroomId
                                )
                            categoryList?.let { list ->
                                insertAttendanceShiftInDB(classroom.classroomId, list)
                            }
                            shiftEntityList =
                                attendanceRepository?.getStudentsShiftsByClassRoomIdFromDB(
                                    classroom.classroomId, classroom.schoolCode
                                )
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
        }
        return shiftEntityList
    }

    private suspend fun insertAttendanceShiftInDB(
        classroomId: Int,
        shiftModelList: List<ShiftModel>?
    ) {
        if (!shiftModelList.isNullOrEmpty()) {
            for (shift in shiftModelList) {
                val shiftEntity = ShiftEntity(
                    shiftId = shift.shiftId,
                    classroomId = classroomId,
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
}