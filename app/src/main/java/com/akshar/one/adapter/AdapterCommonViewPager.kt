package com.akshar.one.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class AdapterCommonViewPager(var currContext : Context, var fm : FragmentManager): FragmentStatePagerAdapter(fm){
   val fragmentList = ArrayList<Fragment>()

    override fun getItem(p0: Int): Fragment {
        return  fragmentList[p0]
    }

    override fun getCount(): Int {
       return fragmentList.size
    }

    fun addFragment(fragment : Fragment,title : String){
        fragmentList.add(fragment)
    }


}