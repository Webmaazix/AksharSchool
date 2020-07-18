package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ScheduleList : Serializable {

    @SerializedName("id")
    @Expose
    var id = 0

    @SerializedName("subjectId")
    @Expose
    var subjectId = ""

    @SerializedName("subjectName")
    @Expose
    var subjectName = ""

    @SerializedName("date")
    @Expose
    var date = ""

    @SerializedName("startTime")
    @Expose
    var startTime = ""

    @SerializedName("endTime")
    @Expose
    var endTime = ""

    @SerializedName("duration")
    @Expose
    var duration = 0
}