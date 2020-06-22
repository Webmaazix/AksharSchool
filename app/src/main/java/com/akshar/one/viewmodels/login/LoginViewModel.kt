package com.akshar.one.viewmodels.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.akshar.one.manager.SessionManager
import com.akshar.one.model.LoginModel
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
    private var mutableLiveDataLoginModel: MutableLiveData<LoginModel> = MutableLiveData<LoginModel>()
    private val isLoading = MutableLiveData<Boolean>()

    init {
        loginRepository = LoginRepository()
    }

    fun getIsLoading(): MutableLiveData<Boolean> = isLoading

    fun getMutableLiveDataLoginModel(): MutableLiveData<LoginModel> =
        mutableLiveDataLoginModel

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
}