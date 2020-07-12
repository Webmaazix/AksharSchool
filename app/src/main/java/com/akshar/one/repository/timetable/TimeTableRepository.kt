package com.akshar.one.repository.timetable

import com.akshar.one.api.service.AksharSchoolService
import com.akshar.one.api.service.ApiInterface
import com.akshar.one.repository.base.BaseRepository

class TimeTableRepository : BaseRepository() {

    private var apiInterface : ApiInterface? =null
    private var service  : AksharSchoolService = AksharSchoolService()

    init {
        apiInterface = service.createService(ApiInterface::class.java)
    }

    suspend fun getClassroomDropdown()  = apiInterface?.getClassRoomDropdowns(service.headers())


}