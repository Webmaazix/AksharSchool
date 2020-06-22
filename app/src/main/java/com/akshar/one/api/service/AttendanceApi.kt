package com.akshar.one.api.service

import com.akshar.one.model.ClassRoomModel
import com.akshar.one.model.CourseModel
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Query

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
}