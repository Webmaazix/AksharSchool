package com.akshar.one.api.service

import com.akshar.one.model.CourseModel
import com.akshar.one.model.DegreeModel
import com.akshar.one.model.ShiftModel
import com.akshar.one.model.StudentAttendanceModel
import retrofit2.http.*

interface AttendanceApi {

    @GET("attendance/shifts")
    suspend fun getShifts(
        @HeaderMap headers: Map<String, String>,
        @Query("profileType") profileType: String = "STUDENT",
        @Query("classroomIdList") classroomId: Int
    ): List<ShiftModel>?

    @GET("attendance/students")
    suspend fun getStudentsAttendanceByClassRoomId(
        @HeaderMap headers: Map<String, String>,
        @Query("classroomId") classroomId: Int,
        @Query("shiftId") shiftId: Int,
        @Query("date") date: String
    ): List<StudentAttendanceModel>?

    @POST("attendance/record")
    suspend fun saveStudentsAttendance(
        @HeaderMap headers: Map<String, String>,
        @Query("profileType") profileType: String,
        @Body studentList: List<StudentAttendanceModel>
    )

    @GET("classrooms/dropdown")
    suspend fun getClassRoomsDropDown(@HeaderMap headers: Map<String, String>): List<CourseModel>?

    @GET("classrooms/dropdown")
    suspend fun getDegreeClassRoomsDropDown(@HeaderMap headers: Map<String, String>): List<DegreeModel>?

    @GET("attendance/employees")
    suspend fun getEmployeeAttendance(
        @HeaderMap headers: Map<String, String>,
        @Query("shiftId") shiftId: Int,
        @Query("date") date: String
    ): List<StudentAttendanceModel>?
}