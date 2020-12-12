package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SkillListParent : Serializable {

    @SerializedName("skillName")
    @Expose
    var skillName = ""
    @SerializedName("grade")
    @Expose
    var grade = ""

}