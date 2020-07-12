package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

 class AddressModel : Serializable{

    @SerializedName("addressLine1")
    @Expose
    var addressLine1 : String? = null

    @SerializedName("addressLine2")
    @Expose
    var addressLine2 : String? = null

    @SerializedName("city")
    @Expose
    var city : String? = null

    @SerializedName("district")
    @Expose
    var district : String? = null

    @SerializedName("state")
    @Expose
    var state : String? = null

    @SerializedName("postalcode")
    @Expose
    var postalcode : String? = null

    @SerializedName("country")
    @Expose
    var country : String? = null
}