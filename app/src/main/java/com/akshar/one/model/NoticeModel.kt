package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class NoticeModel : Serializable {

    @SerializedName("content")
    @Expose
    var list : List<NoticeBoardModel>? = null
}