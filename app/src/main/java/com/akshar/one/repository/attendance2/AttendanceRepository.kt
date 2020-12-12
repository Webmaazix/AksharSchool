package com.akshar.one.repository.attendance2

import com.akshar.one.api.service.AksharSchoolService
import com.akshar.one.api.service.ApiInterface
import com.akshar.one.api.service.AttendanceApi
import com.akshar.one.model.*
import com.akshar.one.repository.base.BaseRepository
import org.json.JSONObject

class AttendanceRepository : BaseRepository() {

    private var apiInterface : AttendanceApi? = null
    private var service : AksharSchoolService = AksharSchoolService()

    init {
        apiInterface = service.createService(AttendanceApi::class.java)
    }

    suspend fun getShiftList(profileType : String , classRoomIdList : ArrayList<Int>) = apiInterface?.getShifts(service.headers(),
        profileType,classRoomIdList)
    suspend fun getShiftListEmployee(profileType : String) = apiInterface?.getShiftListEmployee(service.headers(),
        profileType)
    suspend fun getStudentAttendanceByClassRoomId(classRoomId  : Int, shiftId : Int,date : String) =
        apiInterface?.getStudentsAttendanceByClassRoomId(service.headers(),classRoomId,shiftId,date)
    suspend fun getEmployeeAttendance(shiftId : Int,date : String) =
        apiInterface?.getEmployeeAttendance(service.headers(),shiftId,date)

    suspend fun saveStudentsAttendance(profileType  : String, studentAttendanceList : ArrayList<StudentAttendanceModel>) =
        apiInterface?.saveStudentsAttendance(service.headers(),profileType,studentAttendanceList)

}