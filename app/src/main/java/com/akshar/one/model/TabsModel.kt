package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class TabsModel : Serializable{

    @SerializedName("name")
    @Expose
    var name: String = ""
}