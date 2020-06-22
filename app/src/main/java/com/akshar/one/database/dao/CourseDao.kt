package com.akshar.one.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.akshar.one.database.entity.CourseEntity

@Dao
interface CourseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(course: CourseEntity): Long

    @Query("DELETE FROM course")
    suspend fun deleteAll()

    @Query("SELECT * FROM course WHERE school_cd = :schoolCode ORDER BY display_order ASC")
    suspend fun getCourses(schoolCode: String): List<CourseEntity>?
}