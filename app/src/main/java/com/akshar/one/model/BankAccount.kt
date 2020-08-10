package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class BankAccount : Serializable {

    @SerializedName("bankAccountId")
    @Expose
    var bankAccountId = 0
    @SerializedName("accountNickname")
    @Expose
    var accountNickname = ""
    @SerializedName("bankName")
    @Expose
    var bankName = ""
    @SerializedName("accountNumber")
    @Expose
    var accountNumber = ""
    @SerializedName("accountStartDate")
    @Expose
    var accountStartDate = ""
    @SerializedName("ifscCode")
    @Expose
    var ifscCode = ""
}