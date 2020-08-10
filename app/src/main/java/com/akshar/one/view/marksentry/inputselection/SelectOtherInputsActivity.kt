package com.akshar.one.view.marksentry.inputselection

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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.akshar.one.R
import com.akshar.one.adapter.ClassDropDownAdapter
import com.akshar.one.adapter.ExaminationDropDownAdapter
import com.akshar.one.adapter.SkillCategoryDropDownAdapter
import com.akshar.one.app.AksharSchoolApplication
import com.akshar.one.databinding.ActivitySelectOtherInputsBinding
import com.akshar.one.databinding.DialogSelectClassAndSectionBinding
import com.akshar.one.model.*
import com.akshar.one.util.AndroidUtil
import com.akshar.one.util.AppUtils
import com.akshar.one.view.examschedule.ScheduledExamList
import com.akshar.one.view.marksentry.StudentMarksEntry
import com.akshar.one.view.marksentry.adapter.SubjectAdapter
import com.akshar.one.view.studentprofile.EditStudentProfileActivity
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.examination.ExamViewModel
import com.akshar.one.viewmodels.marksentry.MarksEntryViewModel
import com.akshar.one.viewmodels.timetable.TimeTableViewModel
import kotlinx.android.synthetic.main.main_toolbar.view.*
import java.util.*
import kotlin.collections.ArrayList

class SelectOtherInputsActivity : AppCompatActivity(),View.OnClickListener {


    private var binding : ActivitySelectOtherInputsBinding? = null
    private var currActivity : Activity = this
    private var classDropDownModel : ClassDropDownModel? = null
    private var sectionModel : SectionList? = null
    private var marksEntryViewModel : MarksEntryViewModel? = null
    private var assessmentAreas = ""
    private var subjectList = ArrayList<SubjectListModel>()
    private var skillList = ArrayList<SkillListModel>()
    private lateinit var subjectAdapter : SubjectAdapter
    private  var subjectModel : SubjectListModel? = null
    private var dialogSelectClassSectionBinding: DialogSelectClassAndSectionBinding? = null
    private var dialog: Dialog? = null
    lateinit var examDropDownAdapter: ExaminationDropDownAdapter
    private var testModel : TestListModel? =null
    private var examDropDownList = ArrayList<ExaminationDropDownModel>()
    private var classRoomId: Int = 0;
    private var examId: Int = 0;
    private var skillId: Int = 0;
    private var timeTableViewModel: TimeTableViewModel? = null
    private var examViewModel: ExamViewModel? = null
    private  var examDropDownModel: ExaminationDropDownModel? = null
    lateinit var classDropDownAdapter: ClassDropDownAdapter
    lateinit var skillDropDownAdapter: SkillCategoryDropDownAdapter
    private var classDropdownList = ArrayList<ClassDropDownModel>()
    private var testId: Int = 0;
    private var skillModel : SkillSetModel? = null

