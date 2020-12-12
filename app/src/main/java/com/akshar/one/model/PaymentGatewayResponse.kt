package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PaymentGatewayResponse : Serializable {

    @SerializedName("MOBILE_NO")
    @Expose
    var MOBILE_NO = ""
    @SerializedName("ORDER_ID")
    @Expose
    var ORDER_ID = ""
    @SerializedName("TXN_AMOUNT")
    @Expose
    var TXN_AMOUNT = ""
    @SerializedName("MERCHANT_KEY")
    @Expose
    var MERCHANT_KEY = ""
    @SerializedName("MERCHANT_ID")
    @Expose
    var MERCHANT_ID = ""
    @SerializedName("PRODUCT_INFO")
    @Expose
    var PRODUCT_INFO = ""
    @SerializedName("EMAIL")
    @Expose
    var EMAIL = ""
    @SerializedName("CHECKSUMHASH")
    @Expose
    var CHECKSUMHASH = ""
    @SerializedName("CUST_NAME")
    @Expose
    var CUST_NAME = ""
}