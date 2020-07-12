package com.akshar.one.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.akshar.one.database.entity.BirthDayEntity


@Dao
interface BirthDayDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(birthDayEntity: BirthDayEntity) : Long

    @Query("DELETE FROM birthday")
    suspend fun deleteAll()

    @Query("SELECT * FROM birthday WHERE userId = :userId")
    suspend fun getBirthdayList(userId : Int) : List<BirthDayEntity>?
}