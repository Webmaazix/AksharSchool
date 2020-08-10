package com.akshar.one.viewmodels.attendance

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.akshar.one.database.entity.ShiftEntity
import com.akshar.one.database.entity.ClassRoomEntity
import com.akshar.one.manager.SessionManager
import com.akshar.one.model.ShiftModel
import com.akshar.one.repository.attendance.AttendanceRepository
import com.akshar.one.util.AppConstant
import com.akshar.one.util.AppUtil
import com.akshar.one.view.attendance.AttendanceCategoryListener
import com.akshar.one.viewmodels.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class AttendanceClassRoomViewModel(application: Application) : BaseViewModel(application) {

    private var attendanceRepository: AttendanceRepository? = null

    private var classRoomListMutableLiveData = MutableLiveData<List<ClassRoomEntity>>()
    private var attendanceCategoryListMutableLiveData =
        MutableLiveData<List<ShiftEntity>>()

    private val isLoading = MutableLiveData<Boolean>()

    init {
        attendanceRepository = AttendanceRepository(application)
    }

    fun getIsLoading(): MutableLiveData<Boolean> = isLoading

    fun getClassRoomAt(position: Int): ClassRoomEntity? =
        classRoomListMutableLiveData.value?.getOrNull(position)

    fun getClassRoomListMutableLiveData(): MutableLiveData<List<ClassRoomEntity>> =
        classRoomListMutableLiveData

    fun getAttendanceCategoryListMutableLiveData(): MutableLiveData<List<ShiftEntity>> =
        attendanceCategoryListMutableLiveData

    fun getAttendanceCategories(
        position: Int,
        attendanceCategoryListener: AttendanceCategoryListener
    ): List<ShiftEntity>? {
        val classRoomEntity = getClassRoomAt(position)
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
                                attendanceRepository?.getShifts(AttendanceRepository.STUDENT_PROFILE_TYPE, classroom.classroomId)
                            categoryList?.let { list ->
                                insertAttendanceCategoryInDB(
                                    classroom.classroomId,
                                    classroom.schoolCode,
                                    "STUDENT",
                                    list
                                )

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

    private suspend fun insertAttendanceCategoryInDB(
        classroomId: Int,
        schoolCode: String?,
        profileType: String,
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

    fun getAttendanceCategoriesFromDB(
        position: Int,
        attendanceCategoryListener: AttendanceCategoryListener
    ): List<ShiftEntity>? {
        val classRoomEntity = getClassRoomAt(position)
        var shiftEntityList: List<ShiftEntity>? = ArrayList()
        classRoomEntity?.let { classroom ->
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    try {
                        shiftEntityList =
                            attendanceRepository?.getStudentsShiftsByClassRoomIdFromDB(
                                classroom.classroomId,
                                classroom.schoolCode
                            )
                        withContext(Dispatchers.Main) {
                            attendanceCategoryListener.updateAttendanceCategory(
                                shiftEntityList
                            )
                        }

                    } catch (e: Exception) {
                        Log.d(AppConstant.TAG, "AttendanceCategories DB Exception : $e")
                    }

                }
            }
        }
        return shiftEntityList
    }

}