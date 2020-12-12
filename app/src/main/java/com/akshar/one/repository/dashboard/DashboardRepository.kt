package com.akshar.one.repository.dashboard

import com.akshar.one.api.service.AksharSchoolService
import com.akshar.one.api.service.ApiInterface
import com.akshar.one.repository.base.BaseRepository

class DashboardRepository : BaseRepository() {

    private var apiInterface: ApiInterface? = null
    private var service: AksharSchoolService = AksharSchoolService()

    init {
        apiInterface = service.createService(ApiInterface::class.java)
    }

    suspend fun getTimeTableOfEmployee(employeeId: Int, date: String) = apiInterface?.getTimeTable(service.headers(), employeeId, date)

    suspend fun getTimeTableOfClass(classRoomId: Int, date: String) = apiInterface?.getClassTimeTable(service.headers(), classRoomId, date)

    suspend fun getBirthdays(fromDate : String , toDate : String) = apiInterface?.getBirthdays(service.headers(),fromDate,toDate)
    suspend fun getAttendanceStatsOfStudent(fromDate : String , toDate : String,
                                            studentProfileId : String) = apiInterface?.getAttendanceStatsOfStudent(service.headers(),fromDate,toDate,studentProfileId)
    suspend fun getAttendanceStatsOfyear(studentProfileId : String) = apiInterface?.getAttendanceStatsOfyear(service.headers(),studentProfileId)
    suspend fun getPresentAttendance(studentProfileId : String) = apiInterface?.getPresentAttendance(service.headers(),studentProfileId)

    suspend fun getAllFinance(fromDate: String,toDate: String) = apiInterface?.getAllFinance(service.headers(),fromDate,toDate)
    suspend fun getSecurityGroupsList(appName: String) = apiInterface?.getSecurityGroupsList(service.headers(),appName)

    suspend fun getCollection(groupBy : String,fromDate: String,toDate: String) = apiInterface?.getCollection(service.headers(),groupBy,fromDate,toDate)

    suspend fun getExpences(groupBy: String,fromDate: String,toDate: String) = apiInterface?.getExpences(service.headers(),groupBy,fromDate,toDate)
}