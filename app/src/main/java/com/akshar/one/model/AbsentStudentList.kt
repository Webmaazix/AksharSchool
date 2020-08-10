package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class AbsentStudentList : Serializable {

    @SerializedName("isSelected")
    @Expose
    var isSelected = false
    @SerializedName("rollNbr")
    @Expose
    var rollNbr = ""
    @SerializedName("admissionNbr")
    @Expose
    var admissionNbr = ""
    @SerializedName("employeeId")
    @Expose
    var employeeId = ""
    @SerializedName("fullName")
    @Expose
    var fullName = ""
    @SerializedName("parentFullName")
    @Expose
    var parentFullName = ""
    @SerializedName("date")
    @Expose
    var date = ""
    @SerializedName("shiftId")
    @Expose
    var shiftId = 0
    @SerializedName("shiftName")
    @Expose
    var shiftName = ""
    @SerializedName("attendanceInd")
    @Expose
    var attendanceInd = ""
    @SerializedName("profileId")
    @Expose
    var profileId = 0
    @SerializedName("inTime")
    @Expose
    var inTime = ""
    @SerializedName("outTime")
    @Expose
    var outTime = ""
    @SerializedName("lateEntryFlag")
    @Expose
    var lateEntryFlag = ""
    @SerializedName("mobileList")
    @Expose
    var mobileList = ArrayList<String>()
    @SerializedName("emailList")
    @Expose
    var emailList = ArrayList<String>()

    @SerializedName("appIdList")
    @Expose
    var appIdList = ArrayList<String>()
}