package com.akshar.one.repository.employee

import com.akshar.one.api.service.AksharSchoolService
import com.akshar.one.api.service.ApiInterface
import com.akshar.one.model.EmployeeList
import com.akshar.one.model.StudentListModel
import com.akshar.one.repository.base.BaseRepository
import org.json.JSONObject

class Employeerepository : BaseRepository() {

    private var apiInterface : ApiInterface? = null
    private var service : AksharSchoolService = AksharSchoolService()

    init {
        apiInterface = service.createService(ApiInterface::class.java)
    }

    suspend fun getEmployeeList() = apiInterface?.getEmployeeList(service.headers())

    suspend fun CreateStudentProfile(jsonObject: StudentListModel) = apiInterface?.CreateStudentProfile(service.headers(),jsonObject)

    suspend fun uploadEmployeeImage(employeeProfileId : Int) = apiInterface?.uploadEmployeeImage(service.headers(),employeeProfileId )

    suspend fun updateEmployeeProfile(jsonObject: EmployeeList) = apiInterface?.updateEmployeeProfile(service.headers(),jsonObject)
}