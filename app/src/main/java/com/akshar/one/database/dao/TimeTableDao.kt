package com.akshar.one.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.akshar.one.database.entity.TimeTableEntity

@Dao
interface TimeTableDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(timeTableEntity: TimeTableEntity) : Long

    @Query("DELETE FROM timetable")
    suspend fun deleteAll()

    @Query("SELECT * FROM timetable WHERE userId = :userId")
    suspend fun getTimeTable(userId : Int) : List<TimeTableEntity>?
}