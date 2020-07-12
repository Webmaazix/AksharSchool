package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class NoticeBoardModel : Serializable {

    @SerializedName("id")
    @Expose
    var id = 0
    @SerializedName("schoolCd")
    @Expose
    var schoolCd = ""
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
    @SerializedName("recordStatus")
    @Expose
    var recordStatus = ""

    @SerializedName("auditFields")
    @Expose
    var auditFields = AuditFields()


}