package com.akshar.one.manager

import com.akshar.one.model.AppList
import com.akshar.one.model.LoginModel

object SessionManager {

    private var loginModel: LoginModel? = null
    private var role: AppList? = null
    private var securityList: ArrayList<String>? = null

    fun setLoginModel(loginModel: LoginModel) {
        this.loginModel = loginModel
        SharedPreferenceFactory.getSharedPreferenceManager()
            .putObject(SharedPreferenceManager.LOGIN_MODEL, loginModel)
    }
    fun setLoginRole(role: AppList) {
        this.role = role
        SharedPreferenceFactory.getSharedPreferenceManager()
            .putObject(SharedPreferenceManager.ROLE, role)
    }
//    fun setSecurityList(securityList: ArrayList<String>) {
//        this.securityList = securityList
//        SharedPreferenceFactory.getSharedPreferenceManager()
//            .putObject(SharedPreferenceManager.securityList, securityList)
//    }

    fun getLoginModel(): LoginModel? {
        if (loginModel == null) {
            loginModel = SharedPreferenceFactory.getSharedPreferenceManager().getObject(
                SharedPreferenceManager.LOGIN_MODEL,
                LoginModel::class.java
            ) as LoginModel?
        }
        return loginModel
    }
//    fun getSecurityList(): ArrayList<String>? {
//        if (securityList == null) {
//            securityList = SharedPreferenceFactory.getSharedPreferenceManager().getObject(
//                SharedPreferenceManager.securityList, String::class.java) as ArrayList<String>?
//        }
//        return securityList
//    }
    fun getLoginRole(): AppList? {
        if (role == null) {
            role = SharedPreferenceFactory.getSharedPreferenceManager().getObject(
                SharedPreferenceManager.ROLE,
                AppList::class.java
            ) as AppList?
        }
        return role
    }

    fun clear(){
        this.loginModel = null
        SharedPreferenceFactory.getSharedPreferenceManager().clear()
    }
}