    companion object{
        fun open(currActivity : Activity,classDropDownModel : ClassDropDownModel,sectionList: SectionList){
            val intent = Intent(currActivity, SelectOtherInputsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("classDropDownModel",classDropDownModel)
            intent.putExtra("sectionList",sectionList)
            currActivity.startActivity(intent)
            currActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = DataBindingUtil.setContentView(currActivity,R.layout.activity_select_other_inputs)
        classDropDownModel = intent.getSerializableExtra("classDropDownModel") as ClassDropDownModel
        sectionModel = intent.getSerializableExtra("sectionList") as SectionList
        initViews()
    }

    private fun initViews(){
        binding!!.toolbar.imgMenu.visibility = View.GONE
        binding!!.toolbar.imgBack.visibility = View.VISIBLE
        binding!!.toolbar.txtToolbarTitle.text = getString(R.string.marks_entry)
        binding!!.rlSelectAssessment.visibility = View.GONE
        binding!!.rlAssessmentAreas.visibility = View.GONE
        binding!!.rlCategory.visibility = View.GONE
        binding!!.rlSelectCategory.visibility = View.GONE
        binding!!.rlSelectCategory.visibility = View.GONE
        binding!!.rlSkills.visibility = View.GONE
        binding!!.rlExamSelectionMain.visibility = View.VISIBLE

        val className = sectionModel!!.courseName + " " + sectionModel!!.classroomName
        binding!!.etClassName.text = className

        currActivity.application?.let {
            marksEntryViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(MarksEntryViewModel::class.java)
        }

        marksEntryViewModel?.let {
            if (AksharSchoolApplication.context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                it.getSubjectList(sectionModel!!.classroomId)
            }
        }

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
            if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx) }) {
                it.getClassRoomDropdown()
            }
        }

        examViewModel.let {
            if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx) }) {
                it!!.getExaminationDropDown(sectionModel!!.classroomId)
            }
        }

        setAdapter()
        observer()
        setListener()
    }

    private fun setListener(){
        binding!!.rlScholastic.setOnClickListener(this)
        binding!!.rlCoScholastic.setOnClickListener(this)
        binding!!.rlExamSelectionMain.setOnClickListener(this)
        binding!!.rlClassSectionMain.setOnClickListener(this)
        binding!!.rlSkills.setOnClickListener(this)
        binding!!.rlAssessmentAreas.setOnClickListener(this)
        binding!!.tvStartAssessment.setOnClickListener(this)
        binding!!.toolbar.imgBack.setOnClickListener(this)
        binding!!.rlCategory.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.rlScholastic ->{
                assessmentAreas = "scholastic"
                setAssessment()

            }
            R.id.rlCoScholastic ->{
                assessmentAreas = "coScholastic"
                setAssessment()
            }
            R.id.rlExamSelectionMain->{
                binding!!.rlAssessmentAreas.visibility = View.GONE
                binding!!.rlSelectAssessment.visibility = View.VISIBLE
                binding!!.rlCategory.visibility = View.GONE
                binding!!.rlSelectCategory.visibility = View.GONE
                binding!!.rlSkills.visibility = View.GONE
                openExamDialog()
            }
            R.id.rlClassSectionMain ->{
                openDialog()
            }
            R.id.rlSkills ->{
                openSkillPopup()
            }
            R.id.rlAssessmentAreas ->{
                binding!!.rlSelectAssessment.visibility = View.VISIBLE
                binding!!.rlAssessmentAreas.visibility = View.GONE
                binding!!.rlCategory.visibility = View.GONE
                binding!!.rlSelectCategory.visibility = View.GONE
                binding!!.rlSkills.visibility = View.GONE

            }
            R.id.tvStartAssessment ->{
               if(validation()){
                   StudentMarksEntry.open(currActivity,classDropDownModel!!,assessmentAreas,sectionModel,
                       examDropDownModel,testModel,examId,testId,subjectModel,skillModel)
               }
            }
            R.id.imgBack ->{
                onBackPressed()
            }
            R.id.rlCategory ->{
                binding!!.rlCategory.visibility = View.GONE
                binding!!.rlSelectCategory.visibility = View.VISIBLE
                binding!!.rlSkills.visibility = View.GONE
            }
        }
    }

    private fun setAssessment(){
        if(assessmentAreas.equals("scholastic",true)){
            subjectModel = null
            skillList.clear()
            binding!!.imgAssessment.setImageResource(R.drawable.scholastic)
            binding!!.tvAssessmentName.text = currActivity.resources.getString(R.string.scholastic)
            binding!!.rlSelectAssessment.visibility = View.GONE
            binding!!.rlAssessmentAreas.visibility = View.VISIBLE
            binding!!.rlCategory.visibility = View.GONE
            binding!!.rlSelectCategory.visibility = View.VISIBLE
            binding!!.rlSkills.visibility = View.GONE

        }else if(assessmentAreas.equals("coScholastic",true)) {
            subjectModel = null
            skillList.clear()
            binding!!.imgAssessment.setImageResource(R.drawable.coscholastic)
            binding!!.tvAssessmentName.text = currActivity.resources.getString(R.string.co_scholastic)
            binding!!.rlSelectAssessment.visibility = View.GONE
            binding!!.rlAssessmentAreas.visibility = View.VISIBLE
            binding!!.rlCategory.visibility = View.GONE
            binding!!.rlSelectCategory.visibility = View.GONE
            binding!!.rlSkills.visibility = View.GONE

            marksEntryViewModel?.let {
                if (AksharSchoolApplication.context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                    it.getSkillList(sectionModel!!.classroomId,0,"CoScholastic")
                }
            }
        }
    }

    private fun setAdapter(){
        binding!!.rvCategories.setHasFixedSize(true)
        binding!!.rvCategories.layoutManager = LinearLayoutManager(currActivity,LinearLayoutManager.VERTICAL,false)
        subjectAdapter = SubjectAdapter(currActivity,subjectList)
        binding!!.rvCategories.adapter = subjectAdapter
    }

    private fun observer(){
        marksEntryViewModel?.getErrorMutableLiveData()?.observe(this, Observer {
            it.let {
                AndroidUtil.showToast(currActivity, it.message, true)
            }
        })
        marksEntryViewModel?.getSkillErrorMutableLiveData()?.observe(this, Observer {
            it.let {
                if(assessmentAreas.equals("scholastic",true)){

                }else{
                    AndroidUtil.showToast(currActivity, it.message, true)
                }

            }
        })

        marksEntryViewModel?.getSubjectListLiveData()?.observe(this, Observer {
            subjectList.clear()
            subjectList.addAll(it)
            subjectAdapter.notifyDataSetChanged()
        })
        marksEntryViewModel?.getSkillListLiveData()?.observe(this, Observer {
            skillList.clear()
            skillList.addAll(it)
            if(skillList.size > 0){
                binding!!.rlSkills.visibility = View.VISIBLE
            }else{
                binding!!.rlSkills.visibility = View.GONE
            }
        })


        examViewModel?.getExamDropDownErrorData()?.observe(this, Observer {
            it.let {
                binding!!.tvExaminationErrorMessage.visibility = View.VISIBLE
                binding!!.tvStartAssessment.visibility = View.GONE
                binding!!.rlExamSelectionMain.visibility = View.GONE
                binding!!.rlAssessmentAreas.visibility = View.GONE
                binding!!.rlSelectAssessment.visibility = View.GONE
                binding!!.rlCategory.visibility = View.GONE
                binding!!.rlSelectCategory.visibility = View.GONE
                binding!!.rlSkills.visibility = View.GONE
                examDropDownList.clear()
                AndroidUtil.showToast(currActivity, it.message, true)
            }
        })

        timeTableViewModel?.getErrorMutableLiveData()?.observe(this,Observer {
            it.let {
                AndroidUtil.showToast(currActivity, it.message, true)
            }
        })

        timeTableViewModel?.getClassRoomLiveData()?.observe(this, Observer {
            classDropdownList.clear()
            classDropdownList.addAll(it)
        })

        examViewModel?.getExamDropDownLiveData()?.observe(this, Observer {
            binding!!.tvExaminationErrorMessage.visibility = View.GONE
//            binding!!.tvStartAssessment.visibility = View.VISIBLE
            binding!!.rlExamSelectionMain.visibility = View.VISIBLE
//            binding!!.rlAssessmentAreas.visibility = View.VISIBLE
//            binding!!.rlSelectAssessment.visibility = View.VISIBLE
//            binding!!.rlCategory.visibility = View.VISIBLE
//            binding!!.rlSelectCategory.visibility = View.VISIBLE
//            binding!!.rlSkills.visibility = View.VISIBLE
            examDropDownList.clear()
            examDropDownList.addAll(it)
        })
    }

    fun selectedCategory(model: SubjectListModel){
        subjectModel = model
        binding!!.rlCategory.visibility = View.VISIBLE
        binding!!.tvCategoryName.text = subjectModel?.subjectName
        binding!!.rlSelectCategory.visibility = View.GONE
        marksEntryViewModel?.let {
            if (AksharSchoolApplication.context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                it.getSkillList(sectionModel!!.classroomId,model.subjectId,"Scholastic")
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
        binding!!.etClassName.text = className

        examViewModel.let {
            if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx) }) {
                it!!.getExaminationDropDown(classRoomId)
            }
        }
        binding!!.rlSelectAssessment.visibility = View.GONE
        binding!!.rlAssessmentAreas.visibility = View.GONE
        binding!!.rlCategory.visibility = View.GONE
        binding!!.rlSelectCategory.visibility = View.GONE
        binding!!.rlSelectCategory.visibility = View.GONE
        binding!!.rlSkills.visibility = View.GONE
        binding!!.rlExamSelectionMain.visibility = View.VISIBLE
        binding!!.etExamName.text = "Select Exam Name"
        observer()
        dialog!!.dismiss()
    }


    fun examSelectd(data : ExaminationDropDownModel){
        examId = data.examId
        binding!!.etExamName.text = data.examName
        examDropDownModel  = data
        testModel = null
        examViewModel.let {
            if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx) }) {
                it!!.getExaminations(examId,0)
            }
        }
        dialog!!.dismiss()
    }
    fun selectTest(model : TestListModel, data : ExaminationDropDownModel){
        examId = data.examId
        testId = model.testId
        binding!!.etExamName.text = data.examName+" "+model.testName
        examDropDownModel  = data
        testModel = model
//        binding!!.rlAssesmentAreas.visibility = View.VISIBLE
//        binding!!.rlSelectAssessment.visibility = View.GONE
//        binding!!.rlCategory.visibility = View.GONE
//        binding!!.rlSelectCategory.visibility = View.GONE
//        binding!!.rlSkills.visibility = View.GONE

        dialog!!.dismiss()
        examViewModel.let {
            if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx) }) {
                it!!.getExaminations(0,testId)
            }
        }
    }

    fun skillClicked(subCategory : SubCategoryList, category : SkillListModel,skill : SkillSetModel){
        binding!!.tvSkillName.text = skill.skillName
        skillId = skill.skillId
        skillModel = skill

        dialog!!.dismiss()
    }

    private fun openSkillPopup(){
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
        skillDropDownAdapter = SkillCategoryDropDownAdapter(currActivity,skillList)
        dialogSelectClassSectionBinding!!.rlClassesDropdown.adapter = skillDropDownAdapter
        skillDropDownAdapter.notifyDataSetChanged()

        dialogSelectClassSectionBinding!!.imgCancel.setOnClickListener {
            dialog!!.dismiss()
        }

        dialog!!.setCancelable(true)
        dialog!!.show()
    }

    private fun validation() : Boolean{
        var isValid = true

        if(classDropDownModel== null){
            isValid = false
            AppUtils.showToast(currActivity,"Class / Section is required",true)
        }else if(examDropDownModel == null){
            isValid = false
            AppUtils.showToast(currActivity,"Select Examination / Test name",true)
        }else if(assessmentAreas == ""){
            isValid = false
            AppUtils.showToast(currActivity,"Select Assessment areas",true)
        }else if(assessmentAreas == "Scholastic" && subjectModel == null){
            isValid = false
            AppUtils.showToast(currActivity,"Select Subject name",true)
        }else if(assessmentAreas == "coScholastic" && skillModel == null){
            isValid = false
            AppUtils.showToast(currActivity,"Skill is Required",true)
        }
        return isValid
    }

}
