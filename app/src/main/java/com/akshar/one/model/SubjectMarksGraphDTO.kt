package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SubjectMarksGraphDTO : Serializable {

    @SerializedName("subjectId")
    @Expose
    var subjectId = 0
    @SerializedName("subjectName")
    @Expose
    var subjectName = ""
    @SerializedName("subjectPercentage")
    @Expose
    var subjectPercentage = 0
}