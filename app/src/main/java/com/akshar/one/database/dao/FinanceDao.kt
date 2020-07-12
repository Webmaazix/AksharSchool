package com.akshar.one.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.akshar.one.database.entity.FinanceEntity

@Dao
interface FinanceDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(financeEntity: FinanceEntity) : Long

    @Query("DELETE FROM finance")
    suspend fun deleteAll()

    @Query("SELECT * FROM finance WHERE userId = :userId")
    suspend fun getFinance(userId : Int) : FinanceEntity?
}