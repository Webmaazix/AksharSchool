package com.akshar.one.repository.attendance

import android.app.Application
import com.akshar.one.api.service.AksharSchoolService
import com.akshar.one.api.service.AttendanceApi
import com.akshar.one.database.AksharSchoolDataBase
import com.akshar.one.database.dao.ClassroomDao
import com.akshar.one.database.dao.CourseDao
import com.akshar.one.database.entity.ClassRoomEntity
import com.akshar.one.database.entity.CourseEntity
import com.akshar.one.repository.base.BaseRepository

class AttendanceRepository(application: Application) : BaseRepository() {

    private var attendanceApi: AttendanceApi? = null
    private var service: AksharSchoolService = AksharSchoolService()
    private var courseDao: CourseDao? = null
    private var classroomDao: ClassroomDao? = null

    init {
        attendanceApi = service.createService(AttendanceApi::class.java)
        val aksharSchoolDataBase = AksharSchoolDataBase.getDatabase(application)
        courseDao = aksharSchoolDataBase.courseDao()
        classroomDao = aksharSchoolDataBase.classroomDao()
    }

    suspend fun getCourses() = attendanceApi?.getCourses(service.headers())

    suspend fun getClassRooms(courseId: Int) =
        attendanceApi?.getClassRooms(service.headers(), courseId)

    suspend fun getCoursesFromDB(schoolCode: String) = courseDao?.getCourses(schoolCode)

    suspend fun insertCourse(courseEntity: CourseEntity) = courseDao?.insert(courseEntity)

    suspend fun getClassRoomsByCourseId(courseId: Int, schoolCode: String?) =
        classroomDao?.getClassRoomsByCourseId(courseId, schoolCode)

    suspend fun insertClassroom(classRoomEntity: ClassRoomEntity) =
        classroomDao?.insert(classRoomEntity)
}