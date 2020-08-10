package com.akshar.one.model

data class FinanceModel(val feeSummary : FeeSummeryModel?,
                        val feePayment : ArrayList<FeePayment>?,
                        val expenseSummary : ArrayList<FeePayment>?)
