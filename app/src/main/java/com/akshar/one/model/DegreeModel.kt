package com.akshar.one.model

data class DegreeModel(
    val degreeId: Int?,
    val degreeName: String?,
    val displayOrder: Int?,
    val deptList: List<DepartmentModel>?
)