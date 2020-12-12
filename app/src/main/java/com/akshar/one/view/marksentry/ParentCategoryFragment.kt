package com.akshar.one.view.marksentry


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
import com.akshar.one.adapter.ExaminationDropDownAdapter
import com.akshar.one.databinding.DialogSelectClassAndSectionBinding
import com.akshar.one.view.noticeboard.adapter.MyNoticeBoardAdapter
import com.akshar.one.databinding.FragmentActiveNoticeBinding
import com.akshar.one.databinding.FragmentParentCategoryBinding
import com.akshar.one.manager.SessionManager
import com.akshar.one.model.*
import com.akshar.one.util.AndroidUtil
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.noticeboard.NoticeBoardViewModel
import com.akshar.one.swipelayout.util.Attributes
import com.akshar.one.util.AppUtils
import com.akshar.one.view.examschedule.ScheduledExamList
import com.akshar.one.view.examschedule.adapter.ExaminationAdapter
import com.akshar.one.view.feeandpayments.StudentListForFeesFragment
import com.akshar.one.view.marksentry.adapter.ParentMarksCategoryAdapter
import com.akshar.one.view.marksentry.adapter.SubjectMarksParentAdapter
import com.akshar.one.viewmodels.examination.ExamViewModel
import com.akshar.one.viewmodels.marksentry.MarksEntryViewModel
import com.akshar.one.viewmodels.timetable.TimeTableViewModel
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class ParentCategoryFragment : Fragment() ,View.OnClickListener {


    private var currActivity : Activity? = null
    private var binding : FragmentParentCategoryBinding? = null
    private var marksList = MarksCategoryList()
    private var marksViewModel: MarksEntryViewModel? = null
    lateinit var adapter : ParentMarksCategoryAdapter
    private var dialog : Dialog? = null
    private var mdialog : Dialog? = null
    var securityList = ArrayList<String>()
    lateinit var examDropDownAdapter: ExaminationDropDownAdapter
    private var examDropDownList = ArrayList<ExaminationDropDownModel>()
    private var examId: Int = 0;
    private var studentProfileId: Int = 0;
    private var examViewModel: ExamViewModel? = null
    private  var examDropDownModel: ExaminationDropDownModel? = null
    private var dialogSelectClassSectionBinding: DialogSelectClassAndSectionBinding? = null
    private var selectedRole : AppList? = null
    private lateinit var testHeadingAdapter : SubjectMarksParentAdapter
    private lateinit var categoryAdapter : ParentMarksCategoryAdapter
    private var testHeadingList = ArrayList<MarksTestListParent>()
    private var scholasticMarksList = ArrayList<ScholasticMarksList>()
    private var subjectList = ArrayList<SubjectListParent>()
    private var currentTest = MarksTestListParent()
    var Tid = 0




    companion object {
        @JvmStatic
        fun newInstance() = ParentCategoryFragment()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        currActivity = activity
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_parent_category,container,false)
        return  binding?.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
         studentProfileId = SessionManager.getLoginRole()!!.userUniqueId
        selectedRole = SessionManager.getLoginRole()
        activity?.application?.let {
            marksViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(MarksEntryViewModel::class.java)
        }

        currActivity!!.application?.let {
            examViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(ExamViewModel::class.java)
        }


        examViewModel.let {
            if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx!!) }) {
                it!!.getExaminationDropDown(selectedRole!!.classRoomId)
            }
        }


        initViews()
        setStudentData()
        observers()
    }

    private fun setStudentData(){
        binding!!.tvStudentName.text = selectedRole!!.studentName
        binding!!.tvClassSectionName.text = selectedRole!!.className

        if(selectedRole!!.url!= null && selectedRole!!.url!=""){
            binding!!.flLayout.visibility = View.GONE
            binding!!.imgUserProfile.visibility = View.VISIBLE
            AppUtils.loadImageCrop(
                selectedRole!!.url,
                binding!!.imgUserProfile,
                R.drawable.circle_default_pic,
                80,
                80
            )
        }else {
            binding!!.flLayout.visibility = View.VISIBLE
            binding!!.imgUserProfile.visibility = View.GONE

            binding!!.tvShortName.setText(
                selectedRole!!.studentName.substring(0, 2).toUpperCase()
            )


        }

    }



    private fun initViews(){
       // setAdapter()
        setListner()
    }

