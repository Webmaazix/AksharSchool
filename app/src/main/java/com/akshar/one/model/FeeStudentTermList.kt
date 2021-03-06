package com.akshar.one.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class FeeStudentTermList : Serializable {

    @SerializedName("studentFeeId")
    @Expose
    var studentFeeId = 0
    @SerializedName("academicYear")
    @Expose
    var academicYear = ""
    @SerializedName("classroomId")
    @Expose
    var classroomId = 0
    @SerializedName("refFeeSetupId")
    @Expose
    var refFeeSetupId = 0
    @SerializedName("feeAmount")
    @Expose
    var feeAmount = 0.0
    @SerializedName("concessionAmount")
    @Expose
    var concessionAmount = 0.0
    @SerializedName("concessionCode")
    @Expose
    var concessionCode = ""
    @SerializedName("finalAmount")
    @Expose
    var finalAmount = 0.0
    @SerializedName("dueAmount")
    @Expose
    var dueAmount = 0.0
    @SerializedName("overdueAmount")
    @Expose
    var overdueAmount = 0.0
    @SerializedName("dueDate")
    @Expose
    var dueDate = ""
    @SerializedName("remarks")
    @Expose
    var remarks = ""
    @SerializedName("feeHead")
    @Expose
    var feeHead = FeeHead()
    @SerializedName("feeTerm")
    @Expose
    var feeTerm = FeeTerm()
}