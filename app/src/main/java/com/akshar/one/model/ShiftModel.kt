package com.akshar.one.model

data class ShiftModel(
    val shiftId: Int?,
    val schoolCd: String?,
    val profileType: String?,
    var name: String?,
    var startTime: String?,
    var endTime: String?,
    var emploeeList: String?,
    val classroomList: String?
)