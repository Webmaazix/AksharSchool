package com.akshar.one.view.examschedule

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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.akshar.one.R
import com.akshar.one.adapter.ClassDropDownAdapter
import com.akshar.one.adapter.ExaminationDropDownAdapter
import com.akshar.one.databinding.ActivityScheduleExamBinding
import com.akshar.one.databinding.DialogSelectClassAndSectionBinding
import com.akshar.one.util.AndroidUtil
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.examination.ExamViewModel
import com.akshar.one.viewmodels.timetable.TimeTableViewModel
import kotlinx.android.synthetic.main.activity_create_notice.*
import kotlinx.android.synthetic.main.main_toolbar.view.*
import java.text.SimpleDateFormat
import java.util.*
import com.akshar.one.adapter.MySpinnerAdapter
import com.akshar.one.model.*


class ScheduleExamActivity : AppCompatActivity(),View.OnClickListener {

    private var currActivity : Activity = this
    private var examPostModel = ExamPostModel()
    private var binding : ActivityScheduleExamBinding? = null
    private var examViewModel: ExamViewModel? = null
    private var  myCalendar : Calendar? = null
    private var endDate : DatePickerDialog.OnDateSetListener? =null
    private var startDate : DatePickerDialog.OnDateSetListener? =null
    private var visibility = "All"
    private var dialogSelectClassSectionBinding: DialogSelectClassAndSectionBinding? = null
    private var dialog: Dialog? = null
    lateinit var classDropDownAdapter: ClassDropDownAdapter
    lateinit var examDropDownAdapter: ExaminationDropDownAdapter
    private var classDropdownList = ArrayList<ClassDropDownModel>()
    private var examDropdownList = ArrayList<ExaminationDropDownModel>()
    private var timeTableViewModel: TimeTableViewModel? = null
    private lateinit var classDropDownModel: ClassDropDownModel
    private lateinit var sectionModel: SectionList
    private var classRoomId: Int = 0;
    private lateinit var examDropDownModel : ExaminationDropDownModel
    private var examId: Int = 0;
    private var testId: Int = 0;
    private var duration: Int = 0;
    private var openTest = true
    private var examTime =""
    var classDropDownModelUpdate: ClassDropDownModel? = null
    var sectionModelUpdate: SectionList? =null
    var testModelUpdate : TestListModel? =null
    var examDropDownModelUpdate : ExaminationDropDownModel? = null
    private var scheduledModel : ScheduleList? = null
    var examTimeList = arrayOf("07:00", "07:30", "08:00", "08:30", "09:00", "09:30", "10:00", "10:30",
        "11:00","11:30","12:00","12:30","01:00","01:30","02:00","02:30","03:00","03:30","04:00","04:30",
        "05:00","05:30","06:00","06:30","07:00")

    companion object{
        fun open(currActivity : Activity, model : ScheduleList?, classDropDown : ClassDropDownModel?,
                section : SectionList?, examDropDown : ExaminationDropDownModel?,test : TestListModel? ){
            val intent = Intent(currActivity, ScheduleExamActivity::class.java)
            intent.putExtra("model",model)
            intent.putExtra("classDropDown",classDropDown)
            intent.putExtra("section",section)
            intent.putExtra("examDropDown",examDropDown)
            intent.putExtra("test",test)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            currActivity.startActivity(intent)
            currActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(currActivity,R.layout.activity_schedule_exam)
        scheduledModel = intent.getSerializableExtra("model") as ScheduleList
        classDropDownModelUpdate = intent.getSerializableExtra("classDropDown") as ClassDropDownModel
        sectionModelUpdate = intent.getSerializableExtra("section") as SectionList
        examDropDownModelUpdate = intent.getSerializableExtra("examDropDown") as ExaminationDropDownModel
        testModelUpdate = intent.getSerializableExtra("test") as TestListModel

        if(scheduledModel != null){
            setData()
        }
        myCalendar = Calendar.getInstance();
        initViews()
    }

    private fun setData(){
        binding!!.tvStartDate.text = scheduledModel?.date
        binding!!.etDuration.setText(scheduledModel?.duration!!)
        if(classDropDownModelUpdate!= null){
            val className = classDropDownModelUpdate?.courseName + " " + sectionModelUpdate?.classroomName
            binding!!.tvSelectClassSection.text = className
        }
        if(examDropDownModelUpdate!= null){
            if(testModelUpdate!= null){
                binding!!.rbTestLevel.isChecked = true
                val examName = examDropDownModelUpdate?.examName +" "+testModelUpdate?.testName
                binding!!.tvExamName.text = examName

            }else{
                binding!!.rbExamLevel.isChecked = true
                val examName = examDropDownModelUpdate?.examName
                binding!!.tvExamName.text = examName
            }
        }
        binding!!.tvScheduleExam.text = getString(R.string.update_schedule)


    }

    private fun initViews(){
        currActivity.application?.let {
            examViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(ExamViewModel::class.java)
        }

        currActivity.application?.let {
            timeTableViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(TimeTableViewModel::class.java)
        }

        timeTableViewModel?.let {
            if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx) }) {
                it.getClassRoomDropdown()
            }
        }

        binding!!.sbStartTime.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                examTime = examTimeList[p2]
            }

        }

        val aa = MySpinnerAdapter(this, R.layout.spinner_item, examTimeList)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //Setting the ArrayAdapter data on the Spinner
        binding!!.sbStartTime.adapter = aa

        observers()
        setListner()
        binding!!.toolbar.txtToolbarTitle.text = getString(R.string.create_exam_schedule)
    }

    private fun setListner(){
        binding!!.toolbar.imgBack.setOnClickListener(this)
        binding!!.toolbar.imgMenu.visibility = View.GONE
        binding!!.toolbar.imgBack.visibility = View.VISIBLE
        binding!!.tvScheduleExam.setOnClickListener(this)
        startDate = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar!!.set(Calendar.YEAR, year)
            myCalendar!!.set(Calendar.MONTH, monthOfYear)
            myCalendar!!.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel()
        }

        binding!!.rgLevel.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.rbExamLevel) {
                openTest = false
            } else if (checkedId == R.id.rbTestLevel) {
                openTest = true
            }
        }

        binding!!.etDuration.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
               if(!p0.isNullOrEmpty()){
                   duration = p0.toString().toInt()
               }else{
                   duration = 0
               }
            }

        })
        binding!!.rlStartDate.setOnClickListener(this)
        binding!!.tvSelectClassSection.setOnClickListener(this)
        binding!!.tvExamName.setOnClickListener(this)
        binding!!.tvStartDate.setOnClickListener(this)
        binding!!.tvScheduleExam.setOnClickListener(this)

    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.imgBack ->{
                onBackPressed()
            }
            R.id.tvScheduleExam ->{
                if(validation()){
                    if(binding!!.tvScheduleExam.text.toString().equals(currActivity.resources.getString(R.string.update_schedule))){
                        updateExamSchedule()
                    }else{
                        createExamSchedule()
                    }

                }
            }
            R.id.tvStartDate ->{
                DatePickerDialog(
                    this@ScheduleExamActivity, startDate,
                    myCalendar!!.get(Calendar.YEAR), myCalendar!!.get(Calendar.MONTH),
                    myCalendar!!.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
            R.id.tvSelectClassSection -> {
                openDialog()
            }
            R.id.tvExamName ->{
                openExamDialog()
            }
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

    private fun openExamDialog() {
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
        examDropDownAdapter = ExaminationDropDownAdapter(currActivity, examDropdownList,openTest, null, object :
            ExaminationDropDownAdapter.SectionSelection {
            override fun selectionCallback(parent: Int, child: Int) {
                classDropDownAdapter.notifyDataSetChanged()
            }

        })
        dialogSelectClassSectionBinding!!.rlClassesDropdown.adapter = examDropDownAdapter
        examDropDownAdapter.notifyDataSetChanged()

        dialogSelectClassSectionBinding!!.imgCancel.setOnClickListener {
            dialog!!.dismiss()
        }

        dialog!!.setCancelable(true)
        dialog!!.show()
    }

    fun sectionClicked(data: ClassDropDownModel, model: SectionList) {
        classRoomId = model.classroomId
        classDropDownModel = data
        sectionModel = model

        val className = data.courseName + " " + model.classroomName
        binding!!.tvSelectClassSection.text = className

        examViewModel.let {
            if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx) }) {
                it!!.getExaminationDropDown(classRoomId)
            }
        }

        observers()

        dialog!!.dismiss()
    }

    fun examSelectd(data : ExaminationDropDownModel){
        examId = data.examId
        binding!!.tvExamName.text = data.examName
        examDropDownModel  = data
        dialog!!.dismiss()
    }
    fun selectTest(model :TestListModel,data : ExaminationDropDownModel){
        examId = data.examId
        testId = model.testId
        binding!!.tvExamName.text = data.examName+" "+model.testName
        examDropDownModel  = data
        dialog!!.dismiss()
    }

    private fun updateLabel() {
        val myFormat = "yyyy-MM-dd" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)

        tvStartDate.text = sdf.format(myCalendar!!.getTime())
    }

    private fun validation() : Boolean{
        var isValid = true
        if(classRoomId == 0){
            isValid = false
            AndroidUtil.showToast(currActivity,"Select Class & Section",true)
        }else if(examId == 0){
            isValid = false
            AndroidUtil.showToast(currActivity,"Please select exam name",true)
        }else if(tvStartDate.text.toString().isEmpty() || tvStartDate.text.equals(currActivity.getString(R.string.start_date))){
            isValid = false
            AndroidUtil.showToast(currActivity,"Start date is required",true)
        }else if(examTime.isEmpty()){
            isValid = false
            AndroidUtil.showToast(currActivity,"Exam start time is required",true)
        }else if(duration == 0){
            isValid = false
            AndroidUtil.showToast(currActivity,"Exam duration is required",true)
        }
        return  isValid
    }

    private fun createExamSchedule(){
        examPostModel.examId = examId
        if(testId!= 0){
            examPostModel.testId = testId
        }
        examPostModel.classroomId = classRoomId
        examPostModel.startDate = tvStartDate.text.toString()
        examPostModel.startTime = "$examTime:00"
        examPostModel.duration = duration

        examViewModel?.let {
            if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx) }) {
                it.createExamSchedule(examPostModel) }
        }
        observers()
    }
    private fun updateExamSchedule(){
        if(examId != 0){
            examPostModel.examId = examId
        }else{
            examPostModel.examId = examDropDownModelUpdate!!.examId
        }

        if(testId!= 0){
            examPostModel.testId = testId
        }else if(testModelUpdate!= null){
            examPostModel.testId = testModelUpdate!!.testId
        }
        if(classRoomId != 0){
            examPostModel.classroomId = classRoomId
        }else if(sectionModelUpdate!= null){
            examPostModel.classroomId = sectionModelUpdate!!.classroomId
        }

        examPostModel.startDate = tvStartDate.text.toString()
        examPostModel.startTime = "$examTime:00"
        examPostModel.duration = duration

        examViewModel?.let {
            if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx) }) {
                it.updateExamSchedule(scheduledModel!!.id,examPostModel) }
        }
        observers()
    }

    private fun observers(){
        examViewModel?.getErrorMutableLiveData()?.observe(this,androidx.lifecycle.Observer {
            it?.let {
                AndroidUtil.showToast(currActivity, it.message,true)
            }
        })

        examViewModel?.getSuccessLiveData()?.observe(this,androidx.lifecycle.Observer {
            AndroidUtil.showToast(currActivity,"Examination Schedule created successfully",false)
            onBackPressed()
        })
        examViewModel?.getExamDropDownLiveData()?.observe(this,androidx.lifecycle.Observer {
           examDropdownList.clear()
            examDropdownList.addAll(it)
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


    }

}
