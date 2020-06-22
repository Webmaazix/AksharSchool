package com.akshar.one.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = CourseEntity.TABLE_NAME
)
data class CourseEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID) var id: Long = 0,
    @ColumnInfo(name = COURSE_ID) var courseId: Int = 0,
    @ColumnInfo(name = COURSE_NAME) var courseName: String?,
    @ColumnInfo(name = DEPARTMENT_ID) var departmentId: Int = 0,
    @ColumnInfo(name = SCHOOL_CD) var schoolCode: String?,
    @ColumnInfo(name = DISPLAY_ORDER) var displayOrder: Int? = 0
) {

    companion object {
        const val TABLE_NAME = "course"
        const val ID = "id"
        const val SCHOOL_CD = "school_cd"
        const val DEPARTMENT_ID = "department_id"
        const val COURSE_ID = "course_id"
        const val COURSE_NAME = "course_name"
        const val DISPLAY_ORDER = "display_order"
    }
}