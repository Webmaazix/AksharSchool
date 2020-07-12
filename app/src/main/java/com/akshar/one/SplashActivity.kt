package com.akshar.one

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import com.akshar.one.manager.SessionManager
import com.akshar.one.view.activity.MainActivity
import com.akshar.one.view.welcome.WelcomeActivity
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*

class SplashActivity : AppCompatActivity() {
    private var currActivity : Activity = this


    companion object {
        private var splash_time_out : Long = 3000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

//        val animation_memorize = AnimationUtils.loadAnimation(currActivity,R.anim.slide_in_bottom)
//        imgLogo.startAnimation(animation_memorize)

       // val splash_animation = AnimationUtils.loadAnimation(currActivity,R.anim.scale_up)
       // img_splash.startAnimation(splash_animation)
        Handler().postDelayed({
            if(SessionManager.getLoginModel()== null){
                WelcomeActivity.open(currActivity)
            }else{
                MainActivity.open(currActivity)
            }
        }, splash_time_out)

    }
}
