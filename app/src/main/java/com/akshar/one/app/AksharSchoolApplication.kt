package com.akshar.one.app

import android.app.Application
import android.content.Context
import com.akshar.one.manager.SharedPreferenceFactory
import com.akshar.one.manager.SharedPreferenceManager
import com.akshar.one.view.feeandpayments.AppEnvironment

class AksharSchoolApplication : Application(){

    companion object{
        internal lateinit var appEnvironment: AppEnvironment
        var context:Context? = null
        fun getAppContext(): Context? = context
        fun getAppEnvironment(): AppEnvironment {
            return appEnvironment
        }

        fun setAppEnvironment(appEnvironment: AppEnvironment) {
            this.appEnvironment = appEnvironment
        }

    }

    override fun onCreate() {
        super.onCreate()
        appEnvironment = AppEnvironment.SANDBOX
        context = this
        initializeSharedPreference()
    }





    private fun initializeSharedPreference() {
        val sharedPreferenceManager = SharedPreferenceManager(getSharedPreferences(SharedPreferenceManager.SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE))
        SharedPreferenceFactory.setSharedPreferenceManager(sharedPreferenceManager)
    }

}