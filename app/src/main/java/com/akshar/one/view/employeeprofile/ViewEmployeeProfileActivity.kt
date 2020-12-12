package com.akshar.one.view.employeeprofile

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.akshar.one.R
import com.akshar.one.databinding.ActivityViewStudentProfileBinding
import com.akshar.one.model.StudentListModel
import com.akshar.one.util.AppUtils
import kotlinx.android.synthetic.main.main_toolbar.view.*
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.akshar.one.app.AksharSchoolApplication
import com.akshar.one.databinding.ActivityViewEmployeeProfileBinding
import com.akshar.one.manager.SessionManager
import com.akshar.one.model.EmployeeList
import com.akshar.one.util.AndroidUtil
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.employee.EmployeeViewModel
import com.akshar.one.viewmodels.student.StudentViewModel


class ViewEmployeeProfileActivity : AppCompatActivity(), View.OnClickListener {


    private var binding : ActivityViewEmployeeProfileBinding? = null
    private var currActivity : Activity = this
    private var employeeList = ArrayList<EmployeeList>()
    private var employee = EmployeeList()
    private var position : Int = 0
    private var right = false
    private var securityList : ArrayList<String>? = null
    private var dialog: Dialog? = null
    private var employeeViewModel: EmployeeViewModel? = null


    companion object {
        fun open(currActivity: Activity,employeeList : ArrayList<EmployeeList>?,position : Int?,securityList : ArrayList<String>?) {
            val intent = Intent(currActivity, ViewEmployeeProfileActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("position",position)
            intent.putExtra("employeeList",employeeList)
            intent.putExtra("securityList",securityList)
            currActivity.startActivity(intent)
            currActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(currActivity,R.layout.activity_view_employee_profile)
        securityList = intent.getSerializableExtra("securityList") as ArrayList<String>
        employeeList = intent.getSerializableExtra("employeeList") as ArrayList<EmployeeList>
        position = intent.getIntExtra("position",0)
        setData(position)
        binding!!.rlNextPrevious.visibility = View.VISIBLE



        setListner()
        initViews()
    }



    fun showProgressBar(){
        dialog =  AppUtils.showProgress(currActivity)
    }

    fun hideProgressBar(){
        AppUtils.hideProgress(dialog!!)
    }


    private fun setData(position: Int){
        if(right){
            val RightSwipe = AnimationUtils.loadAnimation(this, R.anim.left_slide)
            binding!!.rlStudentInfo.animation = RightSwipe
            binding!!.rlEmployeeDetails.animation = RightSwipe
        }else if(!right){
            val leftSwipe = AnimationUtils.loadAnimation(this, R.anim.right_slide)
            binding!!.rlStudentInfo.animation = leftSwipe
            binding!!.rlEmployeeDetails.animation = leftSwipe
        }
        employeeList.let {
            val model = it[position]
            var name = ""
            if(model.lastName!= null) {
                name = " "+model.firstName+" "+model.lastName
            }else if(model.lastName!= null){
                name = " "+model.firstName!!
            }else{
                name = " N/A"
            }
            if(model.gender!= null){
                binding!!.tvGender.text = " "+model.gender
            }else{
                binding!!.tvGender.text = " N/A"
            }
            if(model.imageUrl!= null && model.imageUrl!=""){
                binding!!.flLayout.visibility = View.GONE
                binding!!.imgUserImage.visibility = View.VISIBLE
                AppUtils.loadImageCrop(
                    model.imageUrl,

                    binding!!.imgUserImage,
                    R.drawable.circle_default_pic,
                    100,
                    100
                )
            }else {
                binding!!.flLayout.visibility = View.VISIBLE
                binding!!.imgUserImage.visibility = View.GONE
                if(model.lastName!= null){
                    binding!!.tvShortName.setText(model.firstName!!.substring(0,1).toUpperCase() + model!!.lastName!!.substring(0, 1).toUpperCase()
                    )
                }else{
                    binding!!.tvShortName.setText(
                        model.firstName!!.substring(0, 2).toUpperCase()
                    )
                }

            }

            binding!!.tvStudentName.text = name
            binding!!.tvDesignation.text = model.designation
            binding!!.tvDepartment.text = model.department
            binding!!.tvMobile.text = model.phonePrimary
            binding!!.tvEmail.text = model.email

            if(model.address != null){

                if(model.address.addressLine1 != null){
                    binding!!.tvHouseNumber.text = " "+model.address?.addressLine1
                }else{
                    binding!!.tvHouseNumber.text = "N/A"
                }
                if(model.address.addressLine2 != null){
                    binding!!.tvStreet.text = " "+model.address?.addressLine2
                }else{
                    binding!!.tvStreet.text = " N/A"
                }
                if(model.address.city != null){
                    binding!!.tvCity.text = " "+model.address?.city
                }else{
                    binding!!.tvCity.text = " N/A"
                }
                if(model.address.district != null){
                    binding!!.tvDistrict.text = " "+model.address?.district
                }else{
                    binding!!.tvDistrict.text = " N/A"
                }
                if(model.address.state!= null){
                    binding!!.tvState.text = " "+model.address?.state
                }else{
                    binding!!.tvState.text = " N/A"
                }
                if(model.address.postalcode != null){
                    binding!!.tvPostal.text = " "+model.address?.postalcode
                }else{
                    binding!!.tvPostal.text = " N/A"
                }

            }else{
                binding!!.tvHouseNumber.text = "N/A"
                binding!!.tvStreet.text = " N/A"
                binding!!.tvCity.text = " N/A"
                binding!!.tvDistrict.text = " N/A"
                binding!!.tvState.text = " N/A"
                binding!!.tvPostal.text = " N/A"
                binding!!.tvEmail.text = " N/A"
            }




        }
    }

    private fun initViews(){
        binding!!.toolbar.imgMenu.visibility = View.GONE
        binding!!.toolbar.imgBack.visibility = View.VISIBLE
        binding!!.toolbar.txtToolbarTitle.text = getString(R.string.view_profile)

        if(securityList!= null){
            if(securityList!!.contains("EMPLOYEE_PROFILE_EDIT")){
                binding!!.rlEditProfile.visibility = View.VISIBLE
            }else{
                binding!!.rlEditProfile.visibility = View.INVISIBLE
            }

        }
    }

    private fun setListner(){
        binding!!.rlStudentInfo.setOnClickListener(this)
        binding!!.toolbar.imgBack.setOnClickListener(this)
        binding!!.rlNext.setOnClickListener(this)
        binding!!.rlPrevious.setOnClickListener(this)
        binding!!.rlEditProfile.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.imgBack ->{
                onBackPressed()
            }
            R.id.rlPrevious ->{
                right = false
                if(position == 0){
                    //  AndroidUtil.showToast(currActivity,"You can't go previous",true)
                }else{
                    position = --position
                    setData(position)

                    if(position == 0){
                        binding!!.rlPrevious.setBackgroundColor(currActivity.resources.getColor(R.color.inactive_bluee))
                        binding!!.rlPrevious.isClickable = false
                        binding!!.rlPrevious.isEnabled = false
                    }else{
                        binding!!.rlPrevious.setBackgroundColor(currActivity.resources.getColor(R.color.dark_blue))
                        binding!!.rlPrevious.isClickable = true
                        binding!!.rlPrevious.isEnabled = true
                    }
                    if(position == employeeList.size-1){
                        binding!!.rlNext.setBackgroundColor(currActivity.resources.getColor(R.color.inactive_bluee))
                        binding!!.rlNext.isClickable = false
                        binding!!.rlNext.isEnabled = false
                    }else{
                        binding!!.rlNext.setBackgroundColor(currActivity.resources.getColor(R.color.dark_blue))
                        binding!!.rlNext.isClickable = true
                        binding!!.rlNext.isEnabled = true
                    }

                }
            }

            R.id.rlNext -> {
                right = true
                if(position == employeeList.size-1){
                    //  AndroidUtil.showToast(currActivity,"You can't go Next",true)
                }else{
                    position = ++position
                    setData(position)

                    if(position == 0){
                        binding!!.rlPrevious.setBackgroundColor(currActivity.resources.getColor(R.color.inactive_bluee))
                        binding!!.rlPrevious.isClickable = false
                        binding!!.rlPrevious.isEnabled = false
                    }else{
                        binding!!.rlPrevious.setBackgroundColor(currActivity.resources.getColor(R.color.dark_blue))
                        binding!!.rlPrevious.isClickable = true
                        binding!!.rlPrevious.isEnabled = true
                    }
                    if(position == employeeList.size-1){
                        binding!!.rlNext.setBackgroundColor(currActivity.resources.getColor(R.color.inactive_bluee))
                        binding!!.rlNext.isClickable = false
                        binding!!.rlNext.isEnabled = false
                    }else{
                        binding!!.rlNext.setBackgroundColor(currActivity.resources.getColor(R.color.dark_blue))
                        binding!!.rlNext.isClickable = true
                        binding!!.rlNext.isEnabled = true
                    }
                }

            }
            R.id.rlEditProfile ->{
                if(SessionManager.getLoginRole()?.appName.equals("SmartParent",true)){
                    EditEmployeeProfileActivity.open(currActivity,employee,0,null,securityList!!)

                }else{
                    EditEmployeeProfileActivity.open(currActivity,employeeList[position],position,employeeList,securityList!!)

                }
            }
        }
    }
}
