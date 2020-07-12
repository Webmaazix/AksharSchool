package com.akshar.one.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    tableName = ExpenseEntity.TABLE_NAME
)
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)var id : Long = 0,
    @ColumnInfo(name = USER_ID)var userId : Int?,
    @ColumnInfo(name = MAINTENANCE) var maintenance : Int?,
    @ColumnInfo(name = TRANSPORT)var transport : Int?
) {
    companion object{
        const val TABLE_NAME = "expense"
        const val ID = "id"
        const val USER_ID = "userId"
        const val MAINTENANCE = "Maintenance"
        const val TRANSPORT = "Transport"
    }
}