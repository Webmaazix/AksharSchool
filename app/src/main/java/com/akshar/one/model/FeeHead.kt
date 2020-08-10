package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class FeeHead : Serializable {

    @SerializedName("feeHeadSetupId")
    @Expose
    var feeHeadSetupId = 0
    @SerializedName("feeHead")
    @Expose
    var feeHead = ""
    @SerializedName("transportFlag")
    @Expose
    var transportFlag = ""

}