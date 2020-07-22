package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ExamPostModel : Serializable {

    @SerializedName("examId")
    @Expose
    var examId = 0
    @SerializedName("testId")
    @Expose
    var testId = 0
    @SerializedName("classroomId")
    @Expose
    var classroomId = 0
    @SerializedName("duration")
    @Expose
    var duration = 0
    @SerializedName("startDate")
    @Expose
    var startDate = ""
    @SerializedName("startTime")
    @Expose
    var startTime = ""

}