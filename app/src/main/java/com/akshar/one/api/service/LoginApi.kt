package com.akshar.one.api.service

import com.akshar.one.model.LoginModel
import com.akshar.one.model.OTPModel
import retrofit2.http.*

interface LoginApi {

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun loginWithUsername(
        @Field("username") username: String,
        @Field("password") password: String
    ): LoginModel?

    @POST("auth/otp/send")
    suspend fun getOTP(
        @Query("mobile") mobileNumber: String
    ): OTPModel?

    @FormUrlEncoded
    @POST("auth/validateOtp")
    suspend fun loginWithOTP(
        @Field("username") mobileNumber: String,
        @Field("password") otp: String
    ): LoginModel?

    @FormUrlEncoded
    @POST("auth/loginCredentials/changePassword")
    suspend fun changePassword(
        @HeaderMap headers: Map<String, String>,
        @Field("username") username: String,
        @Field("password") password: String
    )
}