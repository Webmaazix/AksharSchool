package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class FeesDetailModel : Serializable {

    @SerializedName("academicYear")
    @Expose
    var academicYear = ""

    @SerializedName("feeHeadList")
    @Expose
    var feeHeadList = ArrayList<FeeHeadList>()


}