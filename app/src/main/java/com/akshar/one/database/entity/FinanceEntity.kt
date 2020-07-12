package com.akshar.one.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = FinanceEntity.TABLE_NAME
)
data class FinanceEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID) var id : Long = 0,
    @ColumnInfo(name = USER_ID)var userId : Int?,
    @ColumnInfo(name = FEA_HEAD) var feeHead : String?,
    @ColumnInfo(name = TERM) var term : String?,
    @ColumnInfo(name = FEEAMOUNT) var feeAmount : Int?,
    @ColumnInfo(name = CONSESSION) var consession : Int?,
    @ColumnInfo(name = TOTAL_AMOUNT) var totalAmount : Int?,
    @ColumnInfo(name = AMOUNT_PAID) var amountPaid : Int?,
    @ColumnInfo(name = DUE_AMOUNT) var dueAmount : Int?,
    @ColumnInfo(name = OVER_DUE_AMOUNT)var overDueAmount : Int?


) {

    companion object{
        const val TABLE_NAME = "finance"
        const val ID = "id"
        const val USER_ID = "userId"
        const val FEA_HEAD = "feehead"
        const val TERM = "term"
        const val FEEAMOUNT = "feeAmount"
        const val CONSESSION = "concession"
        const val TOTAL_AMOUNT = "amountAfterConcession"
        const val AMOUNT_PAID = "amountPaid"
        const val DUE_AMOUNT = "dueAmount"
        const val OVER_DUE_AMOUNT = "overdueAmount"
    }
}