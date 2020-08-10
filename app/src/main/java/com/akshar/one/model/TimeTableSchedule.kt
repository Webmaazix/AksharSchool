package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class TimeTableSchedule : Serializable {

    @SerializedName("weekDay")
    @Expose
    var weekDay =""

    @SerializedName("periodNumber")
    @Expose
    var periodNumber =""

    @SerializedName("subjectName")
    @Expose
    var subjectName =""

    @SerializedName("startTime")
    @Expose
    var startTime =""

    @SerializedName("endTime")
    @Expose
    var endTime =""

    @SerializedName("classroom")
    @Expose
    var classroom = ClassRoom()

    @SerializedName("teacher")
    @Expose
    var teacher = TeacherModel()
}