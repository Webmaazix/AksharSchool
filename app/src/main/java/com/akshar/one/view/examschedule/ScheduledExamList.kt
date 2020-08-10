package com.akshar.one.view.examschedule

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
import com.akshar.one.databinding.ActivityScheduledExamListBinding
import com.akshar.one.databinding.DialogSelectClassAndSectionBinding
import com.akshar.one.model.*
import com.akshar.one.swipelayout.util.Attributes
import com.akshar.one.util.AndroidUtil
import com.akshar.one.util.AppUtils
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.examination.ExamViewModel
import com.akshar.one.viewmodels.timetable.TimeTableViewModel
import kotlinx.android.synthetic.main.main_toolbar.view.*
import java.util.*
import kotlin.collections.ArrayList

class ScheduledExamList : AppCompatActivity(),View.OnClickListener {

    private var binding: ActivityScheduledExamListBinding? = null
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
    private var examinationList = ArrayList<ScheduledExamList>()
    lateinit var examAdapter: ExaminationAdapter
    private lateinit var classDropDownModel: ClassDropDownModel
    private lateinit var examDropDownModel: ExaminationDropDownModel
    lateinit var classDropDownAdapter: ClassDropDownAdapter
    private var classDropdownList = ArrayList<ClassDropDownModel>()
    private lateinit var sectionModel: SectionList
    private var testId: Int = 0;

    companion object{
        fun open(currActivity : Activity){
            val intent = Intent(currActivity, ScheduledExamList::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            currActivity.startActivity(intent)
            currActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(currActivity,R.layout.activity_scheduled_exam_list)
        initViews()
    }

    private fun initViews(){
        binding!!.toolbar.imgMenu.visibility = View.GONE
        binding!!.toolbar.imgBack.visibility = View.VISIBLE
        binding!!.toolbar.txtToolbarTitle.text = currActivity.resources.getString(R.string.exam_schedule)
        currActivity.application?.let {
            timeTableViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(TimeTableViewModel::class.java)
        }

        currActivity.application?.let {
            examViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(ExamViewModel::class.java)
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
        binding!!.rvExams.setHasFixedSize(true)
//        binding!!.rvExams.setItemAnimator(FadeInLeftAnimator())
        binding!!.rvExams.layoutManager =
            LinearLayoutManager(currActivity, LinearLayoutManager.VERTICAL, false)
        examAdapter = ExaminationAdapter(
            currActivity,
            examinationList as ArrayList<ScheduleList>
        )
//        (examAdapter as ExaminationAdapter).mode = Attributes.Mode.Single
        binding!!.rvExams.adapter = examAdapter
    }

    private fun setListner() {
        binding!!.rlClassSection!!.setOnClickListener(this)
        binding!!.rlExaminationSelection!!.setOnClickListener(this)
        binding!!.imgScheduleExam!!.setOnClickListener(this)
        binding!!.toolbar.imgBack.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.rlClassSection -> {
                openDialog()
            }
            R.id.rlExaminationSelection -> {
                openExamDialog()
            }
            R.id.imgScheduleExam ->{
                ScheduleExamActivity.open(currActivity,null,null,null,null,null)
            }
            R.id.imgBack ->{
                onBackPressed()
            }

        }
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
        examViewModel.let {
            if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx) }) {
                dialog =  AppUtils.showProgress(currActivity)
                it!!.getExaminations(examId,0)
            }
        }


        dialog!!.dismiss()
    }

    fun selectTest(model :TestListModel,data : ExaminationDropDownModel){
        examId = data.examId
        testId = model.testId
        binding!!.tvExamName.text = data.examName+" "+model.testName
        examDropDownModel  = data
        testModel = model
        dialog!!.dismiss()
        examViewModel.let {
            if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx) }) {
                dialog =  AppUtils.showProgress(currActivity)
                it!!.getExaminations(0,testId)
            }
        }


    }


    private fun observer() {
        examViewModel?.getErrorMutableLiveData()?.observe(this, androidx.lifecycle.Observer {
            it.let {
                AppUtils.hideProgress(dialog!!)
                examinationList.clear()
                examAdapter.notifyDataSetChanged()
                binding!!.rlNoDataFound.visibility = View.VISIBLE
                binding!!.rvExams.visibility = View.GONE
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

        examViewModel?.getExamLiveData()?.observe(this, androidx.lifecycle.Observer {
            AppUtils.hideProgress(dialog!!)
            examinationList.clear()
            examinationList.addAll(it.schedule as ArrayList<ScheduledExamList>)
            examAdapter.classDropDownModel = classDropDownModel
            examAdapter.sectionModel = sectionModel
            examAdapter.examDropDownModel = examDropDownModel
            examAdapter.testModel = testModel
            if(examinationList.size> 0){
                binding!!.rlNoDataFound.visibility = View.GONE
                binding!!.rvExams.visibility = View.VISIBLE
            }else{
                binding!!.rlNoDataFound.visibility = View.VISIBLE
                binding!!.rvExams.visibility = View.GONE
            }
            examAdapter.notifyDataSetChanged()
        })
    }

    private fun showProgressIndicator(isLoading: Boolean?) {
       // linProgressIndicator.visibility = if (isLoading == true) View.VISIBLE else View.GONE
    }

}
