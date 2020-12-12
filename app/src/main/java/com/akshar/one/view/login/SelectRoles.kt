package com.akshar.one.view.login

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.akshar.one.R
import com.akshar.one.databinding.ActivitySelectRolesBinding
import com.akshar.one.manager.SessionManager
import com.akshar.one.model.AppList
import com.akshar.one.view.activity.MainActivity
import com.akshar.one.view.login.adapter.RoleAdapter
import com.akshar.one.view.login.adapter.StudentAdapter
import kotlinx.android.synthetic.main.main_toolbar.view.*

class SelectRoles : AppCompatActivity(), View.OnClickListener {
    private var binding : ActivitySelectRolesBinding? = null
    private var currActivity = this
    private lateinit var adapter : RoleAdapter
    private lateinit var studentAdapter : StudentAdapter
    private var list = ArrayList<AppList>()
    private var schoolList = ArrayList<AppList>()
    private var studentList = ArrayList<AppList>()
    private var selectedRole : AppList? = null

    companion object{

        fun open(currActivity: Activity) {
            val intent = Intent(currActivity, SelectRoles::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            currActivity.startActivity(intent)
            currActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_select_roles)
        initViews()
    }

    private fun initViews(){
        list = SessionManager.getLoginModel()!!.appsList as ArrayList<AppList>
        selectedRole = SessionManager.getLoginRole()
        for(model in list){
            if(model.appName.equals("Spectrum")){
                schoolList.add(model)
            }else if(model.appName.equals("SmartParent")){
                studentList.add(model)
            }
        }
        if(studentList.size > 0){
            binding!!.toolbar.txtToolbarTitle.text = getString(R.string.select_school_or_student)
        }else{
            binding!!.toolbar.txtToolbarTitle.text = getString(R.string.select_school)
        }

        binding!!.toolbar.imgBack.visibility = View.VISIBLE
        binding!!.toolbar.imgMenu.visibility = View.GONE
        binding!!.toolbar.imgNotification.visibility = View.GONE
        setAdapter()
        setListner()

    }

    private fun setAdapter(){
        binding!!.rvRoleList.setHasFixedSize(true)
        binding!!.rvRoleList.layoutManager = LinearLayoutManager(currActivity,LinearLayoutManager.VERTICAL,false)
        adapter = RoleAdapter(currActivity,schoolList,selectedRole)
        binding!!.rvRoleList.adapter = adapter

        binding!!.rvChildList.setHasFixedSize(true)
        binding!!.rvChildList.layoutManager = LinearLayoutManager(currActivity,LinearLayoutManager.HORIZONTAL,false)
        studentAdapter = StudentAdapter(currActivity,studentList,selectedRole)
        binding!!.rvChildList.adapter = studentAdapter
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

    fun updateStudentAdapter(){
        for(model in studentList){
            model.isSelected = false
        }
        studentAdapter.notifyDataSetChanged()
    }
    fun updateRoleAdapter(){
        for(model in schoolList){
            model.isSelected = false
        }
        adapter.notifyDataSetChanged()
    }

}
