package com.akshar.one.repository.noticeboard

import com.akshar.one.api.service.AksharSchoolService
import com.akshar.one.api.service.ApiInterface
import com.akshar.one.model.StudentListModel
import com.akshar.one.repository.base.BaseRepository
import org.json.JSONObject

class NoticeBoardRepository : BaseRepository() {

    private var apiInterface : ApiInterface? = null
    private var service : AksharSchoolService = AksharSchoolService()

    init {
        apiInterface = service.createService(ApiInterface::class.java)
    }

    suspend fun getAllNotices(showExpired : Boolean) = apiInterface?.getNotices(service.headers(),showExpired)

}