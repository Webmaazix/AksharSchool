package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GetAbsentRequest : Serializable {

    @SerializedName("classroomIdList")
    @Expose
    var classroomIdList : ArrayList<Int>? = null

    @SerializedName("shiftIdList")
    @Expose
    var shiftIdList = ArrayList<Int>()

    @SerializedName("date")
    @Expose
    var date = ""
    @SerializedName("notificationType")
    @Expose
    var notificationType = ""
    @SerializedName("category")
    @Expose
    var category = ""
}