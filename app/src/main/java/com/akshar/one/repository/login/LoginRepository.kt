package com.akshar.one.repository.login

import com.akshar.one.api.service.AksharSchoolService
import com.akshar.one.api.service.ApiInterface
import com.akshar.one.repository.base.BaseRepository

class LoginRepository : BaseRepository() {

    private var apiInterface: ApiInterface? = null
    private var service: AksharSchoolService = AksharSchoolService()

    init {
        apiInterface = service.createService(ApiInterface::class.java)
    }

    suspend fun loginWithUsername(username: String, password: String) = apiInterface?.loginWithUsername(username, password)

    suspend fun getOTP(mobileNumber: String) = apiInterface?.getOTP(mobileNumber)

    suspend fun loginWithOTP(mobileNumber: String, otp: String) = apiInterface?.loginWithOTP(mobileNumber, otp)

    suspend fun changePassword(username: String, password: String) = apiInterface?.changePassword(service.headers(), username, password)
}