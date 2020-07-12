package com.akshar.one.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.akshar.one.database.entity.CollectionEntity

@Dao
interface CollectionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(collectionEntity: CollectionEntity) : Long

    @Query("DELETE FROM collection")
    suspend fun deleteAll()

    @Query("SELECT * FROM  collection WHERE userId = :userId")
    suspend fun getCollection(userId : Int) : CollectionEntity
}