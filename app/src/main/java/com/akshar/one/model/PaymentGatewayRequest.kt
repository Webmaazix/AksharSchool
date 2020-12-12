package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PaymentGatewayRequest : Serializable {

    @SerializedName("gateway")
    @Expose
    var gateway = ""
    @SerializedName("custId")
    @Expose
    var custId = ""
    @SerializedName("custName")
    @Expose
    var custName = ""
    @SerializedName("custEmail")
    @Expose
    var custEmail = ""
    @SerializedName("custMobile")
    @Expose
    var custMobile = ""
    @SerializedName("product")
    @Expose
    var product = ""
    @SerializedName("amount")
    @Expose
    var amount = ""
    @SerializedName("paymentSplits")
    @Expose
    var paymentSplits = ArrayList<PaymentSplits>()
}