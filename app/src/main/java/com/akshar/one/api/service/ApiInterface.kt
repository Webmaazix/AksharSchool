package com.akshar.one.api.service

import com.akshar.one.model.*
import com.google.gson.JsonObject
import retrofit2.http.*
import retrofit2.http.POST
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Url
import retrofit2.http.PUT





interface ApiInterface {

    @FormUrlEncoded
    @POST("login")
    suspend fun loginWithUsername(
        @Field("username") username: String,
        @Field("password") password: String
    ): LoginModel?

    @POST("otp/send")
    suspend fun getOTP(
        @Query("mobile") mobileNumber: String
    ): OTPModel?

    @FormUrlEncoded
    @POST("validateOtp")
    suspend fun loginWithOTP(
        @Field("username") mobileNumber: String,
        @Field("password") otp: String
    ): LoginModel?

    @FormUrlEncoded
    @POST("loginCredentials/changePassword")
    suspend fun changePassword(
        @HeaderMap headers: Map<String, String>,
        @Field("username") username: String,
        @Field("password") password: String
    )

    @GET("academics/timetable")
    suspend fun getTimeTable(
        @HeaderMap headers: Map<String, String>,
        @Query("employeeProfileId") employeeId: Int,
        @Query("date") date: String
    ): List<TimeTableModel>?

    @GET("academics/timetable")
    suspend fun getClassTimeTable(
        @HeaderMap headers: Map<String, String>,
        @Query("classroomId") classroomId: Int,
        @Query("date") date: String
    ): List<TimeTableModel>?

    @GET("birthdays")
    suspend fun getBirthdays(
        @HeaderMap headers: Map<String, String>,
        @Query("fromDate") fromDate: String,
        @Query("toDate") toDate: String
    ): List<BirthDayModel>?

    @GET("attendance/stats")
    suspend fun getAttendanceStatsOfStudent(
        @HeaderMap headers: Map<String, String>,
        @Query("fromDate") fromDate: String,
        @Query("toDate") toDate: String,
        @Query("studentProfileId") studentProfileId: String
    ):  ArrayList<AttendanceDashboard>?
    @GET("attendance/stats")
    suspend fun getAttendanceStatsOfyear(
        @HeaderMap headers: Map<String, String>,
        @Query("studentProfileId") studentProfileId: String
    ):  ArrayList<AttendanceDashboard>?

    @GET("attendance/today")
    suspend fun getPresentAttendance(
        @HeaderMap headers: Map<String, String>,
        @Query("studentProfileId") studentProfileId: String
    ): StudentAttendanceModel?

    @GET("finance/summary")
    suspend fun getAllFinance(
        @HeaderMap headers: Map<String, String>,
        @Query("fromDate") fromDate: String,
        @Query("toDate") toDate: String
    ): FinanceModel?

    @GET("security/groups")
    suspend fun getSecurityGroupsList(
        @HeaderMap headers: Map<String, String>,
        @Query("module") module: String
    ): ArrayList<String>?

    @GET("finance/fees/payments-summary")
    suspend fun getCollection(
        @HeaderMap headers: Map<String, String>,
        @Query("groupBy") groupBy: String,
        @Query("fromDate") fromDate: String,
        @Query("toDate") toDate: String
    ): ArrayList<FeePayment>

    @GET("finance/expenses/summary")
    suspend fun getExpences(
        @HeaderMap headers: Map<String, String>,
        @Query("groupBy") groupBy: String,
        @Query("fromDate") fromDate: String,
        @Query("toDate") toDate: String
    ): ArrayList<FeePayment>

    @GET("classrooms/dropdown")
    suspend fun getClassRoomDropdowns(
        @HeaderMap headers: Map<String, String>
    ) : List<ClassDropDownModel>?

    @GET("academics/exams")
    suspend fun getExaminationDropDown(
        @HeaderMap headers: Map<String, String>,
        @Query("classroomId") classroomId: Int
    ) : List<ExaminationDropDownModel>?
    @GET("academics/exams")
    suspend fun getExaminationDropDownForParent(
        @HeaderMap headers: Map<String, String>
    ) : List<ExaminationDropDownModel>?

    @GET("academics/exams/schedule")
    suspend fun getExaminations(
        @HeaderMap headers: Map<String, String>,
        @Query("examId") examId: Int
    ) : ExaminationScheduleList?

    @GET("academics/exams/schedule")
    suspend fun getExaminationsTest(
        @HeaderMap headers: Map<String, String>,
        @Query("testId") testId: Int
    ) : ExaminationScheduleList?

    @GET("mobile/students")
    suspend fun getStudentListByClassId(
        @HeaderMap headers: Map<String, String>,
        @Query("classroomId") classroomId: Int
    ) : ArrayList<StudentListModel>?

