package com.akshar.one.util

import com.akshar.one.api.response.ErrorResponse
import com.google.gson.Gson
import org.json.JSONObject
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

object AppUtil {

    const val CALENDAR_DAY_FORMAT = "EEE"
    const val CALENDAR_DATE_FORMAT = "dd"

    const val SERVER_DATE_FORMAT = "yyyy-MM-dd"

    fun fromJson(jsonString: String?, type: Type): Any? {
        return Gson().fromJson(jsonString, type)
    }

    fun getErrorResponse(jsonString: String?): ErrorResponse? {
        return fromJson(jsonString, ErrorResponse::class.java) as ErrorResponse?
    }

    @Suppress("IMPLICIT_CAST_TO_ANY")
    fun jsonFromModel(model: Any): JSONObject {
        if (model is String) {
            return JSONObject(Gson().toJson(JSONObject(model)))
        }
        return JSONObject(Gson().toJson(model))
    }

    fun getServerDateFormat(date:Date): String = SimpleDateFormat(SERVER_DATE_FORMAT).format(date)

}