package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class StudentAttendanceModel : Serializable{

    @SerializedName("rollNbr")
    @Expose
    var rollNbr = ""
    @SerializedName("employeeId")
    @Expose
    var employeeId = ""
    @SerializedName("fullName")
    @Expose
    var fullName = ""
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
    @SerializedName("lateEntryFlag")
    @Expose
    var lateEntryFlag = ""
    @SerializedName("profileId")
    @Expose
    var profileId = 0
    @SerializedName("inTime")
    @Expose
    var inTime = ""
    @SerializedName("outTime")
    @Expose
    var outTime = ""
    @SerializedName("attendanceStatus")
    @Expose
    var attendanceStatus = ""
}