package com.akshar.one.viewmodels.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.akshar.one.manager.SessionManager
import com.akshar.one.model.LoginModel
import com.akshar.one.model.OTPModel
import com.akshar.one.repository.login.LoginRepository
import com.akshar.one.util.AppConstant
import com.akshar.one.util.AppUtil
import com.akshar.one.viewmodels.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class LoginViewModel(application: Application) : BaseViewModel(application) {

    private var loginRepository: LoginRepository? = null
    private var mutableLiveDataLoginModel: MutableLiveData<LoginModel> = MutableLiveData()
    private val isLoading = MutableLiveData<Boolean>()
    private var mutableLiveDataOTPModel: MutableLiveData<OTPModel> = MutableLiveData()
    private var mutableLiveDataChangePasswordResponse: MutableLiveData<Boolean>? = MutableLiveData()

    init {
        loginRepository = LoginRepository()
    }

    fun getIsLoading(): MutableLiveData<Boolean> = isLoading

    fun getMutableLiveDataLoginModel(): MutableLiveData<LoginModel> =
        mutableLiveDataLoginModel

    fun getMutableLiveDataOTPResponse(): MutableLiveData<OTPModel>? = mutableLiveDataOTPModel

    fun getMutableLiveDataChangePassword(): MutableLiveData<Boolean>? =
        mutableLiveDataChangePasswordResponse

    fun loginServiceWithUsername(username: String, password: String) {
        isLoading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val loginModel = loginRepository?.loginWithUsername(username, password)
                    isLoading.postValue(false)
                    loginModel?.let {
                        mutableLiveDataLoginModel.postValue(it)
                        SessionManager.setLoginModel(it)
                    }
                } catch (httpException: HttpException) {
                    isLoading.postValue(false)
                    val errorResponse =
                        AppUtil.getErrorResponse(httpException.response()?.errorBody()?.string())
                    errorResponse?.let { getErrorMutableLiveData().postValue(it) }
                } catch (e: Exception) {
                    isLoading.postValue(false)
                    Log.d(AppConstant.TAG, "Login Exception : $e")
                }
            }
        }
    }

    fun loginServiceGetOTP(mobileNumber: String) {
        isLoading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val otpResponse = loginRepository?.getOTP(mobileNumber)
                    isLoading.postValue(false)
                    mutableLiveDataOTPModel.postValue(otpResponse)
                } catch (httpException: HttpException) {
                    val errorResponse =
                        AppUtil.getErrorResponse(httpException.response()?.errorBody()?.string())
                    isLoading.postValue(false)
                    errorResponse?.let { getErrorMutableLiveData().postValue(it) }
                } catch (e: Exception) {
                    isLoading.postValue(false)
                    Log.d(AppConstant.TAG, "Get OTP Exception : $e")
                }
            }
        }
    }

    fun changePasswordService(username: String, password: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    isLoading.postValue(true)
                    loginRepository?.changePassword(username, password)
                    isLoading.postValue(false)
                    mutableLiveDataChangePasswordResponse?.postValue(true)
                } catch (httpException: HttpException) {
                    isLoading.postValue(false)
                    val errorResponse =
                        AppUtil.getErrorResponse(httpException.response()?.errorBody()?.string())
                    Log.d(
                        AppConstant.TAG,
                        "Change Password : Status  ${errorResponse?.status}, Message ${errorResponse?.message}"
                    )
                } catch (e: Exception) {
                    isLoading.postValue(false)
                    Log.d(AppConstant.TAG, "Change Password : $e")
                }
            }
        }
    }
}