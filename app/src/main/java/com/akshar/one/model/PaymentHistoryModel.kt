package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PaymentHistoryModel  : Serializable{

    @SerializedName("invoiceNumber")
    @Expose
    var invoiceNumber = 0
    @SerializedName("controlNumber")
    @Expose
    var controlNumber = 0
    @SerializedName("paymentDate")
    @Expose
    var paymentDate = ""
    @SerializedName("paymentMethod")
    @Expose
    var paymentMethod = ""
    @SerializedName("paymentDescription")
    @Expose
    var paymentDescription = ""
    @SerializedName("checkNbr")
    @Expose
    var checkNbr = ""
    @SerializedName("paymentReferenceNbr")
    @Expose
    var paymentReferenceNbr = ""
    @SerializedName("transactionStatus")
    @Expose
    var transactionStatus = ""
    @SerializedName("clearanceStatus")
    @Expose
    var clearanceStatus = ""
    @SerializedName("paymentClearedDate")
    @Expose
    var paymentClearedDate = ""
    @SerializedName("paymentSource")
    @Expose
    var paymentSource = ""
    @SerializedName("bankAccount")
    @Expose
    var bankAccount = BankAccount()
    @SerializedName("student")
    @Expose
    var student = StudentListModel()
    @SerializedName("paymentsList")
    @Expose
    var paymentsList = ArrayList<PaymentList>()
}