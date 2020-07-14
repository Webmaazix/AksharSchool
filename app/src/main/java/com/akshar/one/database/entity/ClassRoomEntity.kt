package com.akshar.one.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = ClassRoomEntity.TABLE_NAME
)
data class ClassRoomEntity(
    @PrimaryKey
    @ColumnInfo(name = CLASSROOM_ID) var classroomId: Int = 0,
    @ColumnInfo(name = CLASSROOM_NAME) var classroomName: String?,
    @ColumnInfo(name = COURSE_ID) var courseId: Int? = 0,
    @ColumnInfo(name = SCHOOL_CD) var schoolCode: String?,
    @ColumnInfo(name = DISPLAY_ORDER) var displayOrder: Int?
) : Serializable {

    companion object {
        const val TABLE_NAME = "classroom"
        const val SCHOOL_CD = "school_cd"
        const val CLASSROOM_ID = "classroom_id"
        const val COURSE_ID = "course_id"
        const val CLASSROOM_NAME = "classroom_name"
        const val DISPLAY_ORDER = "display_order"
    }
}