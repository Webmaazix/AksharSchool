package com.akshar.one.viewmodels.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.akshar.one.database.entity.ClassRoomEntity
import com.akshar.one.database.entity.CourseEntity
import com.akshar.one.database.entity.DegreeEntity
import com.akshar.one.database.entity.DepartmentEntity
import com.akshar.one.manager.SessionManager
import com.akshar.one.model.*
import com.akshar.one.repository.attendance.AttendanceRepository
import com.akshar.one.util.AppConstant
import com.akshar.one.util.AppUtil
import com.akshar.one.viewmodels.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class MainViewModel(application: Application) : BaseViewModel(application) {

    private var attendanceRepository: AttendanceRepository? = null

    private var degreeListMutableLiveData = MutableLiveData<List<DegreeWithDeptModel>>()
    private var courseListMutableLiveData = MutableLiveData<List<CourseEntity>>()

    private val isLoading = MutableLiveData<Boolean>()

    init {
        attendanceRepository = AttendanceRepository(application)
    }

    fun getIsLoading(): MutableLiveData<Boolean> = isLoading

    fun getCourseListMutableLiveData(): MutableLiveData<List<CourseEntity>> =
        courseListMutableLiveData

    fun getDegreeListMutableLiveData(): MutableLiveData<List<DegreeWithDeptModel>> =
        degreeListMutableLiveData

    fun getClassRoomDropdownService() {
        isLoading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    isLoading.postValue(true)
                    val schoolCode = SessionManager.getLoginModel()?.appsList?.get(0)?.orgCodes
                    schoolCode?.let { schlCode ->
                        var coursesEntityList = attendanceRepository?.getCoursesFromDB(schlCode)
                        if (coursesEntityList.isNullOrEmpty()) {
                            val coursesList = attendanceRepository?.getClassRoomsDropdown()
                            coursesList?.let { list ->
                                insertCourseInDB(list)
                                coursesEntityList = attendanceRepository?.getCoursesFromDB(schlCode)
                            }
                        }
                        courseListMutableLiveData.postValue(coursesEntityList)
                        /**
                         * For Degree Data
                         * */
//                        var degreeEntityList = attendanceRepository?.getDegreeFromDB(schlCode)
//                        if (degreeEntityList.isNullOrEmpty()) {
//                            val degreeList = attendanceRepository?.getDegreeClassRoomsDropdown()
//                            degreeList?.let { list ->
//                                insertDegreeInDB(list)
//                            }
//                        }
//                        degreeListMutableLiveData.postValue(
//                            attendanceRepository?.getDegreeFromDB(
//                                schlCode
//                            )
//                        )
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

    private suspend fun insertCourseInDB(
        list: List<CourseModel>,
        departmentEntity: DepartmentEntity? = null
    ) {
        for (course in list) {
            val courseEntity = course.courseId?.let {
                CourseEntity(
                    courseId = it,
                    courseName = course.courseName,
                    displayOrder = course.displayOrder,
                    departmentId = departmentEntity?.departmentId,
                    schoolCode = SessionManager.getLoginModel()?.appsList?.get(
                        0
                    )?.orgCodes
                )
            }
            courseEntity?.let {
                attendanceRepository?.insertCourse(it)
                course.classroomsList?.let { classRoomList ->
                    insertClassRoomInDB(
                        classRoomList,
                        it
                    )
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

    private suspend fun insertDegreeInDB(list: List<DegreeModel>) {
        for (degree in list) {
            val degreeEntity = degree.degreeId?.let {
                DegreeEntity(
                    degreeId = it,
                    degreeName = degree.degreeName,
                    displayOrder = degree.displayOrder,
                    schoolCode = SessionManager.getLoginModel()?.appsList?.get(
                        0
                    )?.orgCodes
                )
            }
            degreeEntity?.let {
                attendanceRepository?.insertDegree(it)
                degree.deptList?.let { deptList ->
                    insertDeptInDB(
                        deptList,
                        it
                    )
                }
            }
        }
    }

    private suspend fun insertDeptInDB(
        list: List<DepartmentModel>,
        degree: DegreeEntity
    ) {
        for (department in list) {
            val departmentEntity = department.deptId?.let {
                DepartmentEntity(
                    departmentId = it,
                    departmentName = department.departmentName,
                    degreeId = degree.degreeId,
                    schoolCode = degree.schoolCode,
                    displayOrder = department.displayOrder
                )
            }
            departmentEntity?.let { deptEntity ->
                attendanceRepository?.insertDepartment(deptEntity)
                department.coursesList?.let { courseList ->
                    insertCourseInDB(
                        courseList,
                        deptEntity
                    )
                }
            }
        }
    }

}