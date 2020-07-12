package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class StudentList : Serializable {

    @SerializedName("content")
    @Expose
    var list : List<StudentListModel>? = null
}