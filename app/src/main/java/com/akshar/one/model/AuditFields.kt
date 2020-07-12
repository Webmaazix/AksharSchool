package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class AuditFields : Serializable {

    @SerializedName("createdBy")
    @Expose
    var createdBy = ""

    @SerializedName("updatedBy")
    @Expose
    var updatedBy = ""

    @SerializedName("createdTs")
    @Expose
    var createdTs = ""

    @SerializedName("updatedTs")
    @Expose
    var updatedTs = ""

}