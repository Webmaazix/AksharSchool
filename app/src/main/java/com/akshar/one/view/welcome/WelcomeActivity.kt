package com.akshar.one.view.welcome

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.akshar.one.R
import com.akshar.one.adapter.SliderAdapter
import com.akshar.one.util.AndroidUtil
import com.akshar.one.util.SliderTimer
import com.akshar.one.view.activity.BaseActivity
import com.akshar.one.view.activity.MainActivity
import com.akshar.one.view.login.LoginActivity
import kotlinx.android.synthetic.main.activity_welcome.*
import java.util.*

class WelcomeActivity : BaseActivity() {
    internal lateinit var color:List<Int>
    internal lateinit var colorName:List<String>
    private var currActivity : Activity = this

    companion object{
        fun open(currActivity : Activity){
            val intent = Intent(currActivity, WelcomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            currActivity.startActivity(intent)
            currActivity.overridePendingTransition(R.anim.slide_in,R.anim.slide_out)
            currActivity.finish()

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        color = ArrayList()
        (color as ArrayList<Int>).add(Color.RED)
        (color as ArrayList<Int>).add(Color.GREEN)
        (color as ArrayList<Int>).add(Color.BLUE)
        colorName = ArrayList()
        (colorName as ArrayList<String>).add("RED")
        (colorName as ArrayList<String>).add("GREEN")
        (colorName as ArrayList<String>).add("BLUE")
        viewPagerj.adapter = SliderAdapter(this, color, colorName)
        indicator!!.setupWithViewPager(viewPagerj, true)
        val timer = Timer()
        timer.scheduleAtFixedRate(SliderTimer(currActivity,viewPagerj,color), 4000, 6000)

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