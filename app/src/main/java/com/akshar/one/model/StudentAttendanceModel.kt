package com.akshar.one.model

data class StudentAttendanceModel(
    val rollNbr: String?,
    val employeeId: String?,
    val fullName: String?,
    var date: String?,
    val shiftId: Int?,
    val shiftName: String?,
    var attendanceInd: String?,
    var lateEntryFlag: String?,
    val profileId: Int?,
    val inTime: String?,
    val outTime: String?
)