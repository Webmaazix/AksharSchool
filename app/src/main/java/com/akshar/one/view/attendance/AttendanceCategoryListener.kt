package com.akshar.one.view.attendance

import com.akshar.one.database.entity.AttendanceCategoryEntity

interface AttendanceCategoryListener {
    fun updateAttendanceCategory(attendanceCategoryEntityList: List<AttendanceCategoryEntity>?)
    fun onAttendanceCategorySelected(attendanceCategoryEntity: AttendanceCategoryEntity)
}