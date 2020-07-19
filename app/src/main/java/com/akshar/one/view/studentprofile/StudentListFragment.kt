package com.akshar.one.view.studentprofile

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
import com.akshar.one.databinding.FragmentStudentListBinding
import com.akshar.one.model.ClassDropDownModel
import com.akshar.one.model.SectionList
import com.akshar.one.model.StudentListModel
import com.akshar.one.util.AndroidUtil
import com.akshar.one.view.activity.MainActivity
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.student.StudentViewModel
import com.akshar.one.viewmodels.timetable.TimeTableViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import java.util.*
import kotlin.collections.ArrayList


class StudentListFragment : Fragment(),View.OnClickListener {


    private var mainActivity: MainActivity? = null
    private var fragmentStudentListBinding: FragmentStudentListBinding? = null
    lateinit var currActivity: Activity
    private var dialogSelectClassSectionBinding: DialogSelectClassAndSectionBinding? = null
    private var dialog: Dialog? = null
    lateinit var classDropDownAdapter: ClassDropDownAdapter
    private var classDropdownList = ArrayList<ClassDropDownModel>()
    private var classRoomId: Int = 0;
    private var timeTableViewModel: TimeTableViewModel? = null
    private var studentViewModel: StudentViewModel? = null
    private var studentList = ArrayList<StudentListModel>()
    lateinit var studentadapter: StudentListAdapter
    private lateinit var classDropDownModel: ClassDropDownModel
    private lateinit var sectionModel: SectionList


    companion object {
        @JvmStatic
        fun newInstance() = StudentListFragment()
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
        fragmentStudentListBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_student_list, container, false)
        currActivity = activity!!
        setListner()
        setAdapter()
        return fragmentStudentListBinding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.application?.let {
            timeTableViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(TimeTableViewModel::class.java)
        }

        activity?.application?.let {
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

        observer()


    }

    private fun setAdapter() {
        fragmentStudentListBinding!!.rvStudents.setHasFixedSize(true)
        fragmentStudentListBinding!!.rvStudents.layoutManager =
            LinearLayoutManager(currActivity, LinearLayoutManager.VERTICAL, false)
        studentadapter =
            StudentListAdapter(currActivity, studentList)
        fragmentStudentListBinding!!.rvStudents.adapter = studentadapter
    }

    private fun setListner() {
        fragmentStudentListBinding?.rlClassSection!!.setOnClickListener(this)
        fragmentStudentListBinding?.imgCreateStudent!!.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.rlClassSection -> {
                openDialog()
            }
            R.id.imgCreateStudent -> {
                CreateStudentProfile.open(currActivity, classDropDownModel, sectionModel)
            }

        }
    }

    private fun initViews() {
        mainActivity?.setToolbarTitle(getString(R.string.student_list))
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
        classDropDownAdapter = ClassDropDownAdapter(currActivity, classDropdownList, this, object :
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
        fragmentStudentListBinding!!.tvClassSectionName.text = className
        fragmentStudentListBinding!!.imgCreateStudent.visibility = View.VISIBLE
        studentViewModel.let {
            if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                it!!.getStudentList(classRoomId)
            }
        }


        dialog!!.dismiss()
    }

    private fun observer() {
        studentViewModel?.getIsLoading()?.observe(this, Observer {
            showProgressIndicator(it)
        })

        studentViewModel?.getErrorMutableLiveData()?.observe(this, Observer {
            it.let {
                AndroidUtil.showToast(currActivity, it.message, true)
            }
        })

        timeTableViewModel?.getErrorMutableLiveData()?.observe(this, Observer {
            it.let {
                AndroidUtil.showToast(currActivity, it.message, true)
            }
        })

        timeTableViewModel?.getClassRoomLiveData()?.observe(this, Observer {
            classDropdownList.clear()
            classDropdownList.addAll(it)
        })

        studentViewModel?.getStudentListLiveData()?.observe(this, Observer {
            studentList.clear()
            studentList.addAll(it)
            if (studentList.size > 0) {
                fragmentStudentListBinding!!.tvSerialNumber.visibility = View.VISIBLE
                fragmentStudentListBinding!!.tvStudentName.visibility = View.VISIBLE
                fragmentStudentListBinding!!.rlNoDataFound.visibility = View.GONE
                fragmentStudentListBinding!!.rvStudents.visibility = View.VISIBLE
            } else {
                fragmentStudentListBinding!!.tvSerialNumber.visibility = View.GONE
                fragmentStudentListBinding!!.tvStudentName.visibility = View.GONE
                fragmentStudentListBinding!!.rlNoDataFound.visibility = View.GONE
                fragmentStudentListBinding!!.rvStudents.visibility = View.VISIBLE
            }
            studentadapter.notifyDataSetChanged()
        })
    }

    private fun showProgressIndicator(isLoading: Boolean?) {
        linProgressIndicator.visibility = if (isLoading == true) View.VISIBLE else View.GONE
    }

}
