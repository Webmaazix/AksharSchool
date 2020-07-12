package com.akshar.one.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.akshar.one.database.entity.ExpenseEntity

@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(expenseEntity: ExpenseEntity) : Long

    @Query("DELETE FROM expense")
    suspend fun deleteAll()

    @Query("SELECT * FROM expense WHERE userId = :userId")
    suspend fun getExpense(userId: Int) : ExpenseEntity
}