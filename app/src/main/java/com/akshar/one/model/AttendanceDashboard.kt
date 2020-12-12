package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class AttendanceDashboard : Serializable {

    @SerializedName("key")
    @Expose
    var key = ""
    @SerializedName("value")
    @Expose
    var value = ""
}