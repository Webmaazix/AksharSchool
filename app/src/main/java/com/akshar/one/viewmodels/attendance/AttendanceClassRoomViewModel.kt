package com.akshar.one.viewmodels.attendance

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.akshar.one.database.entity.AttendanceCategoryEntity
import com.akshar.one.database.entity.ClassRoomEntity
import com.akshar.one.manager.SessionManager
import com.akshar.one.repository.attendance.AttendanceRepository
import com.akshar.one.util.AppConstant
import com.akshar.one.util.AppUtil
import com.akshar.one.view.attendance.AttendanceCategoryListener
import com.akshar.one.view.attendance.adapters.ClassRoomAdapter
import com.akshar.one.viewmodels.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class AttendanceClassRoomViewModel(application: Application) : BaseViewModel(application) {

    private var attendanceRepository: AttendanceRepository? = null

    private var classRoomListMutableLiveData = MutableLiveData<List<ClassRoomEntity>>()
    private var attendanceCategoryListMutableLiveData =
        MutableLiveData<List<AttendanceCategoryEntity>>()

    private val isLoading = MutableLiveData<Boolean>()

    init {
        attendanceRepository = AttendanceRepository(application)
    }

    fun getIsLoading(): MutableLiveData<Boolean> = isLoading

    fun getClassRoomAt(position: Int): ClassRoomEntity? =
        classRoomListMutableLiveData.value?.getOrNull(position)

    fun getClassRoomListMutableLiveData(): MutableLiveData<List<ClassRoomEntity>> =
        classRoomListMutableLiveData

    fun getAttendanceCategoryListMutableLiveData(): MutableLiveData<List<AttendanceCategoryEntity>> =
        attendanceCategoryListMutableLiveData

    fun getAttendanceCategories(
        position: Int,
        attendanceCategoryListener: AttendanceCategoryListener
    ): List<AttendanceCategoryEntity>? {
        val classRoomEntity = getClassRoomAt(position)
        var attendanceCategoryEntityList: List<AttendanceCategoryEntity>? = ArrayList()
        classRoomEntity?.let { classroom ->
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    try {
                        isLoading.postValue(true)
                        val schoolCode = SessionManager.getLoginModel()?.appsList?.get(0)?.orgCodes
                        attendanceCategoryEntityList =
                            attendanceRepository?.getAttendanceCategoryByClassRoomIdFromDB(
                                classroom.classroomId, classroom.schoolCode
                            )
                        if (attendanceCategoryEntityList.isNullOrEmpty()) {
                            val categoryList =
                                attendanceRepository?.getAttendanceCategories(classroom.classroomId)
                            categoryList?.let { list ->
                                insertAttendanceCategoryInDB(
                                    classroom.classroomId,
                                    classroom.schoolCode,
                                    "STUDENT",
                                    list
                                )

                            }
                            attendanceCategoryEntityList =
                                attendanceRepository?.getAttendanceCategoryByClassRoomIdFromDB(
                                    classroom.classroomId, classroom.schoolCode
                                )
                        }

                        withContext(Dispatchers.Main) {
                            attendanceCategoryListener.updateAttendanceCategory(
                                attendanceCategoryEntityList
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
        return attendanceCategoryEntityList
    }

    private suspend fun insertAttendanceCategoryInDB(
        classroomId: Int,
        schoolCode: String?,
        profileType: String,
        attendanceCategoryList: List<String>?
    ) {
        if (!attendanceCategoryList.isNullOrEmpty()) {
            for (category in attendanceCategoryList) {
                val attendanceCategoryEntity = AttendanceCategoryEntity(
                    classroomId = classroomId,
                    schoolCode = schoolCode,
                    profileType = profileType,
                    category = category
                )
                attendanceRepository?.insertAttendanceCategory(attendanceCategoryEntity)
            }
        }
    }

    fun getAttendanceCategoriesFromDB(
        position: Int,
        attendanceCategoryListener: AttendanceCategoryListener
    ): List<AttendanceCategoryEntity>? {
        val classRoomEntity = getClassRoomAt(position)
        var attendanceCategoryEntityList: List<AttendanceCategoryEntity>? = ArrayList()
        classRoomEntity?.let { classroom ->
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    try {
                        attendanceCategoryEntityList =
                            attendanceRepository?.getAttendanceCategoryByClassRoomIdFromDB(
                                classroom.classroomId,
                                classroom.schoolCode
                            )
                        withContext(Dispatchers.Main) {
                            attendanceCategoryListener.updateAttendanceCategory(
                                attendanceCategoryEntityList
                            )
                        }

                    } catch (e: Exception) {
                        Log.d(AppConstant.TAG, "AttendanceCategories DB Exception : $e")
                    }

                }
            }
        }
        return attendanceCategoryEntityList
    }

}