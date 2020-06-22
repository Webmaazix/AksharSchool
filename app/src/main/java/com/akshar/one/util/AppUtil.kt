package com.akshar.one.util

import com.akshar.one.api.response.ErrorResponse
import com.google.gson.Gson
import org.json.JSONObject
import java.lang.reflect.Type

object AppUtil {

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

}