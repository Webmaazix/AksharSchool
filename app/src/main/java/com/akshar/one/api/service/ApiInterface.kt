package com.akshar.one.api.service

import com.akshar.one.model.*
import com.akshar.one.studentprofile.CreateStudentProfile
import retrofit2.http.*
import com.google.gson.JsonObject
import retrofit2.http.POST
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Url
import retrofit2.http.PUT





interface ApiInterface {

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun loginWithUsername(
        @Field("username") username: String,
        @Field("password") password: String
    ): LoginModel?

    @POST("auth/otp/send")
    suspend fun getOTP(
        @Query("mobile") mobileNumber: String
    ): OTPModel?

    @FormUrlEncoded
    @POST("auth/validateOtp")
    suspend fun loginWithOTP(
        @Field("username") mobileNumber: String,
        @Field("password") otp: String
    ): LoginModel?

    @FormUrlEncoded
    @POST("auth/loginCredentials/changePassword")
    suspend fun changePassword(
        @HeaderMap headers: Map<String, String>,
        @Field("username") username: String,
        @Field("password") password: String
    )

    @GET("AksharOne/timetable")
    suspend fun getTimeTable(
        @HeaderMap headers: Map<String, String>,
        @Query("employeeProfileId") employeeId: Int,
        @Query("date") date: String
    ): List<TimeTableModel>?

    @GET("AksharOne/timetable")
    suspend fun getClassTimeTable(
        @HeaderMap headers: Map<String, String>,
        @Query("classroomId") classroomId: Int,
        @Query("date") date: String
    ): List<TimeTableModel>?

    @GET("AksharOne/birthdays")
    suspend fun getBirthdays(
        @HeaderMap headers: Map<String, String>,
        @Query("fromDate") fromDate: String,
        @Query("toDate") toDate: String
    ): List<BirthDayModel>?

    @GET("AksharOne/finance/summary")
    suspend fun getAllFinance(
        @HeaderMap headers: Map<String, String>,
        @Query("fromDate") fromDate: String,
        @Query("toDate") toDate: String
    ): FinanceModel?

    @GET("AksharOne/finance/fees/payments-summary")
    suspend fun getCollection(
        @HeaderMap headers: Map<String, String>,
        @Query("groupBy") groupBy: String,
        @Query("fromDate") fromDate: String,
        @Query("toDate") toDate: String
    ): FeePayment?

    @GET("AksharOne/finance/expenses/summary")
    suspend fun getExpences(
        @HeaderMap headers: Map<String, String>,
        @Query("groupBy") groupBy: String,
        @Query("fromDate") fromDate: String,
        @Query("toDate") toDate: String
    ): ExpenseSummary?

    @GET("AksharOne/classrooms/dropdown")
    suspend fun getClassRoomDropdowns(
        @HeaderMap headers: Map<String, String>
    ) : List<ClassDropDownModel>?

    @GET("AksharOne/students")
    suspend fun getStudentListByClassId(
        @HeaderMap headers: Map<String, String>,
        @Query("classroomId") classroomId: Int
    ) : StudentList?

    @GET("AksharOne/noticeboard/getAll")
    suspend fun getNotices(
        @HeaderMap headers: Map<String, String>,
        @Query("showExpired") showExpired: Boolean
    ) : List<NoticeBoardModel>?

    @PUT("AksharOne/students/{studentProfileId}")
    suspend fun UpdateStudentProfile(
        @HeaderMap headers: Map<String, String>,
        @Path("studentProfileId") studentProfileId : Int,
        @Body jsonObject : StudentListModel
    )
    @PUT("AksharOne/students/{studentProfileId}/image")
    suspend fun uploadImage(
        @HeaderMap headers: Map<String, String>,
        @Path("studentProfileId") studentProfileId : Int
    ) : ImageModel?
    @POST("AksharOne/students")
    suspend fun CreateStudentProfile(
        @HeaderMap headers: Map<String, String>,
        @Body jsonObject : StudentListModel
    )

    @Headers("Content-Type: image/jpg")
    @PUT
    fun aws_upload(
        @Url path: String,
        @Body image: RequestBody
    ): Call<Void>
}