//    private fun setAdapter(){
//        binding!!.rvCategory.setHasFixedSize(true)
//        binding!!.rvCategory.layoutManager = LinearLayoutManager(currActivity,
//            LinearLayoutManager.VERTICAL,false)
//        adapter = ParentMarksCategoryAdapter(currActivity!!, marksList)
//        binding!!.rvCategory.adapter = adapter
//    }

    private fun observers(){

        examViewModel?.getExamDropDownErrorData()?.observe(this, androidx.lifecycle.Observer {
            it.let {
//                AppUtils.hideProgress(dialog!!)
                examDropDownList.clear()
                AndroidUtil.showToast(currActivity, it.message, true)
            }
        })


        examViewModel?.getExamDropDownLiveData()?.observe(this, Observer {
            examDropDownList.clear()
            examDropDownList.addAll(it)
        })


        marksViewModel?.getErrorMutableLiveData()?.observe(this, Observer {
            it?.let {
//                AppUtils.hideProgress(mdialog!!)
                AndroidUtil.showToast(context!!, it.message,true)
            }
        })

        marksViewModel?.getStudentMarksCategoryLiveData()?.observe(this, Observer {
//            AppUtils.hideProgress(mdialog!!)
           // marksList.clear()
//            marksList.addAll(it)
//            if(marksList.size> 0){
////                binding!!.rlNoDataFound.visibility = View.GONE
//                binding!!.rvCategory.visibility = View.VISIBLE
//            }else{
////                binding!!.rlNoDataFound.visibility = View.VISIBLE
//                binding!!.rvCategory.visibility = View.GONE
//            }

          //  adapter.notifyDataSetChanged()
            // dashboardViewModel?.setTimeTableAdapter(it)
        })


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
        dialogSelectClassSectionBinding!!.tvTitle.text = resources.getString(R.string.select_exam_amp_test_name)
        dialogSelectClassSectionBinding!!.rlClassesDropdown.setHasFixedSize(true)
        dialogSelectClassSectionBinding!!.rlClassesDropdown.layoutManager = LinearLayoutManager(
            currActivity,
            LinearLayoutManager.VERTICAL, false
        )

        examDropDownAdapter = ExaminationDropDownAdapter(currActivity!!, examDropDownList, false,this, object :
            ExaminationDropDownAdapter.SectionSelection {
            override fun selectionCallback(parent: Int, child: Int) {
                examDropDownAdapter.notifyDataSetChanged()
            }

        })

        if(examDropDownList.size > 0){
            dialogSelectClassSectionBinding!!.rlNotFound.visibility = View.GONE
            dialogSelectClassSectionBinding!!.rlClassesDropdown.visibility = View.VISIBLE
        }else{
            dialogSelectClassSectionBinding!!.rlNotFound.visibility = View.VISIBLE
            dialogSelectClassSectionBinding!!.rlClassesDropdown.visibility = View.GONE
        }

        dialogSelectClassSectionBinding!!.rlClassesDropdown.adapter = examDropDownAdapter
        examDropDownAdapter.notifyDataSetChanged()

        dialogSelectClassSectionBinding!!.imgCancel.setOnClickListener {
            dialog!!.dismiss()
        }

        dialog!!.setCancelable(true)
        dialog!!.show()

    }

    fun examSelectd(data : ExaminationDropDownModel){
        examId = data.examId
        binding!!.tvExamName.text = data.examName
        examDropDownModel  = data
        dialog!!.dismiss()
        marksViewModel?.let {
            if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
//                mdialog =  AppUtils.showProgress(currActivity!!)
                it.getStudentMarksByProfileId(examId, studentProfileId) }
        }

    }

    private fun setListner(){
        binding!!.rlExaminationSelection.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.rlExaminationSelection ->{
                openExamDialog()
            }
        }
    }
}