    @GET("employees/all")
    suspend fun getEmployeeList(
        @HeaderMap headers: Map<String, String>
    ) : ArrayList<EmployeeList>?

    @PUT("employees/mobile")
    suspend fun updateEmployeeProfile(
        @HeaderMap headers: Map<String, String>,
        @Body jsonObject : EmployeeList
    ) : EmployeeList?

    @GET("mobile/students/profile")
    suspend fun getStudentProfile(
        @HeaderMap headers: Map<String, String>
    ) : StudentListModel?

    @GET("noticeboard/")
    suspend fun getNotices(
        @HeaderMap headers: Map<String, String>,
        @Query("status") showExpired: String
    ) : NoticeModel?

    @PUT("mobile/students/{studentProfileId}")
    suspend fun UpdateStudentProfile(
        @HeaderMap headers: Map<String, String>,
        @Path("studentProfileId") studentProfileId : Int,
        @Body jsonObject : StudentListModel
    )
    @PUT("students/{studentProfileId}/image")
    suspend fun uploadImage(
        @HeaderMap headers: Map<String, String>,
        @Path("studentProfileId") studentProfileId : Int
    ) : ImageModel?

    @PUT("employees/{employeeProfileId }/image")
    suspend fun uploadEmployeeImage(
        @HeaderMap headers: Map<String, String>,
        @Path("employeeProfileId ") employeeProfileId  : Int
    ) : ImageModel?

    @PUT("academics/exams/schedule/{scheduleId}")
    suspend fun updateExamSchedule(
        @HeaderMap headers: Map<String, String>,
        @Path("scheduleId") scheduleId  : Int,
        @Body jsonObject : ExamPostModel

    ) : ImageModel?

    @POST("mobile/students")
    suspend fun CreateStudentProfile(
        @HeaderMap headers: Map<String, String>,
        @Body jsonObject : StudentListModel
    )

    @POST("academics/exams/schedule")
    suspend fun createExamSchedule(
        @HeaderMap headers: Map<String, String>,
        @Body jsonObject : ExamPostModel
    )

    @Headers("Content-Type: image/jpg")
    @PUT
    fun aws_upload(
        @Url path: String,
        @Body image: RequestBody
    ): Call<Void>

    @DELETE("noticeboard/{id}")
    suspend fun deleteNotice(@HeaderMap headers: Map<String, String>,
                             @Path("id") id : Int) : Boolean

    @DELETE("academics/exams/schedule/{scheduleId}")
    suspend fun deleteExamSchedule(@HeaderMap headers: Map<String, String>,
                             @Path("scheduleId") scheduleId  : Int)

    @POST("noticeboard")
    suspend fun createNotice(@HeaderMap headers: Map<String, String>,
                             @Body jsonObject: NoticeBoardModel)

    @POST("academics/marks")
    suspend fun addOrUpdateStudentMarks(@HeaderMap headers: Map<String, String>,
                             @Body jsonObject: ArrayList<StundentsMarksList>) : ArrayList<StundentsMarksList>?

    @PUT("noticeboard/{scheduleId}")
    suspend fun updateNotice(@HeaderMap headers: Map<String, String>,
                             @Path("scheduleId") scheduleId  : Int,
                             @Body jsonObject: NoticeBoardModel) : NoticeBoardModel

    @GET("subjects")
    suspend fun getSubjectListByClassId(@HeaderMap headers: Map<String, String>,
                                        @Query("classroomId") classroomId : Int) : List<SubjectListModel>?



    @GET("academics/assessments/skills")
    suspend fun getSkillList(@HeaderMap headers: Map<String, String>,
                                        @Query("classroomId") classroomId : Int,
                             @Query("subjectId") subjectId : Int,
                             @Query("area") area  : String) : List<SkillListModel>?


    @GET("academics/marks")
    suspend fun getStudentMarksList(@HeaderMap headers: Map<String, String>,
                                        @Query("classroomId") classroomId : Int,
                                    @Query("examId") examId  : Int,
                                    @Query("subjectId") subjectId : Int,
                                    @Query("skillId") skillId : Int) : List<StundentsMarksList>?

    @GET("academics/marks")
    suspend fun getStudentMarksListTest(@HeaderMap headers: Map<String, String>,
                                        @Query("classroomId") classroomId : Int,
                                        @Query("examId") examId  : Int,
                                        @Query("testId") testId  : Int,
                                        @Query("subjectId") subjectId : Int,
                                        @Query("skillId") skillId : Int) : List<StundentsMarksList>?



    @GET("attendance/shifts")
    suspend fun getShiftList(@HeaderMap headers: Map<String, String>,
                             @Query("profileType") profileType : String,
                             @Query("classroomIdList") classroomIdList : ArrayList<Int>?) : ArrayList<ShiftList>?

