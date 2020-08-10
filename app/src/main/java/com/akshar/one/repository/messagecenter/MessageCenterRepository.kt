package com.akshar.one.repository.messagecenter

import com.akshar.one.api.service.AksharSchoolService
import com.akshar.one.api.service.ApiInterface
import com.akshar.one.model.GetAbsentRequest
import com.akshar.one.model.NoticeBoardModel
import com.akshar.one.model.SendGeneralNotification
import com.akshar.one.model.StudentListModel
import com.akshar.one.repository.base.BaseRepository
import org.json.JSONObject

class MessageCenterRepository : BaseRepository() {

    private var apiInterface : ApiInterface? = null
    private var service : AksharSchoolService = AksharSchoolService()

    init {
        apiInterface = service.createService(ApiInterface::class.java)
    }

    suspend fun getAbsentStudentList(getAbsentRequest : GetAbsentRequest) = apiInterface?.getAbsentStudentList(service.headers(),
        getAbsentRequest)
//    suspend fun getMarksStudentList() = apiInterface?.getMarksStudentList(service.headers())

    suspend fun getAbsentEmployeeList(getAbsentRequest : GetAbsentRequest) = apiInterface?.getAbsentEmployeeList(service.headers(),
        getAbsentRequest)

    suspend fun getLateEmployeeList(getAbsentRequest : GetAbsentRequest) = apiInterface?.getLateEmployeeList(service.headers(),
        getAbsentRequest)

    suspend fun getLateStudentList(getAbsentRequest : GetAbsentRequest) = apiInterface?.getLateStudentList(service.headers(),
        getAbsentRequest)


    suspend fun sendAttendanceReport(getAbsentRequest : GetAbsentRequest) = apiInterface?.sendAttendanceReport(service.headers(),
        getAbsentRequest)
    suspend fun sendGeneralNotification(sendGeneralNotification : SendGeneralNotification) = apiInterface?.sendGeneralNotification(service.headers(),
        sendGeneralNotification)
    suspend fun getEmployeeDepartments(designation : String, isDropDown : Boolean) =
        apiInterface?.getEmployeeDepartments(service.headers(),designation,isDropDown)

    suspend fun getEmployeeProfilesByDepartmentList(departmentList : ArrayList<String>) =
        apiInterface?.getEmployeeProfilesByDepartmentList(service.headers(),departmentList)

    suspend fun getShiftList(profileType : String , classRoomIdList : ArrayList<Int>?) = apiInterface?.getShiftList(service.headers(),
        profileType,classRoomIdList)
}