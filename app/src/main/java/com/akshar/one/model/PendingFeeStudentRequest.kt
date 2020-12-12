package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PendingFeeStudentRequest : Serializable {

    @SerializedName("requestType")
    @Expose
    var requestType = ""
    @SerializedName("dueDate")
    @Expose
    var dueDate = ""
    @SerializedName("classroomIdList")
    @Expose
    var classroomIdList = ArrayList<Int>()
    @SerializedName("termIdList")
    @Expose
    var termIdList = ArrayList<Int>()
}