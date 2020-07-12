package com.akshar.one.birthday

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.akshar.one.R
import com.akshar.one.adapter.BirthDayAdapter
import com.akshar.one.databinding.ActivityBirthdayBinding
import com.akshar.one.model.BirthDayModel
import com.akshar.one.timetable.TimeTableActivity
import com.akshar.one.util.AndroidUtil
import com.akshar.one.util.AppUtil
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.dashboard.DashboardViewModel
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.toolbar_type_square.*

class BirthdayActivity : AppCompatActivity(),View.OnClickListener {


    private var currActivity : Activity = this
    private var binding : ActivityBirthdayBinding? = null
    private var dashboardViewModel: DashboardViewModel? = null
    private var birthDayList = ArrayList<BirthDayModel>()
    private lateinit var birthDayadapter : BirthDayAdapter
    private var date : String? = null

    companion object{
        fun open(currActivity : Activity){
            val intent = Intent(currActivity, BirthdayActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            currActivity.startActivity(intent)
            currActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(currActivity,R.layout.activity_birthday)
        date = AppUtil.getCurrentDate()
        currActivity.application?.let {
            dashboardViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(DashboardViewModel::class.java)
        }
        setAdapter()
        initView()
    }

    private fun setAdapter(){
        binding!!.rvBirthdays.setHasFixedSize(true)
        binding!!.rvBirthdays.layoutManager = LinearLayoutManager(currActivity,
            LinearLayoutManager.VERTICAL,false)
        birthDayadapter = BirthDayAdapter(currActivity,birthDayList)
        binding!!.rvBirthdays.adapter = birthDayadapter
    }

    private fun initView(){
        imgMenu.visibility = View.GONE
        imgBack.visibility = View.VISIBLE
        txtToolbarTitle.text = currActivity.resources.getString(R.string.birthday)
        dashboardViewModel?.let {
            if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                it.getBirthdays(date!!,date!!) }
        }
        observers()
        setListener()
    }

    private fun observers(){
        dashboardViewModel?.getBirthdayLiveData()?.observe(this, Observer {
            birthDayList.clear()
            birthDayList.addAll(it)
            if(birthDayList.size > 0){
                if(birthDayList.size < 10){
                    val count = "0"+birthDayList.size.toString()
                    binding!!.tvBirthDayCount.text =   count
                }else{
                    binding!!.tvBirthDayCount.text = birthDayList.size.toString()
                }

                binding!!.rlBirthdayNotFound.visibility = View.GONE
                binding!!.rvBirthdays.visibility = View.VISIBLE
            }else{
                binding!!.tvBirthDayCount.text = "00"
                binding!!.rlBirthdayNotFound.visibility = View.VISIBLE
                binding!!.rvBirthdays.visibility = View.GONE
            }
            birthDayadapter.notifyDataSetChanged()
            // dashboardViewModel?.setTimeTableAdapter(it)
        })
    }

    private fun setListener(){
        rlDay.setOnClickListener(this)
        imgBack.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.rlDay ->{
                openMenu()

            }
            R.id.imgBack ->{
                onBackPressed()
            }
        }
    }

    private fun openMenu(){
        val popup = PopupMenu(currActivity,rlDay)
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(com.akshar.one.R.menu.birthday_popup, popup.getMenu())

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener {
            if(it.title.equals(currActivity.resources.getString(R.string.today))){
                binding?.tvDay!!.text = currActivity.resources.getString(R.string.today)
                dashboardViewModel?.let {
                    if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                        it.getBirthdays(date!!,date!!) }
                }

                dashboardViewModel?.getBirthdayLiveData()?.observe(this, Observer {
                    birthDayList.clear()
                    birthDayList.addAll(it)
                    if(birthDayList.size > 0){
                        binding!!.rlBirthdayNotFound.visibility = View.GONE
                        binding!!.rvBirthdays.visibility = View.VISIBLE
                    }else{
                        binding!!.rlBirthdayNotFound.visibility = View.VISIBLE
                        binding!!.rvBirthdays.visibility = View.GONE
                    }
                    birthDayadapter.notifyDataSetChanged()
                    // dashboardViewModel?.setTimeTableAdapter(it)
                })

            }else if(it.title.equals(currActivity.resources.getString(R.string.tomorrow))){

                binding?.tvDay!!.text = currActivity.resources.getString(R.string.tomorrow)
                val fromDate = AppUtil.getTomorrowDateString()
                dashboardViewModel?.let {
                    if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                        it.getBirthdays(fromDate,date!!) }
                }

                dashboardViewModel?.getBirthdayLiveData()?.observe(this, Observer {
                    birthDayList.clear()
                    birthDayList.addAll(it)
                    if(birthDayList.size > 0){

                        binding!!.rlBirthdayNotFound.visibility = View.GONE
                        binding!!.rvBirthdays.visibility = View.VISIBLE
                    }else{
                        binding!!.rlBirthdayNotFound.visibility = View.VISIBLE
                        binding!!.rvBirthdays.visibility = View.GONE
                    }
                    birthDayadapter.notifyDataSetChanged()
                    // dashboardViewModel?.setTimeTableAdapter(it)
                })

            }else if(it.title.equals(currActivity.resources.getString(R.string.week))){

                binding?.tvDay!!.text = currActivity.resources.getString(R.string.week)
                val fromDate = AppUtil.getSevenDaysAfter()
                dashboardViewModel?.let {
                    if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                        it.getBirthdays(fromDate!!,date!!) }
                }

                dashboardViewModel?.getBirthdayLiveData()?.observe(this, Observer {
                    birthDayList.clear()
                    birthDayList.addAll(it)
                    if(birthDayList.size > 0){
                        binding!!.rlBirthdayNotFound.visibility = View.GONE
                        binding!!.rvBirthdays.visibility = View.VISIBLE
                    }else{
                        binding!!.rlBirthdayNotFound.visibility = View.VISIBLE
                        binding!!.rvBirthdays.visibility = View.GONE
                    }
                    birthDayadapter.notifyDataSetChanged()
                    // dashboardViewModel?.setTimeTableAdapter(it)
                })


            }
            true
        }

        popup.show()//showing popup menu

    }

}
