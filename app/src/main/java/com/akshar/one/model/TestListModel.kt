package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class TestListModel : Serializable {

    @SerializedName("testId")
    @Expose
    var testId = 0

    @SerializedName("examId")
    @Expose
    var examId = 0

    @SerializedName("displayOrder")
    @Expose
    var displayOrder = 0

    @SerializedName("testName")
    @Expose
    var testName = ""
}