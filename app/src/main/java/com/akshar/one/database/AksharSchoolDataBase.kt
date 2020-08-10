package com.akshar.one.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.akshar.one.database.dao.*
import com.akshar.one.database.entity.*

@Database(entities = [CourseEntity::class, ClassRoomEntity::class, ShiftEntity::class, DegreeEntity::class, DepartmentEntity::class], exportSchema = false, version = 1)
abstract class AksharSchoolDataBase: RoomDatabase() {

    abstract fun courseDao(): CourseDao
    abstract fun classroomDao(): ClassroomDao
    abstract fun attendanceCategory(): AttendanceCategoryDao
    abstract fun degreeDao(): DegreeDao
    abstract fun departmentDao(): DepartmentDao

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