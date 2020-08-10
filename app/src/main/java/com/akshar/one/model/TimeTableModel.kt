package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class TimeTableModel : Serializable{

    @SerializedName("weekday")
    @Expose
    var weekday  = ""

    @SerializedName("periodwiseTimetableList")
    @Expose
    var periodwiseTimetableList = ArrayList<PeriodTimeTable>()
}