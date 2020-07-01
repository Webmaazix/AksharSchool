package com.akshar.one.view.attendance

import com.akshar.one.database.entity.AttendanceCategoryEntity
import com.akshar.one.database.entity.ClassRoomEntity

interface AttendanceBottomSheetDialogListener {
    fun onAttendanceCategorySelectedAction(classRoomEntity: ClassRoomEntity?, attendanceCategoryEntity: AttendanceCategoryEntity?)
}