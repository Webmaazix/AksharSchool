package com.akshar.one.manager

import android.content.SharedPreferences
import com.akshar.one.util.AppUtil
import java.lang.reflect.Type

class SharedPreferenceManager constructor(private val sharedPreferences: SharedPreferences) {

    companion object {
        const val SHARED_PREF_FILE_NAME = "akshar_school_shared_pre_file"
        const val LOGIN_MODEL = "prefLoginModel"
        const val ROLE = "prefLoginRole"
        const val securityList = "prefSecurityList"
    }

    fun putObject( key: String, model: Any) {
        //Save that String in SharedPreferences
        sharedPreferences.edit().putString(key, AppUtil.jsonFromModel(model).toString()).apply()
    }

    fun getObject(key: String, type: Type): Any? {
        val value = sharedPreferences.getString(key, null)
        return AppUtil.fromJson(value,type)
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}