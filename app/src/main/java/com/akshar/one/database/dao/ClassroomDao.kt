package com.akshar.one.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.akshar.one.database.entity.ClassRoomEntity

@Dao
interface ClassroomDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(classroomEntity: ClassRoomEntity): Long

    @Query("DELETE FROM classroom")
    suspend fun deleteAll()

    @Query("SELECT * FROM classroom WHERE course_id = :courseId AND school_cd = :schoolCode ORDER BY display_order ASC")
    suspend fun getClassRoomsByCourseId(courseId:Int, schoolCode: String?): List<ClassRoomEntity>?
}