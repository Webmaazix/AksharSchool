package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MarksTestListParent : Serializable {

    @SerializedName("testName")
    @Expose
    var testName = ""
    @SerializedName("subjectMarksList")
    @Expose
    var subjectMarksList = ArrayList<SubjectListParent>()
}