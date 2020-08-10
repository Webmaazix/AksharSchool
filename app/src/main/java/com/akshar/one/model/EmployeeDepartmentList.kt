package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class EmployeeDepartmentList : Serializable {

    @SerializedName("lookupSetupId")
    @Expose
    var lookupSetupId = 0
    @SerializedName("fieldName")
    @Expose
    var fieldName = ""
    @SerializedName("value")
    @Expose
    var value = ""
    @SerializedName("isActive")
    @Expose
    var isActive = false
    @SerializedName("isSelected")
    @Expose
    var isSelected = false


}