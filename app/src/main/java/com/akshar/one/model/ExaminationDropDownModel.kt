package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ExaminationDropDownModel : Serializable {

    @SerializedName("examId")
    @Expose
    var examId = 0

    @SerializedName("examName")
    @Expose
    var examName = ""

    @SerializedName("examType")
    @Expose
    var examType = ""

    @SerializedName("scheduleStatus")
    @Expose
    var scheduleStatus = ""

    @SerializedName("marksStatus")
    @Expose
    var marksStatus = ""

    @SerializedName("tests")
    @Expose
    var testsList = ArrayList<TestListModel>()
}