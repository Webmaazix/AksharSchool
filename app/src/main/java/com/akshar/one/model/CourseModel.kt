package com.akshar.one.model

data class CourseModel(
    val courseId: Int?,
    val courseName: String?,
    val displayOrder: Int?,
    val classroomsList: List<ClassRoomModel>?
)