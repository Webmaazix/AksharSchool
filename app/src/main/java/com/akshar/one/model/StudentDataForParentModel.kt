package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class StudentDataForParentModel  : Serializable{

    @SerializedName("isSelected")
    @Expose
    var isSelected = false


    @SerializedName("studentProfileId")
    @Expose
    var studentProfileId : Int? = null

    @SerializedName("classroom")
    @Expose
    var classroom = ClassRoomModel()

    @SerializedName("firstName")
    @Expose
    var firstName : String? = null

    @SerializedName("courseName")
    @Expose
    var courseName : String? = null

    @SerializedName("classroomName")
    @Expose
    var classroomName : String? = null

    @SerializedName("lastName")
    @Expose
    var lastName : String? = null

    @SerializedName("gender")
    @Expose
    var gender : String? = null

    @SerializedName("admissionNumber")
    @Expose
    var admissionNumber : String? = null

    @SerializedName("dateOfBirth")
    @Expose
    var dateOfBirth : String? = null

    @SerializedName("bloodGroup")
    @Expose
    var bloodGroup : String? = null

    @SerializedName("fullName")
    @Expose
    var fullName : String = ""

    @SerializedName("imageUrl")
    @Expose
    var imageUrl : String?  = null

    @SerializedName("studentContact")
    @Expose
    var studentContact : StudentContact = StudentContact()
}