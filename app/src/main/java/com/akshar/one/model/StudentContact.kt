package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class StudentContact : Serializable {

    @SerializedName("address")
    @Expose
    var address : AddressModel = AddressModel()


    @SerializedName("primaryParentName")
    @Expose
    var parentName : String? = null

    @SerializedName("primaryParentMobile")
    @Expose
    var mobile : String? = null

    @SerializedName("primaryParentEmail")
    @Expose
    var email : String? = null

    @SerializedName("studentProfileId")
    @Expose
    var studentProfileId : Int? = null

    @SerializedName("primaryParentRelationship")
    @Expose
    var relationship : String?  = null
}