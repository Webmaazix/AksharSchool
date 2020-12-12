package com.akshar.one.api.service

import com.akshar.one.model.*
import retrofit2.http.*

interface AttendanceApi {

    @GET("attendance/shifts")
    suspend fun getShifts(
        @HeaderMap headers: Map<String, String>,
        @Query("profileType") profileType: String,
        @Query("classroomIdList") classroomId: ArrayList<Int>
    ): List<ShiftList>?

    @GET("attendance/shifts")
    suspend fun getShiftListEmployee(
        @HeaderMap headers: Map<String, String>,
        @Query("profileType") profileType: String
    ): List<ShiftList>?

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