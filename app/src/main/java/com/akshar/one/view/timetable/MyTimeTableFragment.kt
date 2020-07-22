package com.akshar.one.view.timetable


import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.LinearLayoutManager

import com.akshar.one.R
import com.akshar.one.view.timetable.adapter.MyTimeTableAdapter
import com.akshar.one.calender.data.Day
import com.akshar.one.databinding.FragmentMyTimeTableBinding
import com.akshar.one.manager.SessionManager
import com.akshar.one.model.LoginModel
import com.akshar.one.model.TimeTableModel
import com.akshar.one.util.AndroidUtil
import com.akshar.one.util.AppUtil
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.dashboard.DashboardViewModel
import android.widget.Toast
import androidx.core.widget.NestedScrollView



/**
 * A simple [Fragment] subclass.
 */
class MyTimeTableFragment : Fragment(),View.OnClickListener {


    private var currActivity : Activity? = null
    private var binding : FragmentMyTimeTableBinding? = null
    private var timeTableList = ArrayList<TimeTableModel>()
    private var dashboardViewModel: DashboardViewModel? = null
    private var employeeId : Int = 0;
    private var loginModel : LoginModel? = null
    private var date : String? = null
    lateinit var adapter : MyTimeTableAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        currActivity = activity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_my_time_table,container,false)
        loginModel = SessionManager.getLoginModel()
        employeeId = loginModel.let {it?.appsList?.get(0)?.userUniqueId!!  }
        date = AppUtil.getCurrentDate()
        initViews()
        return  binding?.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.application?.let {
            dashboardViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(DashboardViewModel::class.java)
        }
        dashboardViewModel?.let {
            if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                it.getTimeTableOfEmployee(employeeId,date!!) }
        }

        observers()
    }


    private fun initViews(){
        setAdapter()
        setListner()
    }

    private fun setListner(){

        binding!!.scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->

            if (scrollY > scrollX) {
                (currActivity as TimeTableActivity).scrollDataTop()
                Toast.makeText(context, "Scrolling up", Toast.LENGTH_SHORT).show()
            } else {
                (currActivity as TimeTableActivity).scrollDataBottom()
                Toast.makeText(context, "Scrolling down", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setAdapter(){
        binding!!.rlActiveNotice.setHasFixedSize(true)
        binding!!.rlActiveNotice.layoutManager = LinearLayoutManager(currActivity,
            LinearLayoutManager.VERTICAL,false)
        adapter =
            MyTimeTableAdapter(currActivity!!, timeTableList)
        binding!!.rlActiveNotice.adapter = adapter
    }

    private fun observers(){

            dashboardViewModel?.getErrorMutableLiveData()?.observe(this, Observer {
                it?.let {
                    AndroidUtil.showToast(context!!, it.message,true)
                }
            })

            dashboardViewModel?.getTimeTableLiveData()?.observe(this, Observer {
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
               it.getTimeTableOfEmployee(employeeId,date) }
       }

   }

    override fun onClick(p0: View?) {

    }

}
