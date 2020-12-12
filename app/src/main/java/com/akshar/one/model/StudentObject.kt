package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class StudentObject : Serializable {

    @SerializedName("isSelected")
    @Expose
    var isSelected = false
    @SerializedName("studentProfileId")
    @Expose
    var studentProfileId = 0
    @SerializedName("classroomId")
    @Expose
    var classroomId = 0
    @SerializedName("rollNbr")
    @Expose
    var rollNbr = ""
    @SerializedName("firstName")
    @Expose
    var firstName = ""
    @SerializedName("lastName")
    @Expose
    var lastName = ""
    @SerializedName("admissionNumber")
    @Expose
    var admissionNumber = ""
    @SerializedName("fullName")
    @Expose
    var fullName = ""
    @SerializedName("gender")
    @Expose
    var gender = ""
    @SerializedName("dateOfBirth")
    @Expose
    var dateOfBirth = ""
    @SerializedName("bloodGroup")
    @Expose
    var bloodGroup = ""
    @SerializedName("imageUrl")
    @Expose
    var imageUrl = ""
    @SerializedName("studentContact")
    @Expose
    var studentContact = StudentContact()
    @SerializedName("classroom")
    @Expose
    var classroom = ClassRoomModel()
}