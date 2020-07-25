package com.akshar.one.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = ShiftEntity.TABLE_NAME
)
data class ShiftEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = SHIFT_ID) var shiftId: Int? = 0,
    @ColumnInfo(name = CLASSROOM_ID) var classroomId: Int = 0,
    @ColumnInfo(name = SCHOOL_CD) var schoolCode: String?,
    @ColumnInfo(name = PROFILE_TYPE) var profileType: String?,
    @ColumnInfo(name = NAME) var name: String?,
    @ColumnInfo(name = START_TIME) var startTime: String?,
    @ColumnInfo(name = END_TIME) var endTime: String?
):Serializable {

    companion object {
        const val TABLE_NAME = "shift"
        const val SHIFT_ID = "shift_id"
        const val CLASSROOM_ID = "classroom_id"
        const val SCHOOL_CD = "school_cd"
        const val PROFILE_TYPE = "profile_type"
        const val NAME = "name"
        const val START_TIME = "startTime"
        const val END_TIME = "endTime"
    }
}