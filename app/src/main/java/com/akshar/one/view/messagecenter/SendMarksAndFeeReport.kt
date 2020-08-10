package com.akshar.one.view.messagecenter

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
import com.akshar.one.R
import com.akshar.one.adapter.ClassDropDownAdapter
import com.akshar.one.view.examschedule.adapter.ExaminationAdapter
import com.akshar.one.adapter.ExaminationDropDownAdapter
import com.akshar.one.app.AksharSchoolApplication
import com.akshar.one.databinding.ActivityScheduledExamListBinding
import com.akshar.one.databinding.ActivitySendMarksAndFeeReportBinding
import com.akshar.one.databinding.DialogSelectClassAndSectionBinding
import com.akshar.one.model.*
import com.akshar.one.swipelayout.util.Attributes
import com.akshar.one.util.AndroidUtil
import com.akshar.one.util.AppUtils
import com.akshar.one.view.examschedule.ScheduledExamList
import com.akshar.one.view.marksentry.adapter.StudentMarksAdapter
import com.akshar.one.view.messagecenter.adapter.StudentListAdapter
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.examination.ExamViewModel
import com.akshar.one.viewmodels.messagecenter.MessageCenterViewModel
import com.akshar.one.viewmodels.student.StudentViewModel
import com.akshar.one.viewmodels.timetable.TimeTableViewModel
import kotlinx.android.synthetic.main.activity_send_notification_message.*
import kotlinx.android.synthetic.main.main_toolbar.view.*
import java.util.*
import kotlin.collections.ArrayList

class SendMarksAndFeeReport : AppCompatActivity(),View.OnClickListener {

    private var binding: ActivitySendMarksAndFeeReportBinding? = null
    private var currActivity: Activity = this
    private var dialogSelectClassSectionBinding: DialogSelectClassAndSectionBinding? = null
    private var dialog: Dialog? = null
    lateinit var examDropDownAdapter: ExaminationDropDownAdapter
    private var testModel : TestListModel? =null
    private var examDropDownList = ArrayList<ExaminationDropDownModel>()
    private var classRoomId: Int = 0;
    private var examId: Int = 0;
    private var timeTableViewModel: TimeTableViewModel? = null
    private var examViewModel: ExamViewModel? = null
    private var messageViewModel: MessageCenterViewModel? = null
    private var studentList = ArrayList<StudentListModel>()
    lateinit var studentAdapter: StudentListAdapter
    private lateinit var classDropDownModel: ClassDropDownModel
    private lateinit var examDropDownModel: ExaminationDropDownModel
    lateinit var classDropDownAdapter: ClassDropDownAdapter
    private var classDropdownList = ArrayList<ClassDropDownModel>()
    private  var sectionModel: SectionList? = null
    private var testId: Int = 0;
    private var studentViewModel: StudentViewModel? = null

    companion object{
        fun open(currActivity : Activity){
            val intent = Intent(currActivity, SendMarksAndFeeReport::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            currActivity.startActivity(intent)
            currActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(currActivity,R.layout.activity_send_marks_and_fee_report)
        initViews()
    }

    private fun initViews(){
        binding!!.toolbar.imgMenu.visibility = View.GONE
        binding!!.toolbar.imgBack.visibility = View.VISIBLE
        binding!!.toolbar.txtToolbarTitle.text = currActivity.resources.getString(R.string.send_report)
        currActivity.application?.let {
            timeTableViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(TimeTableViewModel::class.java)
        }

        currActivity.application?.let {
            messageViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(MessageCenterViewModel::class.java)
        }
        currActivity.application?.let {
            examViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(ExamViewModel::class.java)
        }

        currActivity.application?.let {
            studentViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(StudentViewModel::class.java)
        }

        timeTableViewModel?.let {
            if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                it.getClassRoomDropdown()
            }
        }
        setAdapter()
        setListner()
        observer()

    }

    private fun setAdapter() {
        binding!!.rvStudents.setHasFixedSize(true)
//        binding!!.rvExams.setItemAnimator(FadeInLeftAnimator())
        binding!!.rvStudents.layoutManager =
            LinearLayoutManager(currActivity, LinearLayoutManager.VERTICAL, false)
        studentAdapter = StudentListAdapter(
            currActivity,
            studentList
        )
//        (examAdapter as ExaminationAdapter).mode = Attributes.Mode.Single
        binding!!.rvStudents.adapter = studentAdapter
    }

    private fun setListner() {
        binding!!.rlClassSection!!.setOnClickListener(this)
        binding!!.rlExaminationSelection!!.setOnClickListener(this)
        binding!!.toolbar.imgBack.setOnClickListener(this)
        binding!!.tvSendReport.setOnClickListener(this)

        binding!!.cbSelectAllStudents.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                    if (studentList.size > 0) {
                        for (model in studentList) {
                            model.isSelected = true
                        }
                        studentAdapter.notifyDataSetChanged()
                    }
            } else {
                    if (studentList.size > 0) {
                        for (model in studentList) {
                            model.isSelected = false
                        }
                        studentAdapter.notifyDataSetChanged()
                    }


            }

        }

    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.rlClassSection -> {
                openDialog()
            }
            R.id.rlExaminationSelection -> {
                openExamDialog()
            }
            R.id.imgBack ->{
                onBackPressed()
            }
            R.id.tvSendReport ->{
                if(validation()){
                    messageViewModel.let {
                        if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx) }) {
                            val model = SendGeneralNotification()
                            val classRoomList = ArrayList<Int>()
                            val selectedStudentProfileId = ArrayList<Int>()
                            classRoomList.add(sectionModel!!.classroomId)
                            model.classroomIdList = classRoomList

                            for(data in studentList){
                                if(data.isSelected){
                                    selectedStudentProfileId.add(data.studentProfileId!!)
                                }
                            }
                            model.studentProfileIdList = selectedStudentProfileId
                            model.examinationId = examId
                            if(testId != 0){
                                model.testId = testId
                            }

                            model.type = "SMS"
                            it!!.sendGeneralNotification(model)
                        }
                    }

                }
            }

        }
    }

    private fun validation() : Boolean{
        var isValid = true
        if(sectionModel == null){
            isValid = false
            AppUtils.showToast(currActivity,"Please select class and section",true)

        }else if(examId == 0){
            isValid = false
            AppUtils.showToast(currActivity,"Please select Examination or test",true)
        }

        return isValid
    }
    private fun openExamDialog(){
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
        examDropDownAdapter = ExaminationDropDownAdapter(currActivity, examDropDownList, true,null, object :
            ExaminationDropDownAdapter.SectionSelection {
            override fun selectionCallback(parent: Int, child: Int) {
                examDropDownAdapter.notifyDataSetChanged()
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

    fun sectionClicked(data: ClassDropDownModel, model: SectionList) {
        classRoomId = model.classroomId
        classDropDownModel = data
        sectionModel = model

        val className = data.courseName + " " + model.classroomName
        binding!!.tvClassSectionName.text = className

        studentViewModel.let {
            if (AksharSchoolApplication.context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                it!!.getStudentList(model.classroomId)
            }
        }
        binding!!.tvSerialNumber.visibility = View.GONE
        binding!!.cbSelectAllStudents.visibility = View.GONE

        examViewModel.let {
            if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx) }) {
                it!!.getExaminationDropDown(classRoomId)
            }
        }
        observer()
        dialog!!.dismiss()
    }


    fun examSelectd(data : ExaminationDropDownModel){
        examId = data.examId
        binding!!.tvExamName.text = data.examName
        examDropDownModel  = data
        testModel = null
//        messageViewModel.let {
//            if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx) }) {
//                dialog =  AppUtils.showProgress(currActivity)
//                it!!.getMarksStudentList()
//            }
//        }
        dialog!!.dismiss()
    }

    fun selectTest(model :TestListModel,data : ExaminationDropDownModel){
        examId = data.examId
        testId = model.testId
        binding!!.tvExamName.text = data.examName+" "+model.testName
        examDropDownModel  = data
        testModel = model
        dialog!!.dismiss()
//        messageViewModel.let {
//            if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx) }) {
//                dialog =  AppUtils.showProgress(currActivity)
//                it!!.getMarksStudentList()
//            }
//        }


    }


    private fun observer() {
        messageViewModel?.getErrorMutableLiveData()?.observe(this, androidx.lifecycle.Observer {
            it.let {
              AppUtils.hideProgress(dialog!!)
//                studentList.clear()
//                studentAdapter.notifyDataSetChanged()
//                binding!!.rlNoDataFound.visibility = View.VISIBLE
//                binding!!.rvStudents.visibility = View.GONE
                AndroidUtil.showToast(currActivity, it.message, true)
            }
        })

        examViewModel?.getExamDropDownErrorData()?.observe(this, androidx.lifecycle.Observer {
            it.let {
                AppUtils.hideProgress(dialog!!)
                examDropDownList.clear()
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

        examViewModel?.getExamDropDownLiveData()?.observe(this, androidx.lifecycle.Observer {
            examDropDownList.clear()
            examDropDownList.addAll(it)
        })

        studentViewModel?.getErrorMutableLiveData()?.observe(this, androidx.lifecycle.Observer {
            it.let {
                AppUtils.hideProgress(dialog!!)
                studentList.clear()
                studentAdapter.notifyDataSetChanged()
                binding!!.rlNoDataFound.visibility = View.VISIBLE
                binding!!.rvStudents.visibility = View.GONE
                AndroidUtil.showToast(currActivity, it.message, true)
            }
        })


        studentViewModel?.getStudentListLiveData()?.observe(this, androidx.lifecycle.Observer {
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
            studentAdapter.notifyDataSetChanged()
        })


    }


}
