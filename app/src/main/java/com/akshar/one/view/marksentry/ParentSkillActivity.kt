package com.akshar.one.view.marksentry

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
import com.akshar.one.app.AksharSchoolApplication
import com.akshar.one.databinding.ActivityParentMarksBinding
import com.akshar.one.databinding.ActivityParentSkillBinding
import com.akshar.one.manager.SessionManager
import com.akshar.one.model.*
import com.akshar.one.util.AndroidUtil
import com.akshar.one.util.AppUtils
import com.akshar.one.view.marksentry.adapter.ParentMarksCategoryAdapter
import com.akshar.one.view.marksentry.adapter.SkillParentAdapter
import com.akshar.one.view.marksentry.adapter.SubCategoryParentAdapter
import com.akshar.one.view.marksentry.adapter.SubjectMarksParentAdapter
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.marksentry.MarksEntryViewModel
import kotlinx.android.synthetic.main.main_toolbar.view.*

class ParentSkillActivity : AppCompatActivity(),View.OnClickListener {

    private var binding : ActivityParentSkillBinding? = null
    private var currActivity : Activity = this
    private lateinit var testHeadingAdapter : SubjectMarksParentAdapter
    private var scholasticMarksList = ScholasticMarksList()
    private lateinit var subCategoryAdapter : SubCategoryParentAdapter
    private lateinit var skillParentAdapter : SkillParentAdapter
    private var skillList = ArrayList<SkillListParent>()
    private var subCategoryParentList = ArrayList<SubCategoryListParent>()
    private var list = ArrayList<ScholasticMarksList>()
    private var selectedRole : AppList? = null
    private var Tid = 0




    companion object{
        fun open(currActivity : Activity,model : ScholasticMarksList,list : ArrayList<ScholasticMarksList>){
            val intent = Intent(currActivity, ParentSkillActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("model",model)
            intent.putExtra("list",list)
            currActivity.startActivity(intent)
            currActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(currActivity,R.layout.activity_parent_skill)
        selectedRole = SessionManager.getLoginRole()
//        scholasticMarksList = intent.getSerializableExtra("model") as ScholasticMarksList
        list = intent.getSerializableExtra("list") as ArrayList<ScholasticMarksList>
        setData()

    }


    private fun setListner(){
        binding!!.toolbar.imgBack.setOnClickListener(this)

    }

    private fun setData(){


        if(Tid <  list.size){
            scholasticMarksList = list[Tid]
            binding!!.tvTestName.text = scholasticMarksList.categorySkillList.get(0).categoryName

            binding!!.imgPrev.setOnClickListener {
                Tid--
                setData()
            }
            binding!!.imgNext.setOnClickListener {
                Tid++
                setData()
            }

        }
        setAdapter()
        initViews()
        setListner()
        setStudentData()
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.imgBack ->{
                onBackPressed()
            }
        }
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

    private fun setAdapter(){
        if(scholasticMarksList.categorySkillList.get(0).subCategorySkillList != null){
            subCategoryParentList.clear()
            subCategoryParentList.addAll(scholasticMarksList.categorySkillList.get(0).subCategorySkillList)
            binding!!.rvSubjectMarks.setHasFixedSize(true)
            binding!!.rvSubjectMarks.layoutManager = LinearLayoutManager(
                currActivity,
                LinearLayoutManager.HORIZONTAL, false
            )
            subCategoryAdapter = SubCategoryParentAdapter(currActivity, subCategoryParentList)
            binding!!.rvSubjectMarks.adapter = subCategoryAdapter
            subCategoryAdapter.notifyDataSetChanged()
        }else{
            binding!!.rvSubjectMarks.setHasFixedSize(true)
            binding!!.rvSubjectMarks.layoutManager = LinearLayoutManager(
                currActivity,
                LinearLayoutManager.HORIZONTAL, false
            )
            skillParentAdapter = SkillParentAdapter(currActivity, scholasticMarksList.categorySkillList.get(0).skillList)
            binding!!.rvSubjectMarks.adapter = skillParentAdapter
            skillParentAdapter.notifyDataSetChanged()
        }

    }

    private fun initViews(){
        binding!!.toolbar.imgMenu.visibility = View.GONE
        binding!!.toolbar.imgBack.visibility = View.VISIBLE
        binding!!.toolbar.txtToolbarTitle.text = getString(R.string.marks_entry)
    }


}
