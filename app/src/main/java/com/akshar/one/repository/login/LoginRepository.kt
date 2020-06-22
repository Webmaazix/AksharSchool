package com.akshar.one.repository.login

import com.akshar.one.api.service.AksharSchoolService
import com.akshar.one.api.service.LoginApi
import com.akshar.one.repository.base.BaseRepository

class LoginRepository : BaseRepository() {

    private var loginApi: LoginApi? = null
    private var service: AksharSchoolService = AksharSchoolService()

    init {
        loginApi = service.createService(LoginApi::class.java)
    }

    suspend fun loginWithUsername(username: String, password: String) = loginApi?.loginWithUsername(username, password)

    suspend fun getOTP(mobileNumber: String) = loginApi?.getOTP(mobileNumber)

    suspend fun loginWithOTP(mobileNumber: String, otp: String) = loginApi?.loginWithOTP(mobileNumber, otp)

    suspend fun changePassword(username: String, password: String) = loginApi?.changePassword(service.headers(), username, password)
}