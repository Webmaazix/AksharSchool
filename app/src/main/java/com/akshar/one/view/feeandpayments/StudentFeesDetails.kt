package com.akshar.one.view.feeandpayments

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.akshar.one.DummyContent
import com.akshar.one.R
import com.akshar.one.adapter.AdapterCommonViewPager
import com.akshar.one.adapter.ClassDropDownAdapter
import com.akshar.one.app.AksharSchoolApplication.Companion.context
import com.akshar.one.calender.data.Day
import com.akshar.one.calender.widget.CollapsibleCalendar
import com.akshar.one.databinding.ActivityStudentFeesDetailsBinding
import com.akshar.one.databinding.ActivityTimeTableBinding
import com.akshar.one.databinding.DialogSelectClassAndSectionBinding
import com.akshar.one.model.ClassDropDownModel
import com.akshar.one.model.SectionList
import com.akshar.one.model.StudentListModel
import com.akshar.one.model.TabsModel
import com.akshar.one.util.AndroidUtil
import com.akshar.one.util.AppUtils
import com.akshar.one.view.studentprofile.adapter.StudentListAdapter
import com.akshar.one.view.timetable.ClassTimeTableFragment
import com.akshar.one.view.timetable.MyTimeTableFragment
import com.akshar.one.view.timetable.TimeTableActivity
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.student.StudentViewModel
import com.akshar.one.viewmodels.timetable.TimeTableViewModel
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.main_toolbar.view.*
import kotlinx.android.synthetic.main.tablayout.view.*
import java.util.*
import kotlin.collections.ArrayList

class StudentFeesDetails :AppCompatActivity(), View.OnClickListener {


    private var currActivity: Activity = this
    private var binding: ActivityStudentFeesDetailsBinding? = null
    var customTabs = ArrayList<TabsModel>()
    lateinit var feesDetailsFragment: FeesDetailsFragment
    lateinit var paymentHistoryFragment: PaymentHistoryFragment
    private var dialog: Dialog? = null
    var student = StudentListModel()
    private var dialogSelectClassSectionBinding: DialogSelectClassAndSectionBinding? = null
    lateinit var classDropDownAdapter: ClassDropDownAdapter
    private var classDropdownList = ArrayList<ClassDropDownModel>()
    private var classRoomId: Int = 0;
    private var timeTableViewModel: TimeTableViewModel? = null
    private lateinit var classDropDownModel: ClassDropDownModel
    private lateinit var sectionModel: SectionList
    private var studentViewModel: StudentViewModel? = null
    private var studentList = ArrayList<StudentListModel>()

    companion object {
        fun open(currActivity: Activity,studentListModel : StudentListModel) {
            val intent = Intent(currActivity, StudentFeesDetails::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("studentListModel",studentListModel)
            currActivity.startActivity(intent)
            currActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(currActivity, R.layout.activity_student_fees_details)
        student = intent.getSerializableExtra("studentListModel") as StudentListModel
        customTabs = DummyContent.feeModule()
        setUpViewPager()
        initViews()
    }

    private fun initViews() {

        currActivity.application?.let {
            timeTableViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(TimeTableViewModel::class.java)
        }

        currActivity.application?.let {
            studentViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(StudentViewModel::class.java)
        }

        timeTableViewModel?.let {
            if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                it.getClassRoomDropdown()
            }
        }

        binding!!.tvAllStudents.text =  student.firstName
        val className = student.courseName + " " + student.classroomName
        binding!!.etClassName.text =  className
        binding!!.toolbar.imgMenu.visibility = View.GONE
        binding!!.toolbar.imgBack.visibility = View.VISIBLE
        binding!!.toolbar.txtToolbarTitle.text =
            currActivity.resources.getString(R.string.fee_amp_payment)
        setListner()
        observer()
    }

    private fun setListner() {
        binding!!.toolbar.imgBack.setOnClickListener(this)
       // binding!!.rlClassName.setOnClickListener(this)
      //  binding!!.rlStudents.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.imgBack -> {
                onBackPressed()
            }
            R.id.rlStudents ->{

            }
            R.id.rlClassName ->{
              //  openDialog()
            }
        }
    }

    private fun setUpViewPager() {
        val adapter = AdapterCommonViewPager(currActivity, supportFragmentManager)
        feesDetailsFragment = FeesDetailsFragment()
        paymentHistoryFragment = PaymentHistoryFragment()
        adapter.addFragment(feesDetailsFragment, "")
        adapter.addFragment(paymentHistoryFragment, "")
        binding!!.vpPager.adapter = adapter

        binding!!.tbLayout.setupWithViewPager(binding!!.vpPager)

        for (i in 0 until binding!!.tbLayout.tabCount) {
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

    private fun openDialog() {
        dialog = Dialog(currActivity as Context)
        dialogSelectClassSectionBinding = DataBindingUtil.inflate(
            LayoutInflater.from(currActivity),
            R.layout.dialog_select_class_and_section, null, false
        )
        dialog!!.setContentView(dialogSelectClassSectionBinding!!.getRoot())
        Objects.requireNonNull<Window>(dialog!!.getWindow())
            .setBackgroundDrawableResource(android.R.color.transparent)
        dialogSelectClassSectionBinding!!.rlClassesDropdown.setHasFixedSize(true)
        dialogSelectClassSectionBinding!!.rlClassesDropdown.layoutManager = LinearLayoutManager(
            currActivity,
            LinearLayoutManager.VERTICAL, false
        )
        ClassDropDownAdapter.selectedChild = -1
        ClassDropDownAdapter.clickParent =-1;
        classDropDownAdapter = ClassDropDownAdapter(currActivity, classDropdownList, null, object :
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

    fun sectionClicked(data : ClassDropDownModel, model : SectionList) {
        classRoomId = model.classroomId
        classDropDownModel = data
        sectionModel = model

        val className = data.courseName + " " + model.classroomName
        binding!!.etClassName.text = className
        dialog!!.dismiss()

        studentViewModel.let {
            if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                it!!.getStudentList(classRoomId)
            }
        }
    }


    private fun observer() {

        studentViewModel?.getErrorMutableLiveData()?.observe(this, androidx.lifecycle.Observer {
            it.let {
                hideProgressBar()
                AndroidUtil.showToast(currActivity, it.message, true)
            }
        })

        timeTableViewModel?.getErrorMutableLiveData()?.observe(this, androidx.lifecycle.Observer {
            it.let {
                AndroidUtil.showToast(currActivity, it.message, true)
            }
        })

        timeTableViewModel?.getClassRoomLiveData()?.observe(this, androidx.lifecycle.Observer {
            classDropdownList.clear()
            classDropdownList.addAll(it)
        })

        studentViewModel?.getStudentListLiveData()?.observe(this, androidx.lifecycle.Observer {
            hideProgressBar()
            studentList.clear()
            studentList.addAll(it)
          //  studentadapter.notifyDataSetChanged()
        })
    }

    private fun getCustomView(position: Int): View {
        val view = LayoutInflater.from(currActivity).inflate(R.layout.tablayout, null)
        view.title.text = customTabs[position].name
        return view
    }

    fun showProgressBar(){
        dialog =  AppUtils.showProgress(currActivity)
    }

    fun hideProgressBar(){
        AppUtils.hideProgress(dialog!!)
    }



}