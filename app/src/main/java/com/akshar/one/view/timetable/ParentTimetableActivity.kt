package com.akshar.one.view.timetable

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.akshar.one.DummyContent
import com.akshar.one.R
import com.akshar.one.adapter.AdapterCommonViewPager
import com.akshar.one.app.AksharSchoolApplication.Companion.context
import com.akshar.one.calender.data.Day
import com.akshar.one.calender.widget.CollapsibleCalendar
import com.akshar.one.databinding.ActivityParentTimetableBinding
import com.akshar.one.databinding.ActivityTimeTableBinding
import com.akshar.one.manager.SessionManager
import com.akshar.one.model.AppList
import com.akshar.one.model.LoginModel
import com.akshar.one.model.PeriodTimeTable
import com.akshar.one.model.TabsModel
import com.akshar.one.util.AndroidUtil
import com.akshar.one.util.AppUtil
import com.akshar.one.util.AppUtils
import com.akshar.one.view.timetable.adapter.MyTimeTableAdapter
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.dashboard.DashboardViewModel
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.main_toolbar.view.*
import kotlinx.android.synthetic.main.tablayout.view.*

class ParentTimetableActivity : AppCompatActivity(),View.OnClickListener{


    private var currActivity : Activity = this
    private var binding : ActivityParentTimetableBinding? = null
    var customTabs = ArrayList<TabsModel>()
    private var dialog: Dialog? = null
    private var date : String? = null
    lateinit var adapter : MyTimeTableAdapter
    private var loginRole : AppList? = null
    private var timeTableList = ArrayList<PeriodTimeTable>()
    private var dashboardViewModel: DashboardViewModel? = null



    companion object{
        fun open(currActivity : Activity){
            val intent = Intent(currActivity, ParentTimetableActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            currActivity.startActivity(intent)
            currActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        }
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(currActivity,R.layout.activity_parent_timetable)
        customTabs = DummyContent.timeTable()
        loginRole = SessionManager.getLoginRole()
        date = AppUtil.getCurrentDate()
        initViews()
        setAdapter()
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
        currActivity.application?.let {
            dashboardViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(DashboardViewModel::class.java)
        }
        dashboardViewModel?.let {
            if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                showProgressBar()
                it.getTimeTableOfClass(loginRole!!.classRoomId,date!!) }
        }

        observers()

    }

    private fun setAdapter(){
        binding!!.rlActiveNotice.setHasFixedSize(true)
        binding!!.rlActiveNotice.layoutManager = LinearLayoutManager(currActivity,
            LinearLayoutManager.VERTICAL,false)
        adapter =
            MyTimeTableAdapter(currActivity, timeTableList,null)
        binding!!.rlActiveNotice.adapter = adapter
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
                    binding!!.imgExpand.setImageResource(R.drawable.down_arrow_icon)
                    binding!!.collapsibleCalendarView.collapse(400)
                } else{
                    binding!!.imgExpand.setImageResource(R.drawable.arrow_up)
                    binding!!.collapsibleCalendarView.expand(400)
                }
            }
        }
    }

    private fun observers(){
        dashboardViewModel?.getErrorMutableLiveData()?.observe(this, Observer {
            it.let {
                hideProgressBar()
                binding!!.rlNoDataFound.visibility = View.VISIBLE
                binding!!.rlActiveNotice.visibility = View.GONE
                timeTableList.clear()
                adapter.notifyDataSetChanged()
                // AndroidUtil.showToast(context!!, it.message,true)
            }
        })

        dashboardViewModel?.getClassTimeTableLiveData()?.observe(this, Observer {
            hideProgressBar()
            timeTableList.clear()
            timeTableList.addAll(it)
            if(timeTableList.size> 0){
                binding!!.rlNoDataFound.visibility = View.GONE
                binding!!.rlActiveNotice.visibility = View.VISIBLE
            }else{
                binding!!.rlNoDataFound.visibility = View.GONE
                binding!!.rlActiveNotice.visibility = View.VISIBLE
            }

            adapter.notifyDataSetChanged()
            // dashboardViewModel?.setTimeTableAdapter(it)
        })
    }

    fun sendDateToRefreshData(data : Day){
        val month = data.month+1
        val date = data.year.toString()+"-"+month.toString()+"-"+data.day
        dashboardViewModel?.let {
            if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                timeTableList.clear()
                adapter.notifyDataSetChanged()
                showProgressBar()
                it.getTimeTableOfClass(loginRole!!.classRoomId,date) }
        }

    }


    fun showProgressBar(){
        dialog =  AppUtils.showProgress(currActivity)
    }

    fun hideProgressBar(){
        AppUtils.hideProgress(dialog!!)
    }
}
