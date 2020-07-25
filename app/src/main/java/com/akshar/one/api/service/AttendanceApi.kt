package com.akshar.one.api.service

import com.akshar.one.model.*
import retrofit2.http.*

interface AttendanceApi {

    @GET("AksharOne/courses")
    suspend fun getCourses(
        @HeaderMap headers: Map<String, String>
    ): List<CourseModel>?

    @GET("AksharOne/classrooms")
    suspend fun getClassRooms(
        @HeaderMap headers: Map<String, String>,
        @Query("courseId") courseId: Int
    ): List<ClassRoomModel>?

    @GET("AksharOne/attendance/shifts")
    suspend fun getShifts(
        @HeaderMap headers: Map<String, String>,
        @Query("profileType") profileType: String = "STUDENT",
        @Query("classroomId") classroomId: Int
    ): List<ShiftModel>?

    @GET("AksharOne/attendance/students")
    suspend fun getStudentsAttendanceByClassRoomId(
        @HeaderMap headers: Map<String, String>,
        @Query("classroomId") classroomId: Int,
        @Query("shiftId") shiftId: Int,
        @Query("date") date: String
    ): List<StudentAttendanceModel>?

    @POST("AksharOne/attendance/record")
    suspend fun saveStudentsAttendance(
        @HeaderMap headers: Map<String, String>,
        @Query("profileType") profileType: String,
        @Body  studentList : List<StudentAttendanceModel>
    )

    @GET("AksharOne/classrooms/dropdown")
    suspend fun getClassRoomsDropDown(@HeaderMap headers: Map<String, String>): List<CourseModel>?

    @GET("AksharOne/classrooms/dropdown")
    suspend fun getDegreeClassRoomsDropDown(@HeaderMap headers: Map<String, String>): List<DegreeModel>?

    @GET("AksharOne/attendance/employees")
    suspend fun getEmployeeAttendance(
        @HeaderMap headers: Map<String, String>,
        @Query("shiftId") shiftId: Int,
        @Query("date") date: String
    ): List<StudentAttendanceModel>?
}