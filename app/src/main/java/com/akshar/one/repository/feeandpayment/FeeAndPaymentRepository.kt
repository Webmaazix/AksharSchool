package com.akshar.one.repository.feeandpayment

import com.akshar.one.api.service.AksharSchoolService
import com.akshar.one.api.service.ApiInterface
import com.akshar.one.model.NoticeBoardModel
import com.akshar.one.model.PaymentRequest
import com.akshar.one.model.StudentListModel
import com.akshar.one.repository.base.BaseRepository
import org.json.JSONObject

class FeeAndPaymentRepository : BaseRepository() {

    private var apiInterface : ApiInterface? = null
    private var service : AksharSchoolService = AksharSchoolService()

    init {
        apiInterface = service.createService(ApiInterface::class.java)
    }

    suspend fun getFeeDetail(studentId : Int) = apiInterface?.getFeeDetail(service.headers(),studentId)
    suspend fun getPaymentHistory(studentId : Int) = apiInterface?.getPaymentHistory(service.headers(),studentId)
    suspend fun getPaymentMethod(fieldName : String) = apiInterface?.getPaymentMethod(service.headers(),fieldName)
    suspend fun getBankAccountList() = apiInterface?.getBankAccountList(service.headers())
    suspend fun addFeePayment(paymentRequest : PaymentRequest) = apiInterface?.addFeePayment(service.headers(),paymentRequest)


}