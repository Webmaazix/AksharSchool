package com.akshar.one.view.assignhomework

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.akshar.one.R

class CreateHomeworkActivity : AppCompatActivity() {

    companion object{
        fun open(currActivity : Activity){
            val intent = Intent(currActivity, CreateHomeworkActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            currActivity.startActivity(intent)
            currActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_homework)
    }
}
