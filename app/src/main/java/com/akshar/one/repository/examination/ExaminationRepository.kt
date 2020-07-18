package com.akshar.one.repository.examination

import com.akshar.one.api.service.AksharSchoolService
import com.akshar.one.api.service.ApiInterface
import com.akshar.one.model.ExamPostModel
import com.akshar.one.model.StudentListModel
import com.akshar.one.repository.base.BaseRepository
import org.json.JSONObject

class ExaminationRepository : BaseRepository() {

    private var apiInterface: ApiInterface? = null
    private var service: AksharSchoolService = AksharSchoolService()

    init {
        apiInterface = service.createService(ApiInterface::class.java)
    }

    suspend fun getExaminationDropDown(classRoomId : Int) = apiInterface?.getExaminationDropDown(service.headers(),classRoomId)
    suspend fun getExaminations(examId : Int) = apiInterface?.getExaminations(service.headers(),examId)
    suspend fun getExaminationsTest(testId : Int) = apiInterface?.getExaminationsTest(service.headers(),testId)
    suspend fun createExamSchedule(examModel : ExamPostModel) = apiInterface?.createExamSchedule(service.headers(),examModel)
    suspend fun updateExamSchedule(ScheduleId : Int,examModel : ExamPostModel) = apiInterface?.updateExamSchedule(service.headers(),ScheduleId,examModel)
    suspend fun deleteExamSchedule(ScheduleId : Int) = apiInterface?.deleteExamSchedule(service.headers(),ScheduleId)

}