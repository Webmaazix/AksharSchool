package com.akshar.one.util

import android.app.Activity
import androidx.viewpager.widget.ViewPager
import java.util.*

class SliderTimer(var currContext : Activity, val view_pager : ViewPager, var list : List<Int>) : TimerTask(){
    override fun run() {
       currContext.runOnUiThread(object:Runnable {
             override fun run() {
                if (view_pager.currentItem < list.size - 1)
                {
                    view_pager.currentItem = view_pager.currentItem + 1
                }
                else
                {
                    view_pager.currentItem = 0
                }
            }
        })
    }
}