package com.akshar.one.viewmodels.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.akshar.one.manager.SharedPreferenceFactory
import com.akshar.one.manager.SharedPreferenceManager
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

class LoginWithPhoneViewModel(application: Application) : BaseViewModel(application) {

    private var loginRepository: LoginRepository? = null
    private var mutableLiveDataLoginModel: MutableLiveData<LoginModel>? = null
    private var mutableLiveDataOTPModel: MutableLiveData<OTPModel> = MutableLiveData()
    private var mutableLiveDataChangePasswordResponse: MutableLiveData<Boolean>? = null
    private val isLoading = MutableLiveData<Boolean>()

    init {
        loginRepository = LoginRepository()
    }

    fun getIsLoading(): MutableLiveData<Boolean> = isLoading

    fun getMutableLiveDataLoginResponse(): MutableLiveData<LoginModel>? =
        mutableLiveDataLoginModel

    fun getMutableLiveDataOTPResponse(): MutableLiveData<OTPModel>? = mutableLiveDataOTPModel

    fun getMutableLiveDataChangePassword(): MutableLiveData<Boolean>? =
        mutableLiveDataChangePasswordResponse

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

    fun loginServiceWithOTP(mobileNumber: String, otp: String) {
        mutableLiveDataLoginModel = MutableLiveData()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val loginModel = loginRepository?.loginWithOTP(mobileNumber, otp)
                    loginModel?.let {
                        mutableLiveDataLoginModel?.postValue(it)
                        SharedPreferenceFactory.getSharedPreferenceManager()
                            .putObject(SharedPreferenceManager.LOGIN_MODEL, it)
                    }
                } catch (httpException: HttpException) {
                    val errorResponse =
                        AppUtil.getErrorResponse(httpException.response()?.errorBody()?.string())
                    Log.d(
                        AppConstant.TAG,
                        "Login Error : Status  ${errorResponse?.status}, Message ${errorResponse?.message}"
                    )
                } catch (e: Exception) {
                    Log.d(AppConstant.TAG, "Login Exception : $e")
                }
            }
        }
    }

    fun changePasswordService(username: String, password: String) {
        mutableLiveDataChangePasswordResponse = MutableLiveData()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    loginRepository?.changePassword(username, password)
                    mutableLiveDataChangePasswordResponse?.postValue(true)
                } catch (httpException: HttpException) {
                    val errorResponse =
                        AppUtil.getErrorResponse(httpException.response()?.errorBody()?.string())
                    Log.d(
                        AppConstant.TAG,
                        "Change Password : Status  ${errorResponse?.status}, Message ${errorResponse?.message}"
                    )
                } catch (e: Exception) {
                    Log.d(AppConstant.TAG, "Change Password : $e")
                }
            }
        }
    }

}