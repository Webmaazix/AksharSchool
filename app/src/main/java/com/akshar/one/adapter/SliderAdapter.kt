package com.akshar.one.adapter

import android.content.Context
import android.view.ViewGroup
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.akshar.one.R


class SliderAdapter(private val context: Context, private val color: List<Int>, private val colorName: List<String>) : PagerAdapter() {

    override fun getCount(): Int {
        return color.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.custom_layout, null)
        val viewPager = container as ViewPager
        viewPager.addView(view, 0)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val viewPager = container as ViewPager
        val view = `object` as View
        viewPager.removeView(view)
    }
}