package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PaymentListRequest : Serializable {

    @SerializedName("studentFeeId")
    @Expose
    var studentFeeId = 0
    @SerializedName("paymentAmount")
    @Expose
    var paymentAmount = 0.0
}