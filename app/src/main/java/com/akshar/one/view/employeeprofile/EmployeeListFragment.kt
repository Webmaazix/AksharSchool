package com.akshar.one.view.employeeprofile

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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.LinearLayoutManager

import com.akshar.one.R
import com.akshar.one.adapter.ClassDropDownAdapter
import com.akshar.one.view.studentprofile.adapter.StudentListAdapter
import com.akshar.one.databinding.DialogSelectClassAndSectionBinding
import com.akshar.one.databinding.FragmentEmployeeListBinding
import com.akshar.one.databinding.FragmentStudentListBinding
import com.akshar.one.model.ClassDropDownModel
import com.akshar.one.model.EmployeeList
import com.akshar.one.model.SectionList
import com.akshar.one.model.StudentListModel
import com.akshar.one.util.AndroidUtil
import com.akshar.one.util.AppUtils
import com.akshar.one.view.activity.MainActivity
import com.akshar.one.view.messagecenter.adapter.EmployeeListAdapter
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.employee.EmployeeViewModel
import com.akshar.one.viewmodels.student.StudentViewModel
import com.akshar.one.viewmodels.timetable.TimeTableViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import java.util.*
import kotlin.collections.ArrayList


class EmployeeListFragment : Fragment(),View.OnClickListener {


    private var mainActivity: MainActivity? = null
    private var binding: FragmentEmployeeListBinding? = null
    lateinit var currActivity: Activity
    private var dialogSelectClassSectionBinding: DialogSelectClassAndSectionBinding? = null
    private var dialog: Dialog? = null
    lateinit var classDropDownAdapter: ClassDropDownAdapter
    private var classDropdownList = ArrayList<ClassDropDownModel>()
    private var classRoomId: Int = 0;
    private var timeTableViewModel: TimeTableViewModel? = null
    private var employeeViewModel: EmployeeViewModel? = null
    private var employeeList = ArrayList<EmployeeList>()
    lateinit var employeeAdapter: EmployeeListAdapter
    private lateinit var classDropDownModel: ClassDropDownModel
    private lateinit var sectionModel: SectionList
    var securityList = ArrayList<String>()


    companion object {
        @JvmStatic
        fun newInstance() = EmployeeListFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_employee_list, container, false)
        currActivity = activity!!
        setListner()
        setAdapter()
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.application?.let {
            employeeViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(EmployeeViewModel::class.java)
        }

        employeeViewModel?.let {
            if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                showProgressBar()
                it.getEmployeeList()
            }
        }

        observer()


    }

    private fun setAdapter() {
        binding!!.rvStudents.setHasFixedSize(true)
        binding!!.rvStudents.layoutManager =
            LinearLayoutManager(currActivity, LinearLayoutManager.VERTICAL, false)
        employeeAdapter = EmployeeListAdapter(currActivity, employeeList)
        binding!!.rvStudents.adapter = employeeAdapter
    }

    private fun setListner() {
        binding?.imgCreateEmployee!!.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.imgCreateEmployee -> {
               // CreateEmployeeProfile.open(currActivity)
            }

        }
    }

    private fun initViews() {
        securityList = (currActivity as MainActivity).securityList
        mainActivity?.setToolbarTitle(getString(R.string.employee_list))
    }


    private fun observer() {
        employeeViewModel?.getIsLoading()?.observe(this, Observer {
            showProgressIndicator(it)
        })

        employeeViewModel?.getErrorMutableLiveData()?.observe(this, Observer {
            it.let {
                hideProgressBar()
                AndroidUtil.showToast(currActivity, it.message, true)
            }
        })



        employeeViewModel?.getEmployeeListLiveData()?.observe(this, Observer {
            hideProgressBar()
            employeeList.clear()
            employeeList.addAll(it)
            if (employeeList.size > 0) {
                binding!!.tvSerialNumber.visibility = View.VISIBLE
                binding!!.tvStudentName.visibility = View.VISIBLE
                binding!!.rlNoDataFound.visibility = View.GONE
                binding!!.rvStudents.visibility = View.VISIBLE
            } else {
                binding!!.tvSerialNumber.visibility = View.GONE
                binding!!.tvStudentName.visibility = View.GONE
                binding!!.rlNoDataFound.visibility = View.GONE
                binding!!.rvStudents.visibility = View.VISIBLE
            }
            employeeAdapter.notifyDataSetChanged()
        })
    }

    private fun showProgressIndicator(isLoading: Boolean?) {
        linProgressIndicator.visibility = if (isLoading == true) View.VISIBLE else View.GONE
    }

    fun showProgressBar(){
        dialog =  AppUtils.showProgress(currActivity)
    }

    fun hideProgressBar(){
        AppUtils.hideProgress(dialog!!)
    }

}
