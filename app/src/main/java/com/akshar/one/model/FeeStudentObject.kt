package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class FeeStudentObject : Serializable {

    @SerializedName("content")
    @Expose
    var content = ArrayList<FeeStudentList>()
}