package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ExaminationScheduleList : Serializable {

    @SerializedName("examId")
    @Expose
    var examId = 0
    @SerializedName("testId")
    @Expose
    var testId = 0
    @SerializedName("examinationName")
    @Expose
    var examinationName = ""
    @SerializedName("testName")
    @Expose
    var testName = ""
    @SerializedName("schedule")
    @Expose
    var schedule = ArrayList<ScheduleList>()
}