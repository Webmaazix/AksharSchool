package com.akshar.one.view.attendance

import com.akshar.one.database.entity.ClassRoomEntity
import com.akshar.one.database.entity.CourseEntity

interface OnClassRoomSelectedListener {
    fun onClassRoomSelectedListener(courseEntity: CourseEntity, classroomEntity: ClassRoomEntity)
}