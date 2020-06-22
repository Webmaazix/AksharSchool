package com.akshar.one.manager

import com.akshar.one.model.LoginModel

object SessionManager {

    private var loginModel: LoginModel? = null

    fun setLoginModel(loginModel: LoginModel) {
        this.loginModel = loginModel
        SharedPreferenceFactory.getSharedPreferenceManager()
            .putObject(SharedPreferenceManager.LOGIN_MODEL, loginModel)
    }

    fun getLoginModel(): LoginModel? {
        if (loginModel == null) {
            loginModel = SharedPreferenceFactory.getSharedPreferenceManager().getObject(
                SharedPreferenceManager.LOGIN_MODEL,
                LoginModel::class.java
            ) as LoginModel?
        }
        return loginModel
    }

    fun clear(){
        this.loginModel = null
        SharedPreferenceFactory.getSharedPreferenceManager().clear()
    }
}