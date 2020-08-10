package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class FeeTerm : Serializable {

    @SerializedName("feeTermSetupId")
    @Expose
    var feeTermSetupId = 0
    @SerializedName("feeTerm")
    @Expose
    var feeTerm = ""
}