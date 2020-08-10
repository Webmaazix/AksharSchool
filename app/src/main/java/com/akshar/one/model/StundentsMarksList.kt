package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class StundentsMarksList : Serializable {

    @SerializedName("id")
    @Expose
    var id = 0
    @SerializedName("studentProfileId")
    @Expose
    var studentProfileId = 0
    @SerializedName("studentName")
    @Expose
    var studentName = ""
    @SerializedName("rollNbr")
    @Expose
    var rollNbr = ""
    @SerializedName("examId")
    @Expose
    var examId = 0
    @SerializedName("examName")
    @Expose
    var examName = ""
    @SerializedName("testId")
    @Expose
    var testId = 0
    @SerializedName("testName")
    @Expose
    var testName = ""
    @SerializedName("subjectId")
    @Expose
    var subjectId = 0
    @SerializedName("skillId")
    @Expose
    var skillId = 0
    @SerializedName("marksScored")
    @Expose
    var marksScored : String? = null
    @SerializedName("maxMarks")
    @Expose
    var maxMarks = ""
    @SerializedName("grade")
    @Expose
    var grade = ""
    @SerializedName("gradePoint")
    @Expose
    var gradePoint = ""
}