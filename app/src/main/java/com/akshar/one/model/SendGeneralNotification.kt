package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SendGeneralNotification : Serializable {

    @SerializedName("subject")
    @Expose
    var subject = ""
    @SerializedName("body")
    @Expose
    var body = ""
    @SerializedName("type")
    @Expose
    var type = ""
    @SerializedName("examinationId")
    @Expose
    var examinationId = 0
    @SerializedName("testId")
    @Expose
    var testId = 0
    @SerializedName("scheduledDateTime")
    @Expose
    var scheduledDateTime = ""
    @SerializedName("classroomIdList")
    @Expose
    var classroomIdList = ArrayList<Int>()
    @SerializedName("studentProfileIdList")
    @Expose
    var studentProfileIdList = ArrayList<Int>()
    @SerializedName("employeeProfileIdList")
    @Expose
    var employeeProfileIdList = ArrayList<Int>()
    @SerializedName("recipientList")
    @Expose
    var recipientList = ArrayList<String>()
}