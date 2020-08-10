package com.akshar.one.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.akshar.one.database.entity.ShiftEntity

@Dao
interface AttendanceCategoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(shiftEntity: ShiftEntity): Long

    @Query("DELETE FROM shift")
    suspend fun deleteAll()

    @Query("SELECT * FROM shift WHERE classroom_id = :classRoomId AND school_cd = :schoolCode")
    suspend fun getStudentShifts(classRoomId: Int, schoolCode: String?): List<ShiftEntity>?

    @Query("SELECT * FROM shift WHERE profile_type = :profileType")
    suspend fun getEmployeeShifts(profileType: String): List<ShiftEntity>?
}