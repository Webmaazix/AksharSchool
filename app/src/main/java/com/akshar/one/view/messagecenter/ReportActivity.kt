package com.akshar.one.view.messagecenter

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.akshar.one.R
import com.akshar.one.app.AksharSchoolApplication.Companion.context
import com.akshar.one.databinding.ActivityReportBinding
import com.akshar.one.databinding.DialogSelectClassAndSectionBinding
import com.akshar.one.model.*
import com.akshar.one.util.AndroidUtil
import com.akshar.one.util.AppUtils
import com.akshar.one.view.messagecenter.adapter.AbsentStudentListAdapter
import com.akshar.one.view.messagecenter.adapter.ClassSectionAdapterForMessage
import com.akshar.one.view.messagecenter.adapter.ClassSectionAdapterForMessage.Companion.clickParent
import com.akshar.one.view.messagecenter.adapter.ClassSectionAdapterForMessage.Companion.selectedChild
import com.akshar.one.view.messagecenter.adapter.ShiftListAdapter
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.messagecenter.MessageCenterViewModel
import com.akshar.one.viewmodels.timetable.TimeTableViewModel
import kotlinx.android.synthetic.main.activity_create_notice.*
import kotlinx.android.synthetic.main.main_toolbar.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ReportActivity : AppCompatActivity(),View.OnClickListener {
    private var currActivity : Activity = this
    private var binding : ActivityReportBinding? = null
    private var from : String =""
    private var dialogSelectClassSectionBinding: DialogSelectClassAndSectionBinding? = null
    private var dialog: Dialog? = null
    lateinit var ClassSectionAdapterForMessage: ClassSectionAdapterForMessage
    lateinit var shiftAdapter: ShiftListAdapter
    private var classDropdownList = ArrayList<ClassDropDownModel>()
    private var shiftList = ArrayList<ShiftList>()
    private var selectedShiftList = ArrayList<ShiftList>()
    private var selectedShiftId = ArrayList<Int>()
    private var selectedSectionList = ArrayList<SectionList>()
    private var selectedSectionId = ArrayList<Int>()
    private var messageViewModel: MessageCenterViewModel? = null
    private var studentList = ArrayList<AbsentStudentList>()
    lateinit var studentadapter: AbsentStudentListAdapter
    private var date : DatePickerDialog.OnDateSetListener? =null
    private var  myCalendar : Calendar? = null
    private var message : String = ""
    private var profileType : String = ""
    private var timeTableViewModel: TimeTableViewModel? = null


    companion object{
        fun open(currActivity : Activity,from : String){
            val intent = Intent(currActivity,ReportActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("from",from)
            currActivity.startActivity(intent)
            currActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(currActivity,R.layout.activity_report)
        from = intent.getStringExtra("from")!!
        myCalendar = Calendar.getInstance();
        initViews()
        showData()

    }
    private fun initViews(){
        binding!!.toolbar.imgMenu.visibility = View.GONE
        binding!!.toolbar.imgBack.visibility = View.VISIBLE
        binding!!.toolbar.txtToolbarTitle.text = getString(R.string.send_report)
        currActivity.application?.let {
            messageViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(MessageCenterViewModel::class.java)
        }

        currActivity.application?.let {
            timeTableViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(TimeTableViewModel::class.java)
        }

        timeTableViewModel?.let {
            if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                it.getClassRoomDropdown()
            }
        }

        setAdapter()
        setListener()
        observer()
    }

    private fun showData(){
        if(from.equals("Absent")){
            binding!!.rlClassSection.visibility = View.VISIBLE
            binding!!.llAbsentDateShift.visibility = View.VISIBLE
            binding!!.tvSelectExaminationType.visibility = View.GONE
            binding!!.etTypeMessage.visibility = View.GONE
            binding!!.tvSend.text = getString(R.string.send_absent_report)
        }else if(from.equals("MarksReport")){
            binding!!.rlClassSection.visibility = View.VISIBLE
            binding!!.llAbsentDateShift.visibility = View.GONE
            binding!!.tvSelectExaminationType.visibility = View.VISIBLE
            binding!!.etTypeMessage.visibility = View.GONE
            binding!!.tvSend.text = getString(R.string.send_marks_report)

        }else if(from.equals("homework")){
            binding!!.rlClassSection.visibility = View.VISIBLE
            binding!!.llAbsentDateShift.visibility = View.GONE
            binding!!.tvSelectExaminationType.visibility = View.GONE
            binding!!.etTypeMessage.visibility = View.VISIBLE
            binding!!.tvSend.text = getString(R.string.send_home_work)

        }else if(from.equals("feeReminder")){
            binding!!.rlClassSection.visibility = View.VISIBLE
            binding!!.llAbsentDateShift.visibility = View.VISIBLE
            binding!!.tvSelectExaminationType.visibility = View.GONE
            binding!!.rlProfileType.visibility = View.GONE
            binding!!.tvSelectDate.visibility = View.GONE
            binding!!.etTypeMessage.visibility = View.GONE
            binding!!.tvSend.text = getString(R.string.send_fee_reminder)

        }else if(from.equals("genralNotification")){

            binding!!.tvSend.text = getString(R.string.send_general_notification)

        }else if(from.equals("EmployeeNotification")){
            binding!!.rlClassSection.visibility = View.GONE
            binding!!.llAbsentDateShift.visibility = View.GONE
            binding!!.tvSelectExaminationType.visibility = View.GONE
            binding!!.etTypeMessage.visibility = View.VISIBLE
            binding!!.tvSend.text = getString(R.string.send_employee_notification)

        }else if(from.equals("LateEntry")){
            binding!!.rlClassSection.visibility = View.VISIBLE
            binding!!.llAbsentDateShift.visibility = View.VISIBLE
            binding!!.tvSelectExaminationType.visibility = View.GONE
            binding!!.etTypeMessage.visibility = View.GONE
            binding!!.tvSend.text = getString(R.string.send_late_entry)
        }
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
        dialogSelectClassSectionBinding!!.imgCancel.setImageResource(R.drawable.done)
        dialogSelectClassSectionBinding!!.rlClassesDropdown.setHasFixedSize(true)
        dialogSelectClassSectionBinding!!.rlClassesDropdown.layoutManager = LinearLayoutManager(
            currActivity,
            LinearLayoutManager.VERTICAL, false
        )
        selectedChild = -1
        clickParent = -1
        ClassSectionAdapterForMessage = ClassSectionAdapterForMessage(currActivity, classDropdownList, null, object :
            ClassSectionAdapterForMessage.SectionSelection {
            override fun selectionCallback(parent: Int, child: Int) {
                ClassSectionAdapterForMessage.notifyDataSetChanged()
            }

        })
        dialogSelectClassSectionBinding!!.rlClassesDropdown.adapter = ClassSectionAdapterForMessage
        ClassSectionAdapterForMessage.notifyDataSetChanged()

        dialogSelectClassSectionBinding!!.imgCancel.setOnClickListener {
            for (model in classDropdownList) {
                for (data in model.classroomsList) {
                    if (data.isSelected) {
                        selectedSectionList.add(data)
                        selectedSectionId.add(data.classroomId)
                    }
                }
            }
            if (!selectedSectionList.isNullOrEmpty()) {
                val className = selectedSectionList[0].courseName + " " + selectedSectionList[0].classroomName
                binding!!.tvSelectClassSection.text = "$className..."
                messageViewModel.let {
                    if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx) }) {
                        if(profileType.equals("Student")){
                            it!!.getShiftList(profileType, selectedSectionId)
                        }

                    }
                }

            }
            dialog!!.dismiss()
        }

        dialog!!.setCancelable(true)
        dialog!!.show()
    }

    private fun openShiftDialog() {
        dialog = Dialog(currActivity as Context)
        dialogSelectClassSectionBinding = DataBindingUtil.inflate(
            LayoutInflater.from(currActivity),
            R.layout.dialog_select_class_and_section, null, false
        )
        dialog!!.setContentView(dialogSelectClassSectionBinding!!.getRoot())
        Objects.requireNonNull<Window>(dialog!!.getWindow())
            .setBackgroundDrawableResource(android.R.color.transparent)
        dialogSelectClassSectionBinding!!.imgCancel.setImageResource(R.drawable.done)
        dialogSelectClassSectionBinding!!.rlClassesDropdown.setHasFixedSize(true)
        dialogSelectClassSectionBinding!!.rlClassesDropdown.layoutManager = LinearLayoutManager(
            currActivity,
            LinearLayoutManager.VERTICAL, false
        )
        shiftAdapter = ShiftListAdapter(currActivity, shiftList)
        dialogSelectClassSectionBinding!!.rlClassesDropdown.adapter = shiftAdapter
        shiftAdapter.notifyDataSetChanged()

        dialogSelectClassSectionBinding!!.imgCancel.setOnClickListener {
            for(model in shiftList){
                    if(model.isSelected){
                        selectedShiftList.add(model)
                        selectedShiftId.add(model.shiftId)
                    }
            }

            if(!selectedShiftList.isNullOrEmpty()){
                binding!!.tvSelectShift.text = selectedShiftList[0].shiftName+" ..."
                messageViewModel.let {
                    if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx) }) {
                        if(from.equals("Absent")){
                            val model = GetAbsentRequest()
                            model.classroomIdList = selectedSectionId
                            model.shiftIdList = selectedShiftId
                            model.date = binding!!.tvSelectDate.text.toString()
                            model.notificationType = "SMS"
                            if(profileType == "Student"){
                                model.category = "STUDENT_ABSENT_REPORT"
                                it!!.getStudentList(model)
                            }else if(profileType == "Employee"){
                                model.category = "EMPLOYEE_ABSENT_REPORT"
                                it!!.getAbsentEmployeeList(model)
                            }
                        }else if(from.equals("LateEntry")){
                            val model = GetAbsentRequest()
                            model.classroomIdList = selectedSectionId
                            model.shiftIdList = selectedShiftId
                            model.date = binding!!.tvSelectDate.text.toString()
                            model.notificationType = "EMAIL"
                            if(profileType == "Student"){
                                model.category = "STUDENT_LATE_ENTRY"
                                it!!.getLateStudentList(model)
                            }else if(profileType == "Employee"){
                                model.category = "EMPLOYEE_LATE_ENTRY"
                                it!!.getLateEmployeeList(model)
                            }
                        }


                    }
                }
            }

            dialog!!.dismiss()
        }

        dialog!!.setCancelable(true)
        dialog!!.show()
    }

