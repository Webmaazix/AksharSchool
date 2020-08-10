package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SubjectListModel : Serializable {

    @SerializedName("classroomSubjectId")
    @Expose
    var classroomSubjectId = 0
    @SerializedName("subjectId")
    @Expose
    var subjectId = 0
    @SerializedName("subjectName")
    @Expose
    var subjectName = ""
    @SerializedName("isActive")
    @Expose
    var isActive = false
    @SerializedName("examOrder")
    @Expose
    var examOrder = 0
    @SerializedName("isOptionalSubject")
    @Expose
    var isOptionalSubject = false
    @SerializedName("teacher")
    @Expose
    var teacher = TeacherModel()
}