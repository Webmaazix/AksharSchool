package com.akshar.one.viewmodels.attendance

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.akshar.one.database.entity.ClassRoomEntity
import com.akshar.one.repository.attendance.AttendanceRepository
import com.akshar.one.view.attendance.adapters.ClassRoomAdapter
import com.akshar.one.viewmodels.base.BaseViewModel

class AttendanceClassRoomViewModel(application: Application) : BaseViewModel(application) {

    private var attendanceRepository: AttendanceRepository? = null

    private var courseAdapter: ClassRoomAdapter? = null

    private var classRoomListMutableLiveData = MutableLiveData<List<ClassRoomEntity>>()
    private val isLoading = MutableLiveData<Boolean>()

    init {
        attendanceRepository = AttendanceRepository(application)
        courseAdapter = ClassRoomAdapter(this)
    }

    fun getIsLoading(): MutableLiveData<Boolean> = isLoading

    fun getClassRoomAdapter(): ClassRoomAdapter? = courseAdapter

    fun getClassRoomAt(position: Int): ClassRoomEntity? =
        classRoomListMutableLiveData.value?.getOrNull(position)

    fun setClassRoomInAdapter(classRoomList: List<ClassRoomEntity>?) {
        courseAdapter?.setClassRoomList(classRoomList)
    }

    fun getClassRoomListMutableLiveData(): MutableLiveData<List<ClassRoomEntity>> =
        classRoomListMutableLiveData
}