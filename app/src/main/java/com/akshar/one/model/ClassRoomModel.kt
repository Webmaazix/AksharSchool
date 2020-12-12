package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ClassRoomModel : Serializable {

    @SerializedName("classroomId")
    @Expose
    var classroomId = 0
    @SerializedName("classroomName")
    @Expose
    var classroomName = ""
    @SerializedName("displayOrder")
    @Expose
    var displayOrder = 0
    @SerializedName("courseName")
    @Expose
    var courseName = ""
    @SerializedName("degreeId")
    @Expose
    var degreeId = 0
    @SerializedName("degreeName")
    @Expose
    var degreeName = ""
    @SerializedName("deptId")
    @Expose
    var deptId = 0
    @SerializedName("deptName")
    @Expose
    var deptName = ""
    @SerializedName("courseId")
    @Expose
    var courseId = 0
}
