package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class  ScholasticMarksList : Serializable{

    @SerializedName("subjectName")
    @Expose
    var subjectName = ""
    @SerializedName("categorySkillList")
    @Expose
    var categorySkillList = ArrayList<CategoryListParent>()
}