package com.akshar.one.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.akshar.one.database.dao.AttendanceCategoryDao
import com.akshar.one.database.dao.ClassroomDao
import com.akshar.one.database.dao.CourseDao
import com.akshar.one.database.entity.AttendanceCategoryEntity
import com.akshar.one.database.entity.ClassRoomEntity
import com.akshar.one.database.entity.CourseEntity

@Database(entities = [CourseEntity::class, ClassRoomEntity::class, AttendanceCategoryEntity::class], exportSchema = false, version = 1)
abstract class AksharSchoolDataBase: RoomDatabase() {

    abstract fun courseDao(): CourseDao
    abstract fun classroomDao(): ClassroomDao
    abstract fun attendanceCategory(): AttendanceCategoryDao

    companion object{
        private var INSTANCE: AksharSchoolDataBase? = null
        private const val DB_NAME = "aksharschool.db"

        fun getDatabase(context: Context): AksharSchoolDataBase {
            if(INSTANCE == null){
                synchronized(AksharSchoolDataBase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.applicationContext, AksharSchoolDataBase::class.java, DB_NAME)
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}