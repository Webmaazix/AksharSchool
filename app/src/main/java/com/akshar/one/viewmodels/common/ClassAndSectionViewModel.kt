package com.akshar.one.viewmodels.common

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.akshar.one.database.entity.ClassRoomEntity
import com.akshar.one.database.entity.CourseEntity
import com.akshar.one.manager.SessionManager
import com.akshar.one.model.CourseModel
import com.akshar.one.model.CourseWithClassRoomModel
import com.akshar.one.model.DegreeModel
import com.akshar.one.model.DegreeWithDeptModel
import com.akshar.one.repository.attendance.AttendanceRepository
import com.akshar.one.util.AppConstant
import com.akshar.one.view.common.adapters.ClassAndSectionAdapter
import com.akshar.one.viewmodels.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ClassAndSectionViewModel(application: Application) : BaseViewModel(application) {

    private var degreeList: List<DegreeModel>? = null
    private var courseList: List<CourseModel>? = null
    private var classAndSectionAdapter: ClassAndSectionAdapter? = null
    private var attendanceRepository: AttendanceRepository? = null

    private var courseWithClassRoomMutableList = MutableLiveData<List<CourseWithClassRoomModel>?>()
    private var selectedCourseEntityLiveData = MutableLiveData<CourseEntity?>()
    private var selectedClassRoomEntityLiveData = MutableLiveData<ClassRoomEntity?>()
    private var degreeListMutableLiveData = MutableLiveData<List<DegreeWithDeptModel>>()

    init {
        attendanceRepository = AttendanceRepository()
        classAndSectionAdapter =
            ClassAndSectionAdapter(this)
    }

    fun getDegreeListMutableLiveData(): MutableLiveData<List<DegreeWithDeptModel>> =
        degreeListMutableLiveData

    fun getSelectedClassRoomEntityLiveData() = selectedClassRoomEntityLiveData

    fun setSelectedClassRoomEntityLiveData(classRoomEntity: ClassRoomEntity?) {
        selectedClassRoomEntityLiveData.value = classRoomEntity
    }

    fun getSelectedCourseEntityLiveData() = selectedCourseEntityLiveData

    fun setSelectedCourseEntityLiveData(courseEntity: CourseEntity?) {
        selectedCourseEntityLiveData.value = courseEntity
    }

    fun getCourseWithClassRoomMutableLiveDataList() = courseWithClassRoomMutableList

    fun setDegreeList(degreeList: List<DegreeModel>?) {
        this.degreeList = degreeList
    }

    fun setCourseList(courseList: List<CourseModel>?) {
        this.courseList = courseList
    }

    fun getClassAndSectionAdapter(): ClassAndSectionAdapter? = classAndSectionAdapter

    fun getDegreeModel(position: Int): DegreeModel? = degreeList?.getOrNull(position)

    fun getCourseModel(position: Int): CourseModel? = courseList?.getOrNull(position)

    fun getCourseWithClassRoomModel(position: Int): CourseWithClassRoomModel? =
        courseWithClassRoomMutableList.value?.getOrNull(position)

    fun getCoursesWithClassRoomFromDB() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val schoolCode = SessionManager.getLoginModel()?.appsList?.get(0)?.orgCodes
                    schoolCode?.let { schlCode ->
                        courseWithClassRoomMutableList.postValue(
                            attendanceRepository?.getClassRoomsDropdownFromDB(
                                schlCode
                            )
                        )
                    }

                } catch (e: Exception) {
                    Log.d(AppConstant.TAG, "Get CoursesWithClassRoom From DB Exception : $e")
                }
            }
        }
    }

    fun setCourseWithClassroomListInAdapter(courseWithClassRoomList: List<CourseWithClassRoomModel>?) {
        classAndSectionAdapter?.setCourseWithClassroomList(courseWithClassRoomList)
    }

    fun onClassRoomSectionClick(courseEntity: CourseEntity?,classRoomEntity: ClassRoomEntity?) {
        selectedCourseEntityLiveData.value = courseEntity
        selectedClassRoomEntityLiveData.value = classRoomEntity
    }

    fun getDegreeWithDeptModel(position: Int): DegreeWithDeptModel? =
        degreeListMutableLiveData.value?.getOrNull(position)

    fun getDegreeWithDeptFromDB() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val schoolCode = SessionManager.getLoginModel()?.appsList?.get(0)?.orgCodes
                    schoolCode?.let { schlCode ->
                        degreeListMutableLiveData.postValue(
                            attendanceRepository?.getDegreeFromDB(
                                schlCode
                            )
                        )
                    }

                } catch (e: Exception) {
                    Log.d(AppConstant.TAG, "Get CoursesWithClassRoom From DB Exception : $e")
                }
            }
        }
    }

    fun setDegreeWithDeptInAdapter(degreeWithDeptList: List<DegreeWithDeptModel>?) {
        classAndSectionAdapter?.setDegreeWithDeptList(degreeWithDeptList)
    }
}