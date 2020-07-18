package com.akshar.one.model

import androidx.room.Embedded
import androidx.room.Relation
import com.akshar.one.database.entity.CourseEntity
import com.akshar.one.database.entity.DepartmentEntity

data class DeptWithCourseModel(
    @Embedded val departmentEntity: DepartmentEntity,
    @Relation(
        entity = CourseEntity::class,
        parentColumn = DepartmentEntity.DEPARTMENT_ID,
        entityColumn = CourseEntity.DEPARTMENT_ID
    )
    val courseList: List<CourseWithClassRoomModel>
)