package com.akshar.one.model

data class FeeSummeryModel(val feehead : String?,
                           val term : String?,
                           val feeAmount : Int,
                           val concession : Int?,
                           val amountAfterConcession : Int?,
                           val amountPaid : Int?,
                           val dueAmount : Int?,
                           val overdueAmount : Int?)