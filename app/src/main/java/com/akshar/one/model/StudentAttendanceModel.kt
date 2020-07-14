package com.akshar.one.model

data class StudentAttendanceModel(
    val rollNbr: String?,
    val firstName: String?,
    val lastName: String?,
    var date: String?,
    var category: String?,
    var attendanceInd: String?,
    var lateEntryFlag: String?,
    val profileId: Int?
)