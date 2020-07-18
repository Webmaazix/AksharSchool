package com.akshar.one.api.service

import com.akshar.one.model.ClassRoomModel
import com.akshar.one.model.CourseModel
import com.akshar.one.model.DegreeModel
import com.akshar.one.model.StudentAttendanceModel
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

    @GET("AksharOne/attendance/categories")
    suspend fun getAttendanceCategories(
        @HeaderMap headers: Map<String, String>,
        @Query("profileType") profileType: String = "STUDENT",
        @Query("classroomId") classroomId: Int
    ): List<String>?

    @GET("AksharOne/attendance/classroom/{classroomId}")
    suspend fun getStudentsAttendanceByClassRoomId(
        @HeaderMap headers: Map<String, String>,
        @Path("classroomId") classroomId: Int,
        @Query("category") category: String,
        @Query("date") date: String
    ): List<StudentAttendanceModel>?

    @POST("AksharOne/attendance/classroom/{classroomId}")
    suspend fun saveStudentsAttendanceByClassRoomId(
        @HeaderMap headers: Map<String, String>,
        @Path("classroomId") classroomId: Int,
        @Body  studentList : List<StudentAttendanceModel>
    )

    @GET("AksharOne/classrooms/dropdown")
    suspend fun getClassRoomsDropDown(@HeaderMap headers: Map<String, String>): List<CourseModel>?

    @GET("AksharOne/classrooms/dropdown")
    suspend fun getDegreeClassRoomsDropDown(@HeaderMap headers: Map<String, String>): List<DegreeModel>?
}