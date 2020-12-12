package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SubjectListParent : Serializable {

    @SerializedName("subjectName")
    @Expose
    var subjectName = ""
    @SerializedName("marksScored")
    @Expose
    var marksScored = ""
    @SerializedName("maxMarks")
    @Expose
    var maxMarks = ""
    @SerializedName("grade")
    @Expose
    var grade = ""
    @SerializedName("gradePoint")
    @Expose
    var gradePoint = ""
    @SerializedName("categorySkillList")
    @Expose
    var categorySkillList = ArrayList<CategoryListParent>()
    @SerializedName("skillList")
    @Expose
    var skillList = ""
}