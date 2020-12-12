package com.akshar.one.view.messagecenter


import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.akshar.one.R
import com.akshar.one.adapter.ClassDropDownAdapter
import com.akshar.one.databinding.DialogSelectClassAndSectionBinding
import com.akshar.one.databinding.FragmentMessageCenterBinding
import com.akshar.one.databinding.FragmentStudentListBinding
import com.akshar.one.model.ClassDropDownModel
import com.akshar.one.model.SectionList
import com.akshar.one.model.StudentListModel
import com.akshar.one.util.AppUtil
import com.akshar.one.util.AppUtils
import com.akshar.one.view.activity.MainActivity
import com.akshar.one.view.studentprofile.StudentListFragment
import com.akshar.one.view.studentprofile.adapter.StudentListAdapter
import com.akshar.one.viewmodels.student.StudentViewModel

/**
 * A simple [Fragment] subclass.
 */
class MessageCenterFragment : Fragment(),View.OnClickListener{


    private var mainActivity: MainActivity? = null
    lateinit var currActivity: Activity
    private var binding : FragmentMessageCenterBinding? = null


    companion object {
        @JvmStatic
        fun newInstance() = MessageCenterFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_message_center,container,false)
        currActivity = activity!!
        setListener()
        return binding?.root
    }

    private fun setListener(){
        binding?.rlAbsentReport!!.setOnClickListener(this)
        binding?.rlMarksReport!!.setOnClickListener(this)
        binding?.rlFeeReminder!!.setOnClickListener(this)
        binding?.rlGeneralNotification!!.setOnClickListener(this)
        binding?.rlEmployeeNotification!!.setOnClickListener(this)
        binding?.rlLateEntry!!.setOnClickListener(this)
        binding?.rlStudent!!.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.rlAbsentReport->{
                if((currActivity as MainActivity).securityList.contains("M_MC_ABSENT_REPORT")){
                    ReportActivity.open(currActivity,"Absent")
                }else{
                    AppUtils.showToast(currActivity,"You don't have access to this report",true)
                }
            }

            R.id.rlMarksReport ->{
                if((currActivity as MainActivity).securityList.equals("M_MC_MARKS_REPORT")){
                    SendMarksAndFeeReport.open(currActivity)
                }else{
                    AppUtils.showToast(currActivity,"You don't have access to this report",true)
                }

            }

            R.id.rlHomeWork ->{
                ReportActivity.open(currActivity,"homework")
            }

            R.id.rlFeeReminder ->{
                if((currActivity as MainActivity).securityList.contains("M_MC_FEE_REMINDER")){
                    ReportActivity.open(currActivity,"feeReminder")
                }else{
                    AppUtils.showToast(currActivity,"You don't have access to this report",true)
                }

            }

            R.id.rlGeneralNotification ->{
                if((currActivity as MainActivity).securityList.contains("M_MC_GENERAL_NOTIFICATION")){
                    SendNotificationMessageActivity.open(currActivity,"genralNotification")
                }else{
                    AppUtils.showToast(currActivity,"You don't have access to this report",true)
                }

            }

            R.id.rlEmployeeNotification ->{
                if((currActivity as MainActivity).securityList.equals("M_MC_EMPLOYEE_NOTIFICATION")){
                    SendNotificationMessageActivity.open(currActivity,"EmployeeNotification")
                }else{
                    AppUtils.showToast(currActivity,"You don't have access to this report",true)
                }

            }

            R.id.rlLateEntry ->{
                if((currActivity as MainActivity).securityList.equals("M_MC_LATE_ENTRY")){
                    ReportActivity.open(currActivity,"LateEntry")
                }else{
                    AppUtils.showToast(currActivity,"You don't have access to this report",true)
                }

            }

            R.id.rlStudent ->{
                if((currActivity as MainActivity).securityList.equals("M_MC_STUDENT_NOTIFICATION")){
                    SendNotificationMessageActivity.open(currActivity,"StudentNotification")
                }else{
                    AppUtils.showToast(currActivity,"You don't have access to this report",true)
                }

            }
        }
    }

}