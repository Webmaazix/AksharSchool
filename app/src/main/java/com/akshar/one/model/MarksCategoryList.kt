package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MarksCategoryList : Serializable {

    @SerializedName("examName")
    @Expose
    var examName = ""
    @SerializedName("testList")
    @Expose
    var testList = ArrayList<MarksTestListParent>()

    @SerializedName("scholasticMarksList")
    @Expose
    var scholasticMarksList = ArrayList<ScholasticMarksList>()
}