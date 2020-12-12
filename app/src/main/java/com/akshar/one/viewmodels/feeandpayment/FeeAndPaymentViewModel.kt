package com.akshar.one.viewmodels.feeandpayment

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.akshar.one.model.*
import com.akshar.one.repository.feeandpayment.FeeAndPaymentRepository
import com.akshar.one.repository.noticeboard.NoticeBoardRepository
import com.akshar.one.repository.student.StudentRepository
import com.akshar.one.util.AppUtil
import com.akshar.one.viewmodels.base.BaseViewModel
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException
import java.lang.Exception

class FeeAndPaymentViewModel(application: Application) : BaseViewModel(application){
    private var feeRepository : FeeAndPaymentRepository? = null
    private val isLoading = MutableLiveData<Boolean>()
    private val isSuccess = MutableLiveData<Boolean>()
    private var mutuableLiveDataFeeDetail = MutableLiveData<List<FeesDetailModel>>()
    private var mutuableLiveDataPaymentGatewayResponse= MutableLiveData<PaymentGatewayResponse>()
    private var mutuableLiveDataPaymentHistory = MutableLiveData<List<PaymentHistoryModel>>()
    private var mutuableLiveDataPaymentMethod = MutableLiveData<List<String>>()
    private var mutuableLiveDataBankAccount = MutableLiveData<List<BankAccount>>()
    private var mutableLiveDataImage = MutableLiveData<ImageModel>()
    private var mutableLiveDataForInvoice = MutableLiveData<InvoiceModel>()

    init {
        feeRepository = FeeAndPaymentRepository()
    }

    fun getFeeDetailLiveData() : MutableLiveData<List<FeesDetailModel>> = mutuableLiveDataFeeDetail
    fun getInvoiceLiveData() : MutableLiveData<InvoiceModel> = mutableLiveDataForInvoice
    fun getPaymentGatewayResponseLiveData() : MutableLiveData<PaymentGatewayResponse> = mutuableLiveDataPaymentGatewayResponse
    fun getPaymentHistoryLiveData() : MutableLiveData<List<PaymentHistoryModel>> = mutuableLiveDataPaymentHistory
    fun getPaymentMethodLiveData() : MutableLiveData<List<String>> = mutuableLiveDataPaymentMethod
    fun getBankAccountLiveData() : MutableLiveData<List<BankAccount>> = mutuableLiveDataBankAccount
    fun getSuccessLiveData() : MutableLiveData<Boolean> = isSuccess

    fun getImageLiveData() : MutableLiveData<ImageModel> = mutableLiveDataImage

    fun getFeeDetail(studentId : Int){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    isLoading.postValue(false)
                    val noticeList = feeRepository?.getFeeDetail(studentId)
                    noticeList.let {
                        mutuableLiveDataFeeDetail.postValue(it)
                    }

                }catch (httpException : HttpException){
                    isLoading.postValue(false)
                    val errorResponse  = AppUtil.getErrorResponse(httpException.response()?.errorBody()?.string())
                    errorResponse.let { getErrorMutableLiveData().postValue(it) }

                }catch (e : Exception){
                    isLoading.postValue(false)
                    e.printStackTrace()
                }
            }
        }
    }
    fun getInVoice(invoiceNumber : Int){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    isLoading.postValue(false)
                    val data = feeRepository?.getInVoice(invoiceNumber)
                    mutableLiveDataForInvoice.postValue(data)
                }catch (httpException : HttpException){
                    isLoading.postValue(false)
                    val errorResponse  = AppUtil.getErrorResponse(httpException.response()?.errorBody()?.string())
                    errorResponse.let { getErrorMutableLiveData().postValue(it) }

                }catch (e : Exception){
                    isLoading.postValue(false)
                    e.printStackTrace()
                }
            }
        }
    }
    fun sendPayUResponseToServer(model : JsonObject){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    isLoading.postValue(false)
                    val noticeList = feeRepository?.sendPayUResponseToServer(model)

                }catch (httpException : HttpException){
                    isLoading.postValue(false)
                    val errorResponse  = AppUtil.getErrorResponse(httpException.response()?.errorBody()?.string())
                    errorResponse.let { getErrorMutableLiveData().postValue(it) }

                }catch (e : Exception){
                    isLoading.postValue(false)
                    e.printStackTrace()
                }
            }
        }
    }
    fun paymentGatewayRedirect(model : PaymentGatewayRequest){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    isLoading.postValue(false)
                    val list = feeRepository?.paymentGatewayRedirect(model)
                    list.let {
                        mutuableLiveDataPaymentGatewayResponse.postValue(it)
                    }

                }catch (httpException : HttpException){
                    isLoading.postValue(false)
                    val errorResponse  = AppUtil.getErrorResponse(httpException.response()?.errorBody()?.string())
                    errorResponse.let { getErrorMutableLiveData().postValue(it) }

                }catch (e : Exception){
                    isLoading.postValue(false)
                    e.printStackTrace()
                }
            }
        }
    }

    fun getPaymentHistory(studentId : Int){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    isLoading.postValue(false)
                    val noticeList = feeRepository?.getPaymentHistory(studentId)
                    noticeList.let {
                        mutuableLiveDataPaymentHistory.postValue(it)
                    }

                }catch (httpException : HttpException){
                    isLoading.postValue(false)
                    val errorResponse  = AppUtil.getErrorResponse(httpException.response()?.errorBody()?.string())
                    errorResponse.let { getErrorMutableLiveData().postValue(it) }

                }catch (e : Exception){
                    isLoading.postValue(false)
                    e.printStackTrace()
                }
            }
        }
    }

    fun getPaymentMethod(fieldName : String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    isLoading.postValue(false)
                    val noticeList = feeRepository?.getPaymentMethod(fieldName)
                    noticeList.let {
                        mutuableLiveDataPaymentMethod.postValue(it)
                    }

                }catch (httpException : HttpException){
                    isLoading.postValue(false)
                    val errorResponse  = AppUtil.getErrorResponse(httpException.response()?.errorBody()?.string())
                    errorResponse.let { getErrorMutableLiveData().postValue(it) }

                }catch (e : Exception){
                    isLoading.postValue(false)
                    e.printStackTrace()
                }
            }
        }
    }

    fun getBankAccountList(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    isLoading.postValue(false)
                    val noticeList = feeRepository?.getBankAccountList()
                    noticeList.let {
                        mutuableLiveDataBankAccount.postValue(it)
                    }

                }catch (httpException : HttpException){
                    isLoading.postValue(false)
                    val errorResponse  = AppUtil.getErrorResponse(httpException.response()?.errorBody()?.string())
                    errorResponse.let { getErrorMutableLiveData().postValue(it) }

                }catch (e : Exception){
                    isLoading.postValue(false)
                    e.printStackTrace()
                }
            }
        }
    }

    fun addFeePayment(paymentRequest : PaymentRequest){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    isLoading.postValue(false)
                    val noticeList = feeRepository?.addFeePayment(paymentRequest)
                    isSuccess.postValue(true)

                }catch (httpException : HttpException){
                    isLoading.postValue(false)
                    val errorResponse  = AppUtil.getErrorResponse(httpException.response()?.errorBody()?.string())
                    errorResponse.let { getErrorMutableLiveData().postValue(it) }

                }catch (e : Exception){
                    isLoading.postValue(false)
                    e.printStackTrace()
                }
            }
        }
    }
}