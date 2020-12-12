package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PaymentSplits : Serializable {

    @SerializedName("name")
    @Expose
    var name = ""
    @SerializedName("description")
    @Expose
    var description = ""
    @SerializedName("feeHeadSetupId")
    @Expose
    var feeHeadSetupId = 0L
    @SerializedName("studentFeeId")
    @Expose
    var studentFeeId = 0L
    @SerializedName("amount")
    @Expose
    var amount = 0.0
}