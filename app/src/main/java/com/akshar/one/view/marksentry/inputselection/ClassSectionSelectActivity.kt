package com.akshar.one.view.marksentry.inputselection

import android.app.Activity
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
import com.akshar.one.adapter.ClassDropDownAdapter
import com.akshar.one.app.AksharSchoolApplication.Companion.context
import com.akshar.one.databinding.ActivityClassSectionSelectBinding
import com.akshar.one.model.ClassDropDownModel
import com.akshar.one.model.SectionList
import com.akshar.one.util.AndroidUtil
import com.akshar.one.view.studentprofile.EditStudentProfileActivity
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.timetable.TimeTableViewModel
import kotlinx.android.synthetic.main.main_toolbar.view.*

class ClassSectionSelectActivity : AppCompatActivity(),View.OnClickListener{


    private var binding : ActivityClassSectionSelectBinding? = null
    private var currActivity : Activity = this
    lateinit var classDropDownAdapter: ClassDropDownAdapter
    private var classDropdownList = ArrayList<ClassDropDownModel>()
    private var classRoomId: Int = 0;
    private lateinit var classDropDownModel: ClassDropDownModel
    private lateinit var sectionModel: SectionList
    private var timeTableViewModel: TimeTableViewModel? = null

    companion object{
        fun open(currActivity : Activity){
            val intent = Intent(currActivity, ClassSectionSelectActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            currActivity.startActivity(intent)
            currActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(currActivity,R.layout.activity_class_section_select)
        setAdapter()
        initViews()
        setListner()
    }

    private fun setListner(){
        binding!!.toolbar.imgBack.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.imgBack ->{
                onBackPressed()
            }
        }
    }

    private fun setAdapter(){
        binding!!.rlClassesDropdown.setHasFixedSize(true)
        binding!!.rlClassesDropdown.layoutManager = LinearLayoutManager(
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
        binding!!.rlClassesDropdown.adapter = classDropDownAdapter
        classDropDownAdapter.notifyDataSetChanged()

    }

    private fun initViews(){
        binding!!.toolbar.imgMenu.visibility = View.GONE
        binding!!.toolbar.imgBack.visibility = View.VISIBLE
        binding!!.toolbar.txtToolbarTitle.text = getString(R.string.marks_entry)
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

        observer()
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
            classDropDownAdapter.notifyDataSetChanged()
        })

    }

    fun sectionClicked(data : ClassDropDownModel, model : SectionList) {
        classRoomId = model.classroomId
        classDropDownModel = data
        sectionModel = model

        val className = data.courseName + " " + model.classroomName
        SelectOtherInputsActivity.open(currActivity,classDropDownModel,sectionModel)

    }



}
