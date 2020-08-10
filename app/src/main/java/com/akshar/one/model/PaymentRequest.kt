package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PaymentRequest : Serializable {

    @SerializedName("invoiceNumber")
    @Expose
    var invoiceNumber : String? = null
    @SerializedName("controlNumber")
    @Expose
    var controlNumber : String? = null
    @SerializedName("paymentDate")
    @Expose
    var paymentDate : String = ""
    @SerializedName("paymentMethod")
    @Expose
    var paymentMethod : String = ""
    @SerializedName("paymentDescription")
    @Expose
    var paymentDescription : String = ""
    @SerializedName("checkNbr")
    @Expose
    var checkNbr : String? = null
    @SerializedName("paymentReferenceNbr")
    @Expose
    var paymentReferenceNbr : String? = null
    @SerializedName("transactionStatus")
    @Expose
    var transactionStatus : String? = null
    @SerializedName("clearanceStatus")
    @Expose
    var clearanceStatus : String? = null
    @SerializedName("paymentClearedDate")
    @Expose
    var paymentClearedDate : String? = null
    @SerializedName("paymentSource")
    @Expose
    var paymentSource : String? = null
    @SerializedName("bankAccount")
    @Expose
    var bankAccount = BankAccountRequest()
    @SerializedName("student")
    @Expose
    var student = StudentRequest()
    @SerializedName("paymentsList")
    @Expose
    var paymentsList = ArrayList<PaymentListRequest>()
}