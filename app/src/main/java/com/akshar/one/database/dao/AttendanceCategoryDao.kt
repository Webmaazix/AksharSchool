package com.akshar.one.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.akshar.one.database.entity.AttendanceCategoryEntity

@Dao
interface AttendanceCategoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(attendanceCategoryEntity: AttendanceCategoryEntity): Long

    @Query("DELETE FROM attendance_category")
    suspend fun deleteAll()

    @Query("SELECT * FROM attendance_category WHERE classroom_id = :classRoomId AND school_cd = :schoolCode")
    suspend fun getCategories(classRoomId: Int, schoolCode: String?): List<AttendanceCategoryEntity>?
}