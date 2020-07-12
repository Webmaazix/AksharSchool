package com.akshar.one.timetable


import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.LinearLayoutManager

import com.akshar.one.R
import com.akshar.one.adapter.ClassDropDownAdapter
import com.akshar.one.adapter.MyTimeTableAdapter
import com.akshar.one.calender.data.Day
import com.akshar.one.databinding.DialogSelectClassAndSectionBinding
import com.akshar.one.databinding.FragmentClassTimeTableBinding
import com.akshar.one.databinding.FragmentMyTimeTableBinding
import com.akshar.one.manager.SessionManager
import com.akshar.one.model.*
import com.akshar.one.util.AndroidUtil
import com.akshar.one.util.AppUtil
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.dashboard.DashboardViewModel
import com.akshar.one.viewmodels.timetable.TimeTableViewModel
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class ClassTimeTableFragment : Fragment() ,View.OnClickListener{

    private var currActivity : Activity? = null
    private var binding : FragmentClassTimeTableBinding? = null
    private var timeTableList = ArrayList<TimeTableModel>()
    private var dashboardViewModel: DashboardViewModel? = null
    private var timeTableViewModel : TimeTableViewModel? = null
    private var classRoomId : Int = 0;
    private var loginModel : LoginModel? = null
    private var date : String? = null
    lateinit var adapter : MyTimeTableAdapter
    lateinit var classDropDownAdapter : ClassDropDownAdapter
    private var classDropdownList = ArrayList<ClassDropDownModel>()
    private var dialogSelectClassSectionBinding : DialogSelectClassAndSectionBinding? = null
    private var dialog : Dialog? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        currActivity = activity
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_class_time_table,container,false)
        loginModel = SessionManager.getLoginModel()
       // employeeId = loginModel.let {it?.appsList?.get(0)?.userUniqueId!!  }
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
        activity?.application?.let {
            timeTableViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(TimeTableViewModel::class.java)
        }
//        dashboardViewModel?.let {
//            if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
//                it.getTimeTableOfClass(classRoomId,date!!) }
//        }

        timeTableViewModel?.let {
            if(context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true){
                it.getClassRoomDropdown()
            }
        }

        observers()
    }


    private fun initViews(){
        setListner()
        setAdapter()
    }

    private fun setListner(){
        binding!!.rlClassSection.setOnClickListener(this)
    }

    private fun setAdapter(){
        binding!!.rlClassTimeTable.setHasFixedSize(true)
        binding!!.rlClassTimeTable.layoutManager = LinearLayoutManager(currActivity,
            LinearLayoutManager.VERTICAL,false)
        adapter = MyTimeTableAdapter(currActivity!!,timeTableList)
        binding!!.rlClassTimeTable.adapter = adapter
    }

    private fun observers(){

        dashboardViewModel?.getErrorMutableLiveData()?.observe(this, Observer {
            it?.let {
                AndroidUtil.showToast(context!!, it.message,true)
            }
        })

        dashboardViewModel?.getClassTimeTableLiveData()?.observe(this, Observer {
            timeTableList.clear()
            timeTableList.addAll(it)
            if(timeTableList.size> 0){
                binding!!.rlNoDataFound.visibility = View.GONE
                binding!!.rlClassTimeTable.visibility = View.VISIBLE
            }else{
                binding!!.rlNoDataFound.visibility = View.GONE
                binding!!.rlClassTimeTable.visibility = View.VISIBLE
            }

            adapter.notifyDataSetChanged()
            // dashboardViewModel?.setTimeTableAdapter(it)
        })


        timeTableViewModel?.getClassRoomLiveData()?.observe(this, Observer {
            classDropdownList.clear()
            classDropdownList.addAll(it)
        })

    }

    fun sendDateToRefreshData(data : Day){
         date = data.year.toString()+"-"+data.month.toString()+"-"+data.day
        dashboardViewModel?.let {
            if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                it.getTimeTableOfClass(classRoomId,date!!) }
        }

    }

    override fun onClick(p0: View?) {

        when(p0!!.id){
            R.id.rlClassSection ->{
                openDialog()
            }
        }
    }


    var clickParent=-1;

    private fun openDialog(){
        dialog = Dialog(currActivity as Context)
        dialogSelectClassSectionBinding = DataBindingUtil.inflate(LayoutInflater.from(currActivity),
            R.layout.dialog_select_class_and_section,null,false)
        dialog!!.setContentView(dialogSelectClassSectionBinding!!.getRoot())
        Objects.requireNonNull<Window>(dialog!!.getWindow())
            .setBackgroundDrawableResource(android.R.color.transparent)
        dialogSelectClassSectionBinding!!.rlClassesDropdown.setHasFixedSize(true)
        dialogSelectClassSectionBinding!!.rlClassesDropdown.layoutManager = LinearLayoutManager(currActivity,LinearLayoutManager.VERTICAL,false)
        classDropDownAdapter = ClassDropDownAdapter(currActivity!!,classDropdownList,this,object :
            ClassDropDownAdapter.SectionSelection {
            override fun selectionCallback(parent: Int, child: Int) {
                classDropDownAdapter.notifyDataSetChanged()
            }

        })
        dialogSelectClassSectionBinding!!.rlClassesDropdown.adapter = classDropDownAdapter
        classDropDownAdapter.notifyDataSetChanged()

        dialogSelectClassSectionBinding!!.imgCancel.setOnClickListener {
            dialog!!.dismiss()
        }

        dialog!!.setCancelable(true)
        dialog!!.show()
    }

    fun sectionClicked(data : ClassDropDownModel,model : SectionList){
        classRoomId = model.classroomId!!
        val className = data.courseName+" "+model.classroomName
        binding!!.tvClassSectionName.text = className
        dashboardViewModel?.let {
            if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                it.getTimeTableOfClass(classRoomId,date!!) }
        }

        dialog!!.dismiss()
    }

}