    @GET("finance/fees/outstandingFeeHeads")
    suspend fun getFeeHeadTermList(@HeaderMap headers: Map<String, String>,
                             @Query("dueDate") dueDate : String) : ArrayList<FeeHeadTermList>?
    @GET("finance/fees/outstandingFeeHeads")
    suspend fun getFeeHeadTermListOverDue(@HeaderMap headers: Map<String, String>): ArrayList<FeeHeadTermList>?

    @POST("finance/fees/due-report")
    suspend fun getPendingFeeStudentList(@HeaderMap headers: Map<String, String>,
                             @Body model : PendingFeeStudentRequest) : FeeStudentObject?

    @PUT("attendance/students/absent-report")
    suspend fun getAbsentStudentList(@HeaderMap headers: Map<String, String>,
                                     @Body requestDTO  : GetAbsentRequest) : ArrayList<AbsentStudentList>?

    @PUT("attendance/employees/absent-report")
    suspend fun getAbsentEmployeeList(@HeaderMap headers: Map<String, String>,
                                      @Body requestDTO  : GetAbsentRequest) : ArrayList<AbsentStudentList>?

    @PUT("attendance/students/lateEntry-report")
    suspend fun getLateStudentList(@HeaderMap headers: Map<String, String>,
                                      @Body requestDTO  : GetAbsentRequest) : ArrayList<AbsentStudentList>?

    @PUT("attendance/employee/lateEntry-report")
    suspend fun getLateEmployeeList(@HeaderMap headers: Map<String, String>,
                                      @Body requestDTO  : GetAbsentRequest) : ArrayList<AbsentStudentList>?

    @POST("notifications/attendance")
    suspend fun sendAttendanceReport(@HeaderMap headers: Map<String, String>,
                                     @Body jsonObject : GetAbsentRequest) : SendAbsentReportModel?

    @POST("notifications/general-notification")
    suspend fun sendGeneralNotification(@HeaderMap headers: Map<String, String>,
                                     @Body jsonObject : SendGeneralNotification) : SendGeneralNotification?

    @GET("util/lookup")
    suspend fun getEmployeeDepartments(@HeaderMap headers: Map<String, String>,
                                       @Query("fieldName") fieldName : String,
                                       @Query("isDropdown") isDropdown : Boolean
                                       ) : ArrayList<EmployeeDepartmentList>?

    @GET("employees/byDept")
    suspend fun getEmployeeProfilesByDepartmentList(@HeaderMap headers: Map<String, String>,
                                                    @Query("deptList") deptList : ArrayList<String>?) : ArrayList<EmployeeList>?
    @GET("finance/fees")
    suspend fun getFeeDetail(@HeaderMap headers: Map<String, String>,
                                                    @Query("studentProfileId") studentProfileId : Int) : ArrayList<FeesDetailModel>?
    @GET("finance/fees/receipt")
    suspend fun getInVoice(@HeaderMap headers: Map<String, String>,
                                                    @Query("invoiceNumber") invoiceNumber : Int) : InvoiceModel?
    @GET("finance/fees/payments")
    suspend fun getPaymentHistory(@HeaderMap headers: Map<String, String>,
                                                    @Query("studentProfileId") studentProfileId : Int) : ArrayList<PaymentHistoryModel>?
    @GET("util/lookup/dropdown")
    suspend fun getPaymentMethod(@HeaderMap headers: Map<String, String>,
                                                    @Query("fieldName") fieldName : String) : ArrayList<String>?

    @GET("finance/bankAccounts/school")
    suspend fun getBankAccountList(@HeaderMap headers: Map<String, String>) : ArrayList<BankAccount>?


    @GET("academics/marks/byStudent")
    suspend fun getStudentMarksByProfileId(@HeaderMap headers: Map<String, String>,
                                           @Query("examId") examId : Int,
                                           @Query("studentProfileId") studentProfileId : Int) : MarksCategoryList?
    @GET("academics/marks/student/graph")
    suspend fun getStudentMarksGraph(@HeaderMap headers: Map<String, String>,
                                           @Query("studentProfileId") studentProfileId : Int) : ArrayList<MarksGraphModel>?

//    @GET("")
//    suspend fun getMarksStudentList(@HeaderMap headers: Map<String, String>) : ArrayList<StudentListModel>?


    @POST("finance/fees/payments")
    suspend fun addFeePayment(@HeaderMap headers: Map<String, String>,@Body paymentRequest: PaymentRequest)


    @POST("pg/redirect-properties")
    suspend fun paymentGatewayRedirect(@HeaderMap headers: Map<String, String>,@Body model: PaymentGatewayRequest) : PaymentGatewayResponse

    @POST("pg/postPayment")
    suspend fun sendPayUResponseToServer(@HeaderMap headers: Map<String, String>,@Body model: JsonObject)


}