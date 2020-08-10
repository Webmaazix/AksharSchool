package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SkillListModel : Serializable {

    @SerializedName("categoryId")
    @Expose
    var categoryId = 0
    @SerializedName("area")
    @Expose
    var area = ""
    @SerializedName("categoryName")
    @Expose
    var categoryName = ""
    @SerializedName("displayOrder")
    @Expose
    var displayOrder = 0
    @SerializedName("subCategoriesList")
    @Expose
    var subCategoriesList = ArrayList<SubCategoryList>()

}