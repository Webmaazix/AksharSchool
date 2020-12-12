package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class TermList : Serializable {

    @SerializedName("isSelected")
    @Expose
    var isSelected = false
    @SerializedName("termName")
    @Expose
    var termName = ""

    @SerializedName("feeTermSetupId")
    @Expose
    var feeTermSetupId = 0

    @SerializedName("dueDate")
    @Expose
    var dueDate = ""
}