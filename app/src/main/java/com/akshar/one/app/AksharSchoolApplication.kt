package com.akshar.one.app

import android.app.Application
import android.content.Context
import com.akshar.one.manager.SharedPreferenceFactory
import com.akshar.one.manager.SharedPreferenceManager

class AksharSchoolApplication : Application(){

    companion object{
        var context:Context? = null
        fun getAppContext(): Context? = context
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        initializeSharedPreference()
    }

    private fun initializeSharedPreference() {
        val sharedPreferenceManager = SharedPreferenceManager(getSharedPreferences(SharedPreferenceManager.SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE))
        SharedPreferenceFactory.setSharedPreferenceManager(sharedPreferenceManager)
    }

}