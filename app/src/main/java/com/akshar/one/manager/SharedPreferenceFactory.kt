package com.akshar.one.manager

object SharedPreferenceFactory {
    private lateinit var sharedPreferenceManager: SharedPreferenceManager

    fun setSharedPreferenceManager(sharedPreferenceManager: SharedPreferenceManager) {
        this.sharedPreferenceManager = sharedPreferenceManager
    }

    fun getSharedPreferenceManager() = sharedPreferenceManager
}