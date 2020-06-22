package com.akshar.one.viewmodels.attendance

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.akshar.one.database.entity.ClassRoomEntity
import com.akshar.one.database.entity.CourseEntity
import com.akshar.one.manager.SessionManager
import com.akshar.one.model.ClassRoomModel
import com.akshar.one.model.CourseModel
import com.akshar.one.repository.attendance.AttendanceRepository
import com.akshar.one.util.AppConstant
import com.akshar.one.util.AppUtil
import com.akshar.one.view.attendance.adapters.CourseAdapter
import com.akshar.one.viewmodels.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class AttendanceCourseViewModel(application: Application) : BaseViewModel(application) {

    private var attendanceRepository: AttendanceRepository? = null

    private var courseAdapter: CourseAdapter? = null

    private var courseListMutableLiveData = MutableLiveData<List<CourseEntity>>()
    private var classRoomListMutableLiveData = MutableLiveData<List<ClassRoomEntity>>()
    private val isLoading = MutableLiveData<Boolean>()

    init {
        attendanceRepository = AttendanceRepository(application)
        courseAdapter = CourseAdapter(this)
    }

    fun getIsLoading(): MutableLiveData<Boolean> = isLoading

    fun getCourseListMutableLiveData(): MutableLiveData<List<CourseEntity>> =
        courseListMutableLiveData

    fun getCourseAdapter(): CourseAdapter? = courseAdapter

    fun getCourseAt(position: Int): CourseEntity? =
        courseListMutableLiveData.value?.getOrNull(position)

    fun setCourseInAdapter(courseList: List<CourseEntity>?) {
        courseAdapter?.setCourseList(courseList)
    }

    fun getClassRoomListMutableLiveData(): MutableLiveData<List<ClassRoomEntity>> =
        classRoomListMutableLiveData

    fun getCourses() {
        isLoading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    isLoading.postValue(true)
                    val schoolCode = SessionManager.getLoginModel()?.appsList?.get(0)?.orgCodes
                    schoolCode?.let { schlCode ->
                        var coursesEntityList = attendanceRepository?.getCoursesFromDB(schlCode)
                        if (coursesEntityList.isNullOrEmpty()) {
                            val coursesList = attendanceRepository?.getCourses()
                            coursesList?.let { list ->
                                insertCourseInDB(list)
                                coursesEntityList = attendanceRepository?.getCoursesFromDB(schlCode)
                            }
                        }
                        courseListMutableLiveData.postValue(coursesEntityList)
                    }

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

    private suspend fun insertCourseInDB(list: List<CourseModel>) {
        for (course in list) {
            val courseEntity = course.courseId?.let {
                CourseEntity(
                    courseId = it,
                    courseName = course.courseName,
                    displayOrder = course.displayOrder,
                    schoolCode = SessionManager.getLoginModel()?.appsList?.get(
                        0
                    )?.orgCodes
                )
            }
            courseEntity?.let { attendanceRepository?.insertCourse(it) }
        }
    }

    fun onCourseItemClick(position: Int) {
        isLoading.value = true
        val courseEntity = courseListMutableLiveData.value?.get(position)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    isLoading.postValue(true)
                    courseEntity?.let { course ->
                        var classRoomEntityList = attendanceRepository?.getClassRoomsByCourseId(
                            course.courseId,
                            course.schoolCode
                        )
                        if (classRoomEntityList.isNullOrEmpty()) {
                            val classRoomList = attendanceRepository?.getClassRooms(course.courseId)
                            classRoomList?.let { list ->
                                insertClassRoomInDB(list, course)
                                classRoomEntityList = attendanceRepository?.getClassRoomsByCourseId(course.courseId,course.schoolCode)
                            }
                        }
                        classRoomListMutableLiveData.postValue(classRoomEntityList)

                    }
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

    private suspend fun insertClassRoomInDB(
        list: List<ClassRoomModel>,
        course: CourseEntity
    ) {
        for (classRoom in list) {
            val classRoomEntity = classRoom.classroomId?.let {
                ClassRoomEntity(
                    classroomId = it,
                    classroomName = classRoom.classroomName,
                    courseId = course.courseId,
                    schoolCode = course.schoolCode,
                    displayOrder = classRoom.displayOrder
                )
            }
            classRoomEntity?.let { attendanceRepository?.insertClassroom(it) }
        }
    }
}