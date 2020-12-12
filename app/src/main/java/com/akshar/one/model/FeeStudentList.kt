package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class FeeStudentList : Serializable {

    @SerializedName("student")
    @Expose
    var student = StudentObject()
    @SerializedName("feeItemList")
    @Expose
    var feeItemList = ArrayList<FeeStudentTermList>()
}