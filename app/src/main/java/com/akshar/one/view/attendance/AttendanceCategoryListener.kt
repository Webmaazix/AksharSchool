package com.akshar.one.view.attendance

import com.akshar.one.database.entity.ShiftEntity

interface AttendanceCategoryListener {
    fun updateAttendanceCategory(shiftEntityList: List<ShiftEntity>?)
    fun onAttendanceCategorySelected(shiftEntity: ShiftEntity)
}