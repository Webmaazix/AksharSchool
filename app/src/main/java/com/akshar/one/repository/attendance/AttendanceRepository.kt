package com.akshar.one.repository.attendance

import android.app.Application
import com.akshar.one.api.service.AksharSchoolService
import com.akshar.one.api.service.AttendanceApi
import com.akshar.one.database.AksharSchoolDataBase
import com.akshar.one.database.dao.*
import com.akshar.one.database.entity.*
import com.akshar.one.model.CourseWithClassRoomModel
import com.akshar.one.model.StudentAttendanceModel
import com.akshar.one.repository.base.BaseRepository

class AttendanceRepository(application: Application) : BaseRepository() {

    private var attendanceApi: AttendanceApi? = null
    private var service: AksharSchoolService = AksharSchoolService()
    private var courseDao: CourseDao? = null
    private var classroomDao: ClassroomDao? = null
    private var attendanceCategoryDao: AttendanceCategoryDao? = null
    private var degreeDao: DegreeDao? = null
    private var departmentDao: DepartmentDao? = null

    companion object{
        const val EMPLOYEE_PROFILE_TYPE = "Employee"
        const val STUDENT_PROFILE_TYPE = "Student"
    }
    init {
        attendanceApi = service.createService(AttendanceApi::class.java)
        val aksharSchoolDataBase = AksharSchoolDataBase.getDatabase(application)
        courseDao = aksharSchoolDataBase.courseDao()
        classroomDao = aksharSchoolDataBase.classroomDao()
        attendanceCategoryDao = aksharSchoolDataBase.attendanceCategory()
        degreeDao = aksharSchoolDataBase.degreeDao()
        departmentDao = aksharSchoolDataBase.departmentDao()
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

    suspend fun getShifts(profileType: String, classRoomId: Int = 0) =
        attendanceApi?.getShifts(service.headers(), classroomId = classRoomId, profileType = profileType)

    suspend fun getStudentsShiftsByClassRoomIdFromDB(classRoomId: Int, schoolCode: String?) =
        attendanceCategoryDao?.getStudentShifts(classRoomId, schoolCode)

    suspend fun insertShiftEntity(shiftEntity: ShiftEntity) =
        attendanceCategoryDao?.insert(shiftEntity)

    suspend fun getStudentAttendanceByClassRoomId(
        classRoomId: Int,
        date: String,
        shiftId: Int = 0
    ) =
        attendanceApi?.getStudentsAttendanceByClassRoomId(
            service.headers(),
            classroomId = classRoomId,
            shiftId = shiftId,
            date = date
        )

    suspend fun saveAttendanceStudent(profileType: String, studentList: List<StudentAttendanceModel>) =
        attendanceApi?.saveStudentsAttendance(
            service.headers(),
            profileType = profileType,
            studentList = studentList
        )

    suspend fun getClassRoomsDropdown() =
        attendanceApi?.getClassRoomsDropDown(service.headers())

    suspend fun getClassRoomsDropdownFromDB(schoolCode: String): List<CourseWithClassRoomModel>? =
        courseDao?.getCoursesWithClassRoom(schoolCode)

    suspend fun getDegreeFromDB(schoolCode: String) = degreeDao?.getDegree(schoolCode)

    suspend fun getDegreeClassRoomsDropdown() =
        attendanceApi?.getDegreeClassRoomsDropDown(service.headers())

    suspend fun insertDegree(degreeEntity: DegreeEntity) = degreeDao?.insert(degreeEntity)

    suspend fun insertDepartment(departmentEntity: DepartmentEntity) = departmentDao?.insert(departmentEntity)

    suspend fun getEmployeeAttendance(date: String, shiftId: Int) =
        attendanceApi?.getEmployeeAttendance(
            service.headers(),
            shiftId = shiftId,
            date = date
        )

    suspend fun getEmployeeShiftsFromDB() =
        attendanceCategoryDao?.getEmployeeShifts(EMPLOYEE_PROFILE_TYPE)
}