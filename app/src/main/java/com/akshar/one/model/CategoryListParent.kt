package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CategoryListParent : Serializable {

    @SerializedName("categoryName")
    @Expose
    var categoryName = ""
    @SerializedName("subCategorySkillList")
    @Expose
    var subCategorySkillList = ArrayList<SubCategoryListParent>()
    @SerializedName("skillList")
    @Expose
    var skillList = ArrayList<SkillListParent>()
}