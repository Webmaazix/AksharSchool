package com.akshar.one.api.service

import com.akshar.one.R
import com.akshar.one.app.AksharSchoolApplication
import com.akshar.one.manager.SessionManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class AksharSchoolService {

    private val AUTHORIZATION = "Authorization"
    private val APP_NAME = "APP_NAME"
    private val STUDENT_PROFILE_ID = "STUDENT_PROFILE_ID"
    private val SCHOOL_CD = "SCHOOL_CODE"
    private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }

    private val client: OkHttpClient = OkHttpClient.Builder().apply {
        this.addInterceptor(interceptor)
        this.connectTimeout(30, TimeUnit.MINUTES)
        this.readTimeout(30, TimeUnit.MINUTES)

    }.build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(AksharSchoolApplication.getAppContext()?.getString(R.string.BASE_URL))
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun <T> createService(serviceClass: Class<T>?): T {
        return retrofit.create(serviceClass)
    }

    fun headers(): Map<String, String> {
        if(SessionManager.getLoginRole()?.appName.equals("SmartParent")){
            return mapOf(Pair(AUTHORIZATION, SessionManager.getLoginModel()?.token ?: ""),
                Pair(APP_NAME,SessionManager.getLoginRole()?.appName ?: ""),
                Pair(STUDENT_PROFILE_ID,SessionManager.getLoginRole()?.userUniqueId.toString() ?: ""),
                Pair(SCHOOL_CD , SessionManager.getLoginRole()?.schoolCode ?: "")
            )

        }else {
            return mapOf(Pair(AUTHORIZATION, SessionManager.getLoginModel()?.token ?: ""),
                Pair(APP_NAME,SessionManager.getLoginRole()?.appName ?: ""),
                Pair(SCHOOL_CD , SessionManager.getLoginRole()?.schoolCode ?: "")
            )

        }
    }
}