//    fun sectionClicked(data : ClassDropDownModel, model : SectionList) {
//        classRoomId = model.classroomId
//        classDropDownModel = data
//        sectionModel = model
//
//        val className = data.courseName + " " + model.classroomName
//        binding!!.tvSelectClassSection.text = className
//        studentViewModel.let {
//            if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx) }) {
//                it!!.getStudentList(classRoomId)
//            }
//        }
//        dialog!!.dismiss()
//    }

    private fun setAdapter() {
        binding!!.rvStudents.setHasFixedSize(true)
        binding!!.rvStudents.layoutManager =
            LinearLayoutManager(currActivity, LinearLayoutManager.VERTICAL, false)
        studentadapter =
            AbsentStudentListAdapter(currActivity, studentList)
        binding!!.rvStudents.adapter = studentadapter
    }

    private fun setListener() {
        date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar!!.set(Calendar.YEAR, year)
            myCalendar!!.set(Calendar.MONTH, monthOfYear)
            myCalendar!!.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel()
        }

        binding!!.toolbar.imgBack.setOnClickListener(this)
        binding?.etTypeMessage!!.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
              message = p0!!.toString()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })
        binding?.rlClassSection!!.setOnClickListener(this)
        binding?.tvSelectDate!!.setOnClickListener(this)
        binding?.tvSelectShift!!.setOnClickListener(this)
        binding?.tvSend!!.setOnClickListener(this)
        binding!!.rgProfileType.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.rbStudentProfile) {
                profileType = "Student"
                binding!!.rlClassSection.visibility = View.VISIBLE
                binding!!.tvSerialNumber.text = getString(R.string.showing_all_students)
            } else if (checkedId == R.id.rbEmployeeProfile) {
                profileType = "Employee"
                binding!!.rlClassSection.visibility = View.GONE
                binding!!.tvSerialNumber.text = getString(R.string.showing_all_employees)
                messageViewModel.let {
                    if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx) }) {
                        it!!.getShiftList(profileType,null)
                    }
                }



            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.rlClassSection -> {
                openDialog()
            }
            R.id.imgBack -> {
                onBackPressed()
            }
            R.id.tvSelectDate ->{
                DatePickerDialog(
                    this@ReportActivity, date,
                    myCalendar!!.get(Calendar.YEAR), myCalendar!!.get(Calendar.MONTH),
                    myCalendar!!.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
            R.id.tvSelectShift -> {
                if(from.equals("feeReminder")){
                    openFeeHeadDialog()
                }else{
                    openShiftDialog()
                }

            }
            R.id.tvSend ->{
                if(validation()){
                    messageViewModel.let {
                        showProgressBar()
                        if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx) }) {
                            if(from.equals("Absent")){
                                val model = GetAbsentRequest()
                                model.classroomIdList = selectedSectionId
                                model.shiftIdList = selectedShiftId
                                model.date = binding!!.tvSelectDate.text.toString()
                                model.notificationType = "SMS"
                                if(profileType == "Student"){
                                    model.category = "STUDENT_ABSENT_REPORT"
                                    it!!.sendAttendanceReport(model)
                                }else if(profileType == "Employee"){
                                    model.category = "EMPLOYEE_ABSENT_REPORT"
                                    it!!.sendAttendanceReport(model)
                                }
                            }else if(from.equals("LateEntry")){
                                val model = GetAbsentRequest()
                                model.classroomIdList = selectedSectionId
                                model.shiftIdList = selectedShiftId
                                model.date = binding!!.tvSelectDate.text.toString()
                                model.notificationType = "EMAIL"
                                if(profileType == "Student"){
                                    model.category = "STUDENT_LATE_ENTRY"
                                    it!!.sendAttendanceReport(model)
                                }else if(profileType == "Employee"){
                                    model.category = "EMPLOYEE_LATE_ENTRY"
                                    it!!.sendAttendanceReport(model)
                                }
                            }


                        }
                    }

                }
            }

        }
    }

    private fun openFeeHeadDialog(){

    }

    private fun validation() : Boolean{
        var isValue = true
        if(selectedSectionId.isNullOrEmpty() && profileType.equals("Student")){
            isValue = false
            AppUtils.showToast(currActivity,"Please select class Section",true)
        }else if(selectedShiftId.isNullOrEmpty()){
            isValue = false
            AppUtils.showToast(currActivity,"Please select shift ",true)
        }else if( binding!!.tvSelectDate.text.isNullOrEmpty()){
            isValue = false
            AppUtils.showToast(currActivity,"Please select Date ",true)
        }
        return isValue
    }

    private fun updateLabel() {
        val myFormat = "yyyy-MM-dd" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)

        binding!!.tvSelectDate.text = sdf.format(myCalendar!!.getTime())
    }


    private fun observer() {

        timeTableViewModel?.getErrorMutableLiveData()?.observe(this, Observer {
            it.let {
                AndroidUtil.showToast(currActivity, it.message, true)
            }
        })

        timeTableViewModel?.getClassRoomLiveData()?.observe(this, Observer {
            classDropdownList.clear()
            classDropdownList.addAll(it)
        })


        messageViewModel?.getIsLoading()?.observe(this, Observer {

        })

        messageViewModel?.getErrorMutableLiveData()?.observe(this, Observer {
           if(dialog!= null){
               hideProgressBar()
           }
            if(it!= null){
                AndroidUtil.showToast(currActivity, it.message, true)
            }
        })

        messageViewModel?.getStudentListLiveData()?.observe(this, Observer {
            studentList.clear()
            studentList.addAll(it)
            if (studentList.size > 0) {
                binding!!.tvSerialNumber.visibility = View.VISIBLE
                binding!!.rlNoDataFound.visibility = View.GONE
                binding!!.rvStudents.visibility = View.VISIBLE
            } else {
                binding!!.tvSerialNumber.visibility = View.GONE
                binding!!.rlNoDataFound.visibility = View.GONE
                binding!!.rvStudents.visibility = View.VISIBLE
            }
            studentadapter.notifyDataSetChanged()
        })

        messageViewModel?.getAbsentReportLiveData()?.observe(this, Observer {
            hideProgressBar()
            AppUtils.showToast(currActivity,"Absent Report Send Successfully",false)
            onBackPressed()


        })

        messageViewModel?.getShiftListLiveData()?.observe(this, Observer {
            shiftList.clear()
            shiftList.addAll(it)
        })
    }



    fun showProgressBar(){
        dialog =  AppUtils.showProgress(currActivity)
    }

    fun hideProgressBar(){
        AppUtils.hideProgress(dialog!!)
    }

}
