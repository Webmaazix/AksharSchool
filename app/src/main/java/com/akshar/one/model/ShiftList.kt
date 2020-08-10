package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ShiftList : Serializable {

    @SerializedName("isSelected")
    @Expose
    var isSelected = false
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
    @SerializedName("name")
    @Expose
    var shiftName = ""
    @SerializedName("attendanceInd")
    @Expose
    var attendanceInd = ""
    @SerializedName("endTime")
    @Expose
    var endTime = ""
    @SerializedName("profileId")
    @Expose
    var profileId = ""
    @SerializedName("startTime")
    @Expose
    var startTime = ""
    @SerializedName("profileType")
    @Expose
    var profileType = ""
}