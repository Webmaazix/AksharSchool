package com.akshar.one.model

import androidx.room.Embedded
import androidx.room.Relation
import com.akshar.one.database.entity.DegreeEntity
import com.akshar.one.database.entity.DepartmentEntity

data class DegreeWithDeptModel(
    @Embedded val degreeEntity: DegreeEntity,
    @Relation(
        entity = DepartmentEntity::class,
        parentColumn = DegreeEntity.DEGREE_ID,
        entityColumn = DepartmentEntity.DEGREE_ID
    )
    val deptList: List<DeptWithCourseModel>
)