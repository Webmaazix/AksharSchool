package com.akshar.one.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    tableName = CollectionEntity.TABLE_NAME
)
data class CollectionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID) var id : Long = 0,
    @ColumnInfo(name = USER_ID)var userId : Int?,
    @ColumnInfo(name = ADMISSION_FEE) var admissionFee : Int?,
    @ColumnInfo(name = TUTION_FEE) var tutionFee : Int?
) {

    companion object{
        const val TABLE_NAME = "collection"
        const val ID ="id"
        const val USER_ID = "userId"
        const val ADMISSION_FEE ="Admission_Fee"
        const val TUTION_FEE = "TUTION_FEE"
    }
}