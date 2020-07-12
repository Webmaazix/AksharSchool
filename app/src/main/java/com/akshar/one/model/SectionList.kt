package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

 class SectionList : Serializable{
    @SerializedName("classroomId")
    @Expose
    var classroomId = 0

    @SerializedName("degreeName")
    @Expose
    var degreeName = ""

    @SerializedName("deptName")
    @Expose
    var deptName = ""

    @SerializedName("courseName")
    @Expose
    var courseName = ""

    @SerializedName("classroomName")
    @Expose
    var classroomName = ""

    @SerializedName("displayOrder")
    @Expose
    var displayOrder = 0
}