package com.akshar.one.model

data class DepartmentModel(
    val deptId: Int?,
    val departmentName: String?,
    val displayOrder: Int?,
    val coursesList: List<CourseModel>?
)