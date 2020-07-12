package com.akshar.one.repository.student

import com.akshar.one.api.service.AksharSchoolService
import com.akshar.one.api.service.ApiInterface
import com.akshar.one.model.StudentListModel
import com.akshar.one.repository.base.BaseRepository
import org.json.JSONObject

class StudentRepository : BaseRepository() {

    private var apiInterface : ApiInterface? = null
    private var service : AksharSchoolService = AksharSchoolService()

    init {
        apiInterface = service.createService(ApiInterface::class.java)
    }

    suspend fun getStudentListByClassId(classRoomId : Int) = apiInterface?.getStudentListByClassId(service.headers(),classRoomId)

    suspend fun CreateStudentProfile(jsonObject: StudentListModel) = apiInterface?.CreateStudentProfile(service.headers(),jsonObject)

    suspend fun uploadImage(studentId: Int) = apiInterface?.uploadImage(service.headers(),studentId)

    suspend fun UpdateStudentProfile(studentId : Int, jsonObject: StudentListModel) = apiInterface?.UpdateStudentProfile(service.headers(),studentId,jsonObject)
}