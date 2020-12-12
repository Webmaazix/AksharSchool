package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class TeacherModel : Serializable {

    @SerializedName("employeeProfileId")
    @Expose
    var employeeProfileId = 0

    @SerializedName("employeeId")
    @Expose
    var employeeId = ""

    @SerializedName("fullName")
    @Expose
    var fullName = ""

    @SerializedName("department")
    @Expose
    var department = ""

    @SerializedName("designation")
    @Expose
    var designation = ""
    @SerializedName("namePrefix")
    @Expose
    var namePrefix = ""
    @SerializedName("firstName")
    @Expose
    var firstName = ""
    @SerializedName("middleName")
    @Expose
    var middleName = ""
    @SerializedName("lastName")
    @Expose
    var lastName = ""
    @SerializedName("phonePrimary")
    @Expose
    var phonePrimary = ""
    @SerializedName("gender")
    @Expose
    var gender = ""
    @SerializedName("email")
    @Expose
    var email = ""
    @SerializedName("imageUrl")
    @Expose
    var imageUrl = ""

}
