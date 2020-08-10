package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SendMarksReportRequest : Serializable {

    @SerializedName("classroomId")
    @Expose
    var classroomId = 0
    @SerializedName("examId")
    @Expose
    var examId = 0
    @SerializedName("notificationType")
    @Expose
    var notificationType = ""

    @SerializedName("studentProfileidList")
    @Expose
    var studentProfileidList = 0
}