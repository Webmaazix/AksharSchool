package com.akshar.one.view.welcome

import android.os.Bundle
import com.akshar.one.R
import com.akshar.one.util.AndroidUtil
import com.akshar.one.view.activity.BaseActivity
import com.akshar.one.view.login.LoginActivity
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        btnLoginWithEmail.setOnClickListener {
            goToLoginScreen(true)
        }

        btnLoginWithPhone.setOnClickListener {
            goToLoginScreen(false)
        }
    }

    private fun goToLoginScreen(showUsernameScreen: Boolean) {
        val bundle = Bundle()
        bundle.putBoolean(LoginActivity.SHOW_LOGIN_WITH_USERNAME_SCREEN, showUsernameScreen)
        AndroidUtil.startActivity(this, LoginActivity::class.java, bundle)
        finish()
    }
}