package com.akshar.one.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.akshar.one.database.entity.DepartmentEntity

@Dao
interface DepartmentDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(departmentEntity: DepartmentEntity): Long

    @Query("DELETE FROM department")
    suspend fun deleteAll()

    @Query("SELECT * FROM department WHERE school_cd = :schoolCode ORDER BY display_order ASC")
    suspend fun getDepartment(schoolCode: String): List<DepartmentEntity>?
}