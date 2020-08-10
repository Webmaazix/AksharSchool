package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SubCategoryList : Serializable {

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

    @SerializedName("skillList")
    @Expose
    var skillList = ArrayList<SkillSetModel>()
}