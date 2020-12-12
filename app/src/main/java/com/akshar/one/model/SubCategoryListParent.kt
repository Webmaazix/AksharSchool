package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SubCategoryListParent : Serializable {

    @SerializedName("subCategoryName")
    @Expose
    var subCategoryName = ""
    @SerializedName("skillList")
    @Expose
    var skillList = ArrayList<SkillListParent>()
}