package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PeriodTimeTable : Serializable {

    @SerializedName("periodNumber")
    @Expose
    var periodNumber = 0

    @SerializedName("timetable")
    @Expose
    var timetable = ArrayList<TimeTableSchedule>()
}