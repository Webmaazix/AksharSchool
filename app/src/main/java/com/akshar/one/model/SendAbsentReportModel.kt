package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SendAbsentReportModel : Serializable {


    @SerializedName("id")
    @Expose
    var id = 0

    @SerializedName("startDate")
    @Expose
    var startDate = ""

    @SerializedName("endDate")
    @Expose
    var endDate = ""

    @SerializedName("title")
    @Expose
    var title = ""

    @SerializedName("description")
    @Expose
    var description = ""

    @SerializedName("visibility")
    @Expose
    var visibility = ""

}