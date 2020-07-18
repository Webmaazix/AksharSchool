package com.akshar.one.view.timetable

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import com.akshar.one.DummyContent
import com.akshar.one.R
import com.akshar.one.adapter.AdapterCommonViewPager
import com.akshar.one.calender.data.Day
import com.akshar.one.calender.widget.CollapsibleCalendar
import com.akshar.one.databinding.ActivityTimeTableBinding
import com.akshar.one.model.TabsModel
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.main_toolbar.view.*
import kotlinx.android.synthetic.main.tablayout.view.*

class TimeTableActivity : AppCompatActivity(),View.OnClickListener{


    private var currActivity : Activity = this
    private var binding : ActivityTimeTableBinding? = null
    var customTabs = ArrayList<TabsModel>()
    lateinit var myTimeTableFragment: MyTimeTableFragment
    lateinit var classTimeTableFragment: ClassTimeTableFragment

    companion object{
        fun open(currActivity : Activity){
            val intent = Intent(currActivity, TimeTableActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            currActivity.startActivity(intent)
            currActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        }
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(currActivity,R.layout.activity_time_table)
        customTabs = DummyContent.timeTable()
        setUpViewPager()
        initViews()
        binding!!.collapsibleCalendarView.setCalendarListener(object : CollapsibleCalendar.CalendarListener {
            override fun onDayChanged() {

            }

            override fun onClickListener() {
                if(binding!!.collapsibleCalendarView.expanded){
                    binding!!.imgExpand.setImageResource(R.drawable.arrow_up)
                    binding!!.collapsibleCalendarView.collapse(400)
                } else{
                    binding!!.imgExpand.setImageResource(R.drawable.down_arrow_icon)
                    binding!!.collapsibleCalendarView.expand(400)
                }
            }

            override fun onDaySelect() {
                val data = binding!!.collapsibleCalendarView.selectedDay
                sendDateToRefreshData(data!!)
            }

            override fun onItemClick(v: View) {

            }

            override fun onDataUpdate() {

            }

            override fun onMonthChange() {

            }

            override fun onWeekChange(position: Int) {

            }

        })
    }

    private fun initViews(){
        binding!!.toolbar.imgMenu.visibility = View.GONE
        binding!!.toolbar.imgBack.visibility = View.VISIBLE
        binding!!.toolbar.txtToolbarTitle.text = currActivity.resources.getString(R.string.timetable)
        setListner()
    }

    private fun setListner(){
        binding!!.toolbar.imgBack.setOnClickListener(this)
        binding!!.imgExpand.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.imgBack ->{
                onBackPressed()
            }
            R.id.imgExpand ->{
                if(binding!!.collapsibleCalendarView.expanded){
                    binding!!.imgExpand.setImageResource(R.drawable.arrow_up)
                    binding!!.collapsibleCalendarView.collapse(400)
                } else{
                    binding!!.imgExpand.setImageResource(R.drawable.down_arrow_icon)
                    binding!!.collapsibleCalendarView.expand(400)
                }
            }
        }
    }

    private fun setUpViewPager(){
        val adapter = AdapterCommonViewPager(currActivity,supportFragmentManager)
        myTimeTableFragment = MyTimeTableFragment()
        classTimeTableFragment = ClassTimeTableFragment()
        adapter.addFragment(myTimeTableFragment,"")
        adapter.addFragment(classTimeTableFragment,"")
        binding!!.vpPager.adapter = adapter

        binding!!.tbLayout.setupWithViewPager( binding!!.vpPager)

        for(i in 0 until binding!!.tbLayout.tabCount){
            binding!!.tbLayout.getTabAt(i)!!.customView = getCustomView(i)
        }

        binding!!.vpPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding!!.tbLayout))

        binding!!.tbLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding!!.vpPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    private fun getCustomView(position : Int) : View {
        val view = LayoutInflater.from(currActivity).inflate(R.layout.tablayout, null)
        view.title.text = customTabs[position].name
        return view
    }

    private fun sendDateToRefreshData(data : Day){
        val position = binding!!.vpPager.currentItem
        myTimeTableFragment.sendDateToRefreshData(data)
        classTimeTableFragment.sendDateToRefreshData(data)
    }

    fun scrollDataBottom(){
        //val animShow = AnimationUtils.loadAnimation( this, R.anim.view_show);
       // binding!!.toolbar.visibility = View.VISIBLE
        //binding!!.toolbar.animation = animShow
    }

    fun scrollDataTop(){
     //  val animHide = AnimationUtils.loadAnimation( this, R.anim.view_hide);
     //   binding!!.toolbar.visibility = View.GONE
      //  binding!!.toolbar.animation = animHide
        if(binding!!.collapsibleCalendarView.expanded){
            binding!!.imgExpand.setImageResource(R.drawable.arrow_up)
            binding!!.collapsibleCalendarView.collapse(400)

        }
    }

}
