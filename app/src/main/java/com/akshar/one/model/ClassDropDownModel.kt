package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

 class ClassDropDownModel : Serializable{

    @SerializedName("courseId")
    @Expose
    var courseId = 0

    @SerializedName("courseName")
    @Expose
    var courseName = ""

    @SerializedName("displayOrder")
    @Expose
    var displayOrder = ""

    @SerializedName("classroomsList")
    @Expose
    var classroomsList = ArrayList<SectionList>()
}