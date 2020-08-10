package com.akshar.one.view.marksentry

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.akshar.one.R
import com.akshar.one.app.AksharSchoolApplication
import com.akshar.one.databinding.ActivityStudentMarksEntryBinding
import com.akshar.one.model.*
import com.akshar.one.util.AndroidUtil
import com.akshar.one.util.AppUtils
import com.akshar.one.view.marksentry.adapter.StudentMarksAdapter
import com.akshar.one.view.marksentry.inputselection.SelectOtherInputsActivity
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.marksentry.MarksEntryViewModel
import kotlinx.android.synthetic.main.main_toolbar.view.*

class StudentMarksEntry : AppCompatActivity(),View.OnClickListener {


    private var currActivity : Activity = this
    private var binding : ActivityStudentMarksEntryBinding? = null
    private var sectionModel : SectionList? = null
    private var classDropDownModel : ClassDropDownModel? = null
    private var examinationDropDownModel : ExaminationDropDownModel? = null
    private var testListModel : TestListModel? = null
    private var examId = 0
    private var testId = 0
    private var skillId = 0
    private var subjectModel : SubjectListModel? = null
    private var marksEntryViewModel : MarksEntryViewModel? = null
    private var studentMarksList = ArrayList<StundentsMarksList>()
    private var submitList : ArrayList<StundentsMarksList>? = null
    private lateinit var adapter : StudentMarksAdapter
    private var assessmentAreas = ""
    private var skillSetModel : SkillSetModel? = null
    private var isUpdate : Boolean = false
    private var dialog: Dialog? = null

    companion object{
        fun open(currActivity : Activity,classDropDownModel: ClassDropDownModel,
                 assessmentAreas : String,sectionModel : SectionList?,
                 examinationDropDownModel: ExaminationDropDownModel?,
                 testListModel: TestListModel?,
                 examId : Int,
                 testId : Int,subjectModel : SubjectListModel?,skillSetModel : SkillSetModel?){
            val intent = Intent(currActivity, StudentMarksEntry::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("sectionModel",sectionModel)
            intent.putExtra("assessmentAreas",assessmentAreas)
            intent.putExtra("classDropDownModel",classDropDownModel)
            intent.putExtra("examId",examId)
            intent.putExtra("testId",testId)
            intent.putExtra("subjectModel",subjectModel)
            intent.putExtra("skillSetModel",skillSetModel)
            intent.putExtra("examinationDropDownModel",examinationDropDownModel)
            intent.putExtra("testListModel",testListModel)
            currActivity.startActivity(intent)
            currActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(currActivity,R.layout.activity_student_marks_entry)
        sectionModel = intent.getSerializableExtra("sectionModel") as SectionList?
        classDropDownModel = intent.getSerializableExtra("classDropDownModel") as ClassDropDownModel?
        subjectModel = intent.getSerializableExtra("subjectModel") as SubjectListModel?
        skillSetModel = intent.getSerializableExtra("skillSetModel") as SkillSetModel?
        examinationDropDownModel = intent.getSerializableExtra("examinationDropDownModel") as ExaminationDropDownModel?
        testListModel = intent.getSerializableExtra("testListModel") as TestListModel?
        assessmentAreas = intent.getStringExtra("assessmentAreas")!!
        examId = intent.getIntExtra("examId",0)
        testId = intent.getIntExtra("testId",0)

        initViews()
    }

    private fun initViews(){
        binding!!.toolbar.imgMenu.visibility = View.GONE
        binding!!.toolbar.imgBack.visibility = View.VISIBLE
        binding!!.toolbar.txtToolbarTitle.text = getString(R.string.marks_entry)

        val className = classDropDownModel!!.courseName + " " + sectionModel!!.classroomName
        binding!!.etClassName.text = className

        if(testListModel != null){
            val examName  =  examinationDropDownModel!!.examName+" "+testListModel!!.testName
            binding!!.tvExamName.text = examName
        }else{
            val examName  =  examinationDropDownModel!!.examName
            binding!!.tvExamName.text = examName
        }

        if(assessmentAreas.equals("scholastic",true)){
            binding!!.imgAssessment.setImageResource(R.drawable.scholastic)
            binding!!.tvAssessmentName.text = currActivity.resources.getString(R.string.scholastic)

        }else if(assessmentAreas.equals("coScholastic",true)) {
            binding!!.imgAssessment.setImageResource(R.drawable.coscholastic)
            binding!!.tvAssessmentName.text = currActivity.resources.getString(R.string.co_scholastic)

        }

        if(subjectModel!= null){
            binding!!.rlSubject.visibility = View.VISIBLE
            binding!!.tvSubjectName.text = subjectModel!!.subjectName
        }else {
            binding!!.rlSubject.visibility = View.GONE
        }

        if(skillSetModel!= null){
            binding!!.rlSkill.visibility = View.VISIBLE
            binding!!.tvSkillName.text = skillSetModel!!.skillName
        }else{
            binding!!.rlSkill.visibility = View.GONE
        }

        currActivity.application?.let {
            marksEntryViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(MarksEntryViewModel::class.java)
        }

        marksEntryViewModel?.let {
            if (AksharSchoolApplication.context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                if(skillSetModel != null){
                    showProgressBar()
                    it.getStudentMarks(sectionModel!!.classroomId,examId,testId,subjectModel?.subjectId,skillSetModel?.skillId)
                }else{
                    showProgressBar()
                    it.getStudentMarks(sectionModel!!.classroomId,examId,testId,subjectModel?.subjectId,skillSetModel?.skillId)
                }

            }
        }
        setListner()
        setAdapter()
        observer()
    }

    private fun setListner(){
        binding!!.tvSaveOrUpdate.setOnClickListener(this)
        binding!!.toolbar.imgBack.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.tvSaveOrUpdate ->{
                if(submitList == null){
                    AppUtils.showToast(currActivity,"Please assign numbers to all students",true)
                }else{
                    marksEntryViewModel?.let {
                        if (AksharSchoolApplication.context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                            showProgressBar()
                                it.addOrUpdateStudentMarks(submitList!!)


                        }
                    }

                }
            }
            R.id.imgBack ->{
                onBackPressed()
            }
        }
    }

    private fun setAdapter(){
        binding!!.rvStudents.setHasFixedSize(true)
        binding!!.rvStudents.layoutManager = LinearLayoutManager(currActivity,LinearLayoutManager.VERTICAL,false)
        adapter = StudentMarksAdapter(currActivity,studentMarksList,isUpdate)
        binding!!.rvStudents.adapter = adapter
    }

    private fun observer(){
        marksEntryViewModel?.getErrorMutableLiveData()?.observe(this, Observer {
            hideProgressBar()
            it.let {
                AndroidUtil.showToast(currActivity, it.message, true)
            }
        })

        marksEntryViewModel?.getStudentListLiveData()?.observe(this, Observer {
            hideProgressBar()
            studentMarksList.clear()
            studentMarksList.addAll(it)
            if(studentMarksList.size > 0){
                binding!!.tvMaximumMarks.text = studentMarksList[0].maxMarks
                if(studentMarksList[0].marksScored.isNullOrEmpty()){
                    isUpdate = false
                    binding!!.tvSaveOrUpdate.text = currActivity.resources.getString(R.string.save)
                }else {
                    isUpdate = true
                    binding!!.tvSaveOrUpdate.text = currActivity.resources.getString(R.string.edit)
                    binding!!.toolbar.txtToolbarTitle.text = getString(R.string.edit_marks)
                }
            }
            adapter.notifyDataSetChanged()
        })

        marksEntryViewModel?.getStudentMarksEntryLiveData()?.observe(this, Observer {
            hideProgressBar()
           if(it != null){
               AppUtils.showToast(currActivity,"Maks added successfully",false)
               onBackPressed()
           }
        })

    }

    fun saveOrUpdateList(list : ArrayList<StundentsMarksList>,isUpdate : Boolean){
        submitList = list
        this.isUpdate = isUpdate
    }

    fun showProgressBar(){
        dialog =  AppUtils.showProgress(currActivity)
    }

    fun hideProgressBar(){
        AppUtils.hideProgress(dialog!!)
    }

}
