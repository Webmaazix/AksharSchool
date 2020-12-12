package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class FeeHeadList : Serializable{

    @SerializedName("feeHead")
    @Expose
    var feeHead = ""
    @SerializedName("feeItemList")
    @Expose
    var feeTermList = ArrayList<FeeTermList>()


}