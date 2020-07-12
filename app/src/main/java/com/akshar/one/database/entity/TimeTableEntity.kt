package com.akshar.one.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = TimeTableEntity.TABLE_NAME
)
data class TimeTableEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)var id : Long = 0,
    @ColumnInfo(name = USER_ID)var userId : Int?,
    @ColumnInfo(name = CLASSROOMID) var classRoomId : Int?,
    @ColumnInfo(name = CLASSROOMNAME) var classRoomName : String?,
    @ColumnInfo(name = COURCENAME) var courceName : String?,
    @ColumnInfo(name = WEEKDAY) var weekDay : String?,
    @ColumnInfo(name = SUBJECTNAME)var subjectName : String?,
    @ColumnInfo(name = START_TIME)var startTime : String?,
    @ColumnInfo(name = END_TIME)var endTime : String?,
    @ColumnInfo(name = TEACHER_NAME)var teacherName : String?,
    @ColumnInfo(name = SESSION_NUMBER)var sessionNumber : String?
) {
    companion object{
        const val TABLE_NAME = "timetable"
        const val ID = "id"
        const val USER_ID = "userId"
        const val CLASSROOMID = "classroomId"
        const val CLASSROOMNAME = "classroomName"
        const val COURCENAME = "courseName"
        const val WEEKDAY = "weekDay"
        const val SUBJECTNAME = "subjectName"
        const val START_TIME = "startTime"
        const val END_TIME = "endTime"
        const val TEACHER_NAME = "teacherName"
        const val SESSION_NUMBER = "sessionNumber"

    }
}