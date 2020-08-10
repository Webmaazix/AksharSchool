package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class EmployeeList : Serializable {

    @SerializedName("isSelected")
    @Expose
    var isSelected = false
    @SerializedName("employeeProfileId")
    @Expose
    var employeeProfileId = 0
    @SerializedName("employeeId")
    @Expose
    var employeeId = ""
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
    @SerializedName("department")
    @Expose
    var department = ""
    @SerializedName("designation")
    @Expose
    var designation = ""
    @SerializedName("gender")
    @Expose
    var gender = ""
    @SerializedName("phonePrimary")
    @Expose
    var phonePrimary = ""
    @SerializedName("email")
    @Expose
    var email = ""
    @SerializedName("imageUrl")
    @Expose
    var imageUrl = ""
    @SerializedName("fullName")
    @Expose
    var fullName = ""
    @SerializedName("emailList")
    @Expose
    var emailList = ArrayList<String>()
    @SerializedName("primaryPhoneList")
    @Expose
    var primaryPhoneList = ArrayList<String>()
}