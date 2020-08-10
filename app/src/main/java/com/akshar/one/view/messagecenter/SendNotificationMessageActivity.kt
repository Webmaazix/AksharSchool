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
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.akshar.one.R
import com.akshar.one.adapter.MySpinnerAdapter
import com.akshar.one.app.AksharSchoolApplication.Companion.context
import com.akshar.one.databinding.ActivityReportBinding
import com.akshar.one.databinding.ActivitySendNotificationMessageBinding
import com.akshar.one.databinding.DialogSelectClassAndSectionBinding
import com.akshar.one.model.*
import com.akshar.one.util.AndroidUtil
import com.akshar.one.util.AppUtil
import com.akshar.one.util.AppUtils
import com.akshar.one.view.messagecenter.adapter.*
import com.akshar.one.view.messagecenter.adapter.ClassSectionAdapterForMessage.Companion.clickParent
import com.akshar.one.view.messagecenter.adapter.ClassSectionAdapterForMessage.Companion.selectedChild
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.messagecenter.MessageCenterViewModel
import com.akshar.one.viewmodels.student.StudentViewModel
import com.akshar.one.viewmodels.timetable.TimeTableViewModel
import kotlinx.android.synthetic.main.activity_send_notification_message.*
import kotlinx.android.synthetic.main.main_toolbar.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SendNotificationMessageActivity : AppCompatActivity(),View.OnClickListener {
    private var currActivity : Activity = this
    private var binding : ActivitySendNotificationMessageBinding? = null
    private var from : String =""
    private var dialogSelectClassSectionBinding: DialogSelectClassAndSectionBinding? = null
    private var dialog: Dialog? = null
    lateinit var ClassSectionAdapterForMessage: ClassSectionAdapterForMessage
    private var classDropdownList = ArrayList<ClassDropDownModel>()
    private var selectedSectionList = ArrayList<SectionList>()
    private var selectedStudentProfileId = ArrayList<Int>()
    private var selectedSectionId = ArrayList<Int>()
    private var messageViewModel: MessageCenterViewModel? = null
    private var date : DatePickerDialog.OnDateSetListener? =null
    private var  myCalendar : Calendar? = null
    private var message : String = ""
    private var profileType : String = ""
    private var studentViewModel: StudentViewModel? = null
    private var studentList = ArrayList<StudentListModel>()
    lateinit var studentadapter: StudentListAdapter
    private var timeTableViewModel: TimeTableViewModel? = null
    private lateinit var employeeDepartmentAdapter : EmployeeDepartmentAdapter
    private var employeeDepartmentList = ArrayList<EmployeeDepartmentList>()
    private var employeeList = ArrayList<EmployeeList>()
    private var selectedEmployeeDepartmentList = ArrayList<String>()
    private lateinit var employeeAdapter  : EmployeeListAdapter
    private var selectedEmployeeProfileId = ArrayList<Int>()
    private var examTime =""
    private var mobileNumber =""
    private var mobileList = ArrayList<String>()

    var examTimeList = arrayOf("07:00 AM", "07:30 AM", "08:00 AM", "08:30 AM", "09:00 AM", "09:30 AM", "10:00 AM", "10:30 AM",
        "11:00 AM","11:30 AM","12:00 PM","12:30 PM","01:00 PM","01:30 PM","02:00 PM","02:30 PM","03:00 PM","03:30 PM","04:00 PM","04:30 PM",
        "05:00 PM","05:30 PM","06:00PM","06:30PM","07:00PM")


    companion object{
        fun open(currActivity : Activity,from : String){
            val intent = Intent(currActivity,SendNotificationMessageActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("from",from)
            currActivity.startActivity(intent)
            currActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(currActivity,R.layout.activity_send_notification_message)
        from = intent.getStringExtra("from")!!
        myCalendar = Calendar.getInstance();
        initViews()
        showData()
        setAdapter()

    }

    private fun initViews(){
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


        binding!!.sbStartTime.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                if(examTimeList[p2].contains("AM")){
                    examTime = examTimeList[p2].replace(" AM","")
                }else if(examTimeList[p2].contains("PM")){
                    examTime = examTimeList[p2].replace(" PM","")
                }

            }

        }

        val aa = MySpinnerAdapter(this, R.layout.spinner_item, examTimeList)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //Setting the ArrayAdapter data on the Spinner
        binding!!.sbStartTime.adapter = aa

        setListener()
        observer()
    }

    private fun showData(){
        binding!!.toolbar.imgMenu.visibility = View.GONE
        binding!!.toolbar.imgBack.visibility = View.VISIBLE
        when {
            from == "genralNotification" -> {
                binding!!.rlAddRecipient.visibility = View.VISIBLE
                binding!!.rlClassSection.visibility = View.GONE
                binding!!.rlStudentList.visibility = View.GONE
                binding!!.cbScheduleMessage1.visibility = View.VISIBLE
                binding!!.toolbar.txtToolbarTitle.text = getString(R.string.general_notification)
            }
            from == "EmployeeNotification" -> {
                binding!!.rlAddRecipient.visibility = View.GONE
                binding!!.cbScheduleMessage1.visibility = View.GONE
                binding!!.rlClassSection.visibility = View.VISIBLE
                binding!!.rlStudentList.visibility = View.VISIBLE
                binding!!.toolbar.txtToolbarTitle.text = getString(R.string.employee_notification)
                binding!!.tvSelectClassSection.text = currActivity.resources.getString(R.string.select_department)
                messageViewModel?.let {
                    if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                        it.getEmployeeDepartments("EMP_DESIGNATION",false)
                    }
                }
                setEmployeeAdapter()

            }
            from == "StudentNotification" ->{
                binding!!.rlAddRecipient.visibility = View.GONE
                binding!!.rlClassSection.visibility = View.VISIBLE
                binding!!.cbScheduleMessage1.visibility = View.GONE
                binding!!.rlStudentList.visibility = View.VISIBLE
                binding!!.toolbar.txtToolbarTitle.text = getString(R.string.student_notification)
                binding!!.tvSelectClassSection.text = currActivity.resources.getString(R.string.select_class_section)

            }

        }
    }

    private fun setAdapter() {
        binding!!.rvStudents.setHasFixedSize(true)
        binding!!.rvStudents.layoutManager =
            LinearLayoutManager(currActivity, LinearLayoutManager.VERTICAL, false)
        studentadapter =
            StudentListAdapter(currActivity, studentList)
        binding!!.rvStudents.adapter = studentadapter
    }
    private fun setEmployeeAdapter() {
        binding!!.rvStudents.setHasFixedSize(true)
        binding!!.rvStudents.layoutManager =
            LinearLayoutManager(currActivity, LinearLayoutManager.VERTICAL, false)
        employeeAdapter =
            EmployeeListAdapter(currActivity, employeeList)
        binding!!.rvStudents.adapter = employeeAdapter
    }


    private fun openDialog() {
        selectedSectionId.clear()
        selectedSectionList.clear()
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
            if (!selectedSectionList.isNullOrEmpty() && selectedSectionList.size == 1) {
                val className = selectedSectionList[0].courseName + " " + selectedSectionList[0].classroomName
                binding!!.tvSelectClassSection.text = "$className..."
                studentViewModel.let {
                    if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                        it!!.getStudentList(selectedSectionList[0].classroomId)
                    }
                }
                binding!!.tvSerialNumber.visibility = View.VISIBLE
                binding!!.cbSelectAllStudents.visibility = View.VISIBLE

            }else if(!selectedSectionList.isNullOrEmpty()){
                val className = selectedSectionList.size
                binding!!.tvSelectClassSection.text = "$className Classes Selected"
                binding!!.tvEmptyText.text = "$className Classes Selected"
                binding!!.tvSerialNumber.visibility = View.GONE
                binding!!.cbSelectAllStudents.visibility = View.GONE
            }
            dialog!!.dismiss()
        }

        dialog!!.setCancelable(true)
        dialog!!.show()
    }

    private fun openEmployeeDepartment(){
        selectedEmployeeDepartmentList.clear()
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

        employeeDepartmentAdapter = EmployeeDepartmentAdapter(currActivity, employeeDepartmentList)
        dialogSelectClassSectionBinding!!.rlClassesDropdown.adapter = employeeDepartmentAdapter
        employeeDepartmentAdapter.notifyDataSetChanged()

        dialogSelectClassSectionBinding!!.imgCancel.setOnClickListener {
                for (data in employeeDepartmentList) {

                    if (data.isSelected) {
                        selectedEmployeeDepartmentList.add(data.value)
                        binding!!.tvSelectClassSection.text = selectedEmployeeDepartmentList[0]+"..."
                    }
                }

            messageViewModel?.let {
                if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                    it.getEmployeeProfilesByDepartmentList(selectedEmployeeDepartmentList)
                }
            }

            dialog!!.dismiss()
        }



        dialog!!.setCancelable(true)
        dialog!!.show()
    }

    private fun setListener() {
        date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar!!.set(Calendar.YEAR, year)
            myCalendar!!.set(Calendar.MONTH, monthOfYear)
            myCalendar!!.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel()
        }
        binding!!.toolbar.imgBack.setOnClickListener(this)

        binding?.etTypeMessage!!.addTextChangedListener(object : TextWatcher {
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

        binding?.tvSend!!.setOnClickListener(this)
        binding?.rlAddRecipient!!.setOnClickListener(this)

        binding!!.cbScheduleMessage.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding?.tvSend!!.text = resources.getString(R.string.schedule_message)
                binding?.llAbsentDateShift!!.visibility = View.VISIBLE
            } else {
                binding?.tvSend!!.text = resources.getString(R.string.send_message)
                binding?.llAbsentDateShift!!.visibility = View.GONE

            }

        }
        binding!!.cbScheduleMessage1.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding?.tvSend!!.text = resources.getString(R.string.schedule_message)
                binding?.llAbsentDateShift!!.visibility = View.VISIBLE
            } else {
                binding?.tvSend!!.text = resources.getString(R.string.send_message)
                binding?.llAbsentDateShift!!.visibility = View.GONE

            }

        }

        binding!!.etMobileNumber.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                mobileNumber = p0.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })

        binding!!.tvDone.setOnClickListener(this)

        binding!!.cbSelectAllStudents.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if (from == "StudentNotification") {
                    if (studentList.size > 0) {
                        for (model in studentList) {
                            model.isSelected = true
                        }
                        studentadapter.notifyDataSetChanged()
                    }
                } else if (from == "EmployeeNotification") {
                    if (employeeList.size > 0) {
                        for (model in employeeList) {
                            model.isSelected = true
                        }
                    }

                    employeeAdapter.notifyDataSetChanged()
                }
            } else {
                if (from == "StudentNotification") {
                    if (studentList.size > 0) {
                        for (model in studentList) {
                            model.isSelected = false
                        }
                        studentadapter.notifyDataSetChanged()
                    }
                } else if (from == "EmployeeNotification") {
                    if (employeeList.size > 0) {
                        for (model in employeeList) {
                            model.isSelected = false

                        }
                        employeeAdapter.notifyDataSetChanged()
                    }
                }


            }

        }
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {

            R.id.imgBack ->{
                onBackPressed()
            }
            R.id.rlClassSection -> {
                if(from == "StudentNotification"){
                    openDialog()
                }else if(from == "EmployeeNotification"){
                    openEmployeeDepartment()
                }

            }
            R.id.tvDone ->{
                if(validateMobile()){
                    mobileList.add(mobileNumber)
                    var number = ""
                    for(index in  0 until mobileList.size){
                        if(number == ""){
                            number = mobileList[index]
                        }else{
                            number = number+" ,"+mobileList[index]
                        }

                    }
                    binding!!.etMobileNumber.setText("")
                    binding!!.tvMobileNumberList.visibility = View.VISIBLE
                    binding!!.llMobile.visibility = View.GONE
                    binding!!.tvMobileNumberList.text = number
                }
            }
            R.id.tvSelectDate ->{
                DatePickerDialog(
                    this, date,
                    myCalendar!!.get(Calendar.YEAR), myCalendar!!.get(Calendar.MONTH),
                    myCalendar!!.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
            R.id.tvSend ->{
                if(validation()){
                    if(from == "EmployeeNotification"){
                        messageViewModel.let {
                            if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx) }) {
                                val model = SendGeneralNotification()

                                model.body = etTypeMessage.text.toString()
                                if(binding!!.cbScheduleMessage.isChecked){
                                    val time = "T"+examTime+":00"
                                    model.scheduledDateTime = binding!!.tvSelectDate.text.toString()+time
                                }
                                model.type = "SMS"
                                for(data in employeeList){
                                    if(data.isSelected){
                                        selectedEmployeeProfileId.add(data.employeeProfileId)
                                    }
                                }
                                model.employeeProfileIdList
                                it!!.sendGeneralNotification(model)
                            }
                        }

                    }else if( from == "StudentNotification"){
                        messageViewModel.let {
                            if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx) }) {
                                val model = SendGeneralNotification()

                                if(selectedSectionList.size == 1){
                                    for(data in studentList){
                                        if(data.isSelected){
                                            selectedStudentProfileId.add(data.studentProfileId!!)
                                        }
                                    }
                                    model.studentProfileIdList = selectedStudentProfileId
                                }else{
                                    model.classroomIdList = selectedSectionId
                                }
                                model.body = etTypeMessage.text.toString()
                                if(binding!!.cbScheduleMessage.isChecked){
                                    val time = "T"+examTime+":00"
                                    model.scheduledDateTime = binding!!.tvSelectDate.text.toString()+time
                                }
                                model.type = "SMS"
                                it!!.sendGeneralNotification(model)
                            }
                        }

                    }else if(from == "genralNotification"){
                        messageViewModel.let {
                            if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx) }) {
                                val model = SendGeneralNotification()
                                model.recipientList = mobileList
                                model.body = etTypeMessage.text.toString()
                                if(binding!!.cbScheduleMessage1.isChecked){
                                    val time = "T"+examTime+":00"
                                    model.scheduledDateTime = binding!!.tvSelectDate.text.toString()+time
                                }
                                model.type = "SMS"
                                it!!.sendGeneralNotification(model)
                            }
                        }

                    }

                }
            }

            R.id.rlAddRecipient ->{
                binding!!.llMobile.visibility = View.VISIBLE

            }

        }
    }

    private fun validateMobile() : Boolean{
        var isValid = true
        if(mobileNumber.length < 10){
            isValid = false
            AppUtils.showToast(currActivity,"Please enter a valid number",true)
        }
        return isValid
    }

    private fun validation() : Boolean{
        var isValue = true
        if(selectedSectionId.isNullOrEmpty() && profileType == "Student"){
            isValue = false
            AppUtils.showToast(currActivity,"Please select class Section",true)
        }else if(binding!!.tvSelectDate.text.isNullOrEmpty()){
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
            if(it!= null){
                AndroidUtil.showToast(currActivity, it.message, true)
            }
        })

        messageViewModel?.getEmployeeDepartmentLiveData()?.observe(this, Observer {
            employeeDepartmentList.clear()
            employeeDepartmentList.addAll(it)

        })
        messageViewModel?.getEmployeeListLiveData()?.observe(this, Observer {
            employeeList.clear()
            employeeList.addAll(it)

        })

        studentViewModel?.getIsLoading()?.observe(this, Observer {
        })

        studentViewModel?.getErrorMutableLiveData()?.observe(this, Observer {
            it.let {
                AndroidUtil.showToast(currActivity, it.message, true)
            }
        })
        messageViewModel?.getSentNotificationLiveData()?.observe(this, Observer {
            it.let {
                if(binding!!.cbScheduleMessage.isChecked){
                    AndroidUtil.showToast(currActivity, "Message Scheduled Successfully", false)
                    onBackPressed()
                }else{
                    AndroidUtil.showToast(currActivity, "Message Sent Successfully", false)
                    onBackPressed()
                }

            }
        })

        studentViewModel?.getStudentListLiveData()?.observe(this, Observer {
            studentList.clear()
            studentList.addAll(it)
            if (studentList.size > 0) {
                binding!!.tvSerialNumber.visibility = View.VISIBLE
                binding!!.cbSelectAllStudents.visibility = View.VISIBLE
                binding!!.rlNoDataFound.visibility = View.GONE
                binding!!.rvStudents.visibility = View.VISIBLE
            } else {
                binding!!.tvSerialNumber.visibility = View.GONE
                binding!!.cbSelectAllStudents.visibility = View.GONE
                binding!!.rlNoDataFound.visibility = View.GONE
                binding!!.rvStudents.visibility = View.VISIBLE
            }
            studentadapter.notifyDataSetChanged()
        })


    }

}
