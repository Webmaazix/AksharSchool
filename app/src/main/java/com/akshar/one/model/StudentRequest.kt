package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class StudentRequest : Serializable {
    @SerializedName("studentProfileId")
    @Expose
    var studentProfileId = 0
}