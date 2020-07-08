package com.akshar.one.database.dao

import androidx.room.*
import com.akshar.one.database.entity.DegreeEntity
import com.akshar.one.model.DegreeWithDeptModel

@Dao
interface DegreeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(degreeEntity: DegreeEntity): Long

    @Query("DELETE FROM degree")
    suspend fun deleteAll()

    @Transaction
    @Query("SELECT * FROM degree WHERE school_cd = :schoolCode ORDER BY display_order ASC")
    suspend fun getDegree(schoolCode: String): List<DegreeWithDeptModel>?
}