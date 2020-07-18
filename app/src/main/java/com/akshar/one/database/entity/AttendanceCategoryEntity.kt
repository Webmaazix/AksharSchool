package com.akshar.one.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = AttendanceCategoryEntity.TABLE_NAME
)
data class AttendanceCategoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID) var id: Long = 0,
    @ColumnInfo(name = CLASSROOM_ID) var classroomId: Int = 0,
    @ColumnInfo(name = SCHOOL_CD) var schoolCode: String?,
    @ColumnInfo(name = PROFILE_TYPE) var profileType: String?,
    @ColumnInfo(name = CATEGORY) var category: String?
):Serializable {

    companion object {
        const val TABLE_NAME = "attendance_category"
        const val ID = "id"
        const val CLASSROOM_ID = "classroom_id"
        const val SCHOOL_CD = "school_cd"
        const val PROFILE_TYPE = "profile_type"
        const val CATEGORY = "category"
    }
}