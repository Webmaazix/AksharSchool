package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MarksGraphModel : Serializable {

    @SerializedName("examId")
    @Expose
    var examId = 0
    @SerializedName("examName")
    @Expose
    var examName = ""

    @SerializedName("overAllPercentage")
    @Expose
    var overAllPercentage = 0

    @SerializedName("displayOrder")
    @Expose
    var displayOrder = 0

    @SerializedName("subjectMarksGraphDTO")
    @Expose
    var subjectMarksGraphDTO = ArrayList<SubjectMarksGraphDTO>()

}