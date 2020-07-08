package com.akshar.one.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = DepartmentEntity.TABLE_NAME
)
data class DepartmentEntity(
    @PrimaryKey
    @ColumnInfo(name = DEPARTMENT_ID) var departmentId: Int = 0,
    @ColumnInfo(name = DEPARTMENT_NAME) var departmentName: String?,
    @ColumnInfo(name = SCHOOL_CD) var schoolCode: String?,
    @ColumnInfo(name = DEGREE_ID) var degreeId: Int? = 0,
    @ColumnInfo(name = DISPLAY_ORDER) var displayOrder: Int? = 0
) {

    companion object {
        const val TABLE_NAME = "department"
        const val SCHOOL_CD = "school_cd"
        const val DEPARTMENT_ID = "department_id"
        const val DEPARTMENT_NAME = "department_name"
        const val DEGREE_ID = "degree_id"
        const val DISPLAY_ORDER = "display_order"
    }
}