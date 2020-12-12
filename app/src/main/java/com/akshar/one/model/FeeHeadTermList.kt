package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class FeeHeadTermList : Serializable {

    @SerializedName("feeHeadSetupId")
    @Expose
    var feeHeadSetupId = 0
    @SerializedName("headName")
    @Expose
    var headName = ""
    @SerializedName("transportFlag")
    @Expose
    var transportFlag = ""
    @SerializedName("termsList")
    @Expose
    var termsList = ArrayList<TermList>()

}