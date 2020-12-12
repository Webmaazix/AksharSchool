package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class AppList : Serializable{

    @SerializedName("isSelected")
    @Expose
    var isSelected = false
    @SerializedName("selectedPosition")
    @Expose
    var selectedPosition = -1
    @SerializedName("id")
    @Expose
    var id = 0
    @SerializedName("username")
    @Expose
    var username = ""
    @SerializedName("appName")
    @Expose
    var appName = ""
    @SerializedName("orgCodes")
    @Expose
    var orgCodes = ""
    @SerializedName("url")
    @Expose
    var url = ""
    @SerializedName("name")
    @Expose
    var name = ""
    @SerializedName("schoolCode")
    @Expose
    var schoolCode = ""
    @SerializedName("className")
    @Expose
    var className = ""
    @SerializedName("classroomId")
    @Expose
    var classRoomId = 0
    @SerializedName("studentCount")
    @Expose
    var studentCount = 0
    @SerializedName("schoolName")
    @Expose
    var schoolName = ""
    @SerializedName("studentName")
    @Expose
    var studentName = ""
    @SerializedName("employeeCount")
    @Expose
    var employeeCount = 0
    @SerializedName("userUniqueId")
    @Expose
    var userUniqueId = 0
    @SerializedName("isDefault")
    @Expose
    var isDefault = false
}