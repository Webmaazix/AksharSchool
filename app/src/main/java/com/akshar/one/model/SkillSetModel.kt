package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SkillSetModel : Serializable {

    @SerializedName("skillId")
    @Expose
    var skillId = 0
    @SerializedName("classroomId")
    @Expose
    var classroomId = 0
    @SerializedName("subjectId")
    @Expose
    var subjectId = 0
    @SerializedName("area")
    @Expose
    var area = ""
    @SerializedName("categoryId")
    @Expose
    var categoryId = 0
    @SerializedName("categoryName")
    @Expose
    var categoryName = ""
    @SerializedName("subCategoryId")
    @Expose
    var subCategoryId = 0
    @SerializedName("subCategoryName")
    @Expose
    var subCategoryName = ""
    @SerializedName("skillName")
    @Expose
    var skillName = ""
    @SerializedName("displayOrder")
    @Expose
    var displayOrder = 0
}