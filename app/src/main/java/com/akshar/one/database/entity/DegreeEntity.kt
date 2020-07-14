package com.akshar.one.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = DegreeEntity.TABLE_NAME
)
data class DegreeEntity(
    @PrimaryKey
    @ColumnInfo(name = DEGREE_ID) var degreeId: Int = 0,
    @ColumnInfo(name = DEGREE_NAME) var degreeName: String?,
    @ColumnInfo(name = SCHOOL_CD) var schoolCode: String?,
    @ColumnInfo(name = DISPLAY_ORDER) var displayOrder: Int? = 0
) {

    companion object {
        const val TABLE_NAME = "degree"
        const val SCHOOL_CD = "school_cd"
        const val DEGREE_ID = "degree_id"
        const val DEGREE_NAME = "degree_name"
        const val DISPLAY_ORDER = "display_order"
    }
}