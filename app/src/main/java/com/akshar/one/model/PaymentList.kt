package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PaymentList : Serializable {

    @SerializedName("studentFeeId")
    @Expose
    var studentFeeId = 0
    @SerializedName("feeHead")
    @Expose
    var feeHead = ""
    @SerializedName("feeTerm")
    @Expose
    var feeTerm = ""
    @SerializedName("paymentAmount")
    @Expose
    var paymentAmount = 0.0
    @SerializedName("controlNumber")
    @Expose
    var controlNumber = 0
}