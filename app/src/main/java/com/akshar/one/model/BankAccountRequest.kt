package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class BankAccountRequest : Serializable {

    @SerializedName("bankAccountId")
    @Expose
    var bankAccountId = 0
}