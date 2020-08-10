package com.akshar.one.repository.marksentry

import com.akshar.one.api.service.AksharSchoolService
import com.akshar.one.api.service.ApiInterface
import com.akshar.one.model.StudentListModel
import com.akshar.one.model.StundentsMarksList
import com.akshar.one.repository.base.BaseRepository
import org.json.JSONObject

class MarksEntryRepository : BaseRepository() {

    private var apiInterface : ApiInterface? = null
    private var service : AksharSchoolService = AksharSchoolService()

    init {
        apiInterface = service.createService(ApiInterface::class.java)
    }

    suspend fun getSubjectListByClassId(classRoomId : Int) = apiInterface?.getSubjectListByClassId(service.headers(),classRoomId)

    suspend fun addOrUpdateStudentMarks(studentMarksList : ArrayList<StundentsMarksList>) = apiInterface?.addOrUpdateStudentMarks(service.headers(),studentMarksList)

    suspend fun getSkillList(classRoomId : Int,subjectId: Int,area : String) = apiInterface?.getSkillList(service.headers(),
        classRoomId,subjectId,area)
    suspend fun getStudentMarksList(classRoomId : Int,
                                    examId : Int,subjectId : Int,
                                    skillId : Int) = apiInterface?.getStudentMarksList(service.headers(),classRoomId,
        examId,subjectId,skillId)

    suspend fun getStudentMarksListTest(classRoomId : Int,examId : Int,
                                    testId : Int,subjectId : Int,
                                    skillId : Int) = apiInterface?.getStudentMarksListTest(service.headers(),classRoomId,examId,
        testId,subjectId,skillId)

    suspend fun CreateStudentProfile(jsonObject: StudentListModel) = apiInterface?.CreateStudentProfile(service.headers(),jsonObject)

}