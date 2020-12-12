package com.akshar.one.view.studentprofile

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
import com.akshar.one.manager.SessionManager
import com.akshar.one.util.AndroidUtil
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.student.StudentViewModel


class ViewStudentProfileActivity : AppCompatActivity(), View.OnClickListener {


    private var binding : ActivityViewStudentProfileBinding? = null
    private var currActivity : Activity = this
    private var studentList = ArrayList<StudentListModel>()
    private var student = StudentListModel()
    private var position : Int = 0
    private var right = false
    private var securityList : ArrayList<String>? = null
    private var dialog: Dialog? = null
    private var studentViewModel: StudentViewModel? = null


    companion object {
        fun open(currActivity: Activity,studentList : ArrayList<StudentListModel>?,position : Int?,securityList : ArrayList<String>?) {
            val intent = Intent(currActivity, ViewStudentProfileActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("position",position)
            intent.putExtra("studentList",studentList)
            intent.putExtra("securityList",securityList)
            currActivity.startActivity(intent)
            currActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(currActivity,R.layout.activity_view_student_profile)
        securityList = intent.getSerializableExtra("securityList") as ArrayList<String>
        if(SessionManager.getLoginRole()?.appName.equals("SmartParent")){
            fetchStudentProfile()
            binding!!.rlNextPrevious.visibility = View.GONE
        }else{
            studentList = intent.getSerializableExtra("studentList") as ArrayList<StudentListModel>
            position = intent.getIntExtra("position",0)
            setData(position)
            binding!!.rlNextPrevious.visibility = View.VISIBLE
        }


        setListner()
        initViews()
    }

    private fun fetchStudentProfile(){
        currActivity.application?.let {
            studentViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(StudentViewModel::class.java)
        }

        studentViewModel.let {
            if (AksharSchoolApplication.context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                showProgressBar()
                it!!.getStudentProfile()
            }
        }


        observer()

    }

    private fun observer(){
        studentViewModel?.getErrorMutableLiveData()?.observe(this, Observer {
            it.let {
                hideProgressBar()
                AndroidUtil.showToast(currActivity, it.message, true)
            }
        })

//        studentViewModel?.getStudentListLiveData()?.observe(this, Observer {
//            hideProgressBar()
//            studentList.clear()
//            studentList.addAll(it)
//            setData(0)
//        })
        studentViewModel?.getStudentProfileLiveData()?.observe(this, Observer {
            hideProgressBar()
            student = it
            setStudent(it)
        })
    }

    fun showProgressBar(){
        dialog =  AppUtils.showProgress(currActivity)
    }

    fun hideProgressBar(){
        AppUtils.hideProgress(dialog!!)
    }


    private fun setData(position: Int){
        if(right){
            val RightSwipe = AnimationUtils.loadAnimation(this@ViewStudentProfileActivity, R.anim.left_slide)
            binding!!.rlStudentInfo.animation = RightSwipe
            binding!!.rlParentDetails.animation = RightSwipe
            binding!!.rlAddress.animation = RightSwipe
        }else if(!right){
            val leftSwipe = AnimationUtils.loadAnimation(this@ViewStudentProfileActivity, R.anim.right_slide)
            binding!!.rlStudentInfo.animation = leftSwipe
            binding!!.rlParentDetails.animation = leftSwipe
            binding!!.rlAddress.animation = leftSwipe
        }
        studentList.let {
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
            if(model.bloodGroup!= null){
                binding!!.tvBloodGroup.text = " "+model.bloodGroup
            }else{
                binding!!.tvBloodGroup.text = " N/A"
            }
            if(model.admissionNumber.toString()!= null){
                binding!!.tvAdmissionNumber.text = " "+model.admissionNumber.toString()
            }else{
                binding!!.tvAdmissionNumber.text = " N/A"
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

            if(model.studentContact!= null){
                if(model.studentContact.address != null){

                    if(model.studentContact.address.addressLine1 != null){
                        binding!!.tvHouseNumber.text = " "+model.studentContact.address?.addressLine1
                    }else{
                        binding!!.tvHouseNumber.text = "N/A"
                    }
                    if(model.studentContact.address.addressLine2 != null){
                        binding!!.tvStreet.text = " "+model.studentContact.address?.addressLine2
                    }else{
                        binding!!.tvStreet.text = " N/A"
                    }
                    if(model.studentContact.address.city != null){
                        binding!!.tvCity.text = " "+model.studentContact.address?.city
                    }else{
                        binding!!.tvCity.text = " N/A"
                    }
                    if(model.studentContact.address.district != null){
                        binding!!.tvDistrict.text = " "+model.studentContact.address?.district
                    }else{
                        binding!!.tvDistrict.text = " N/A"
                    }
                    if(model.studentContact.address.state!= null){
                        binding!!.tvState.text = " "+model.studentContact.address?.state
                    }else{
                        binding!!.tvState.text = " N/A"
                    }
                    if(model.studentContact.address.postalcode != null){
                        binding!!.tvPostal.text = " "+model.studentContact.address?.postalcode
                    }else{
                        binding!!.tvPostal.text = " N/A"
                    }
                    if(model.studentContact.parentName!= null){
                        binding!!.tvParentName.text = " "+model.studentContact.parentName
                    }else{
                        binding!!.tvParentName.text = " N/A"
                    }

                    if(model.studentContact.relationship!= null){
                        binding!!.tvRelationShip.text = " "+model.studentContact.relationship
                    }else{
                        binding!!.tvRelationShip.text = " N/A"
                    }

                    if(model.studentContact.mobile != null){
                        binding!!.tvMobile.text = " "+model.studentContact.mobile
                    }else{
                        binding!!.tvMobile.text = " N/A"
                    }
                    if(model.studentContact.email != null){
                        binding!!.tvEmail.text = " "+model.studentContact.email
                    }else{
                        binding!!.tvEmail.text = " N/A"
                    }

                }else{
                    binding!!.tvHouseNumber.text = "N/A"
                    binding!!.tvStreet.text = " N/A"
                    binding!!.tvCity.text = " N/A"
                    binding!!.tvDistrict.text = " N/A"
                    binding!!.tvState.text = " N/A"
                    binding!!.tvPostal.text = " N/A"
                    binding!!.tvMobile.text = " N/A"
                    binding!!.tvRelationShip.text = " N/A"
                    binding!!.tvParentName.text = " N/A"
                    binding!!.tvEmail.text = " N/A"
                }
            }
        }
    }
    private fun setStudent(model: StudentListModel){
//        if(right){
//            val RightSwipe = AnimationUtils.loadAnimation(this@ViewStudentProfileActivity, R.anim.left_slide)
//            binding!!.rlStudentInfo.animation = RightSwipe
//            binding!!.rlParentDetails.animation = RightSwipe
//            binding!!.rlAddress.animation = RightSwipe
//        }else if(!right){
//            val leftSwipe = AnimationUtils.loadAnimation(this@ViewStudentProfileActivity, R.anim.right_slide)
//            binding!!.rlStudentInfo.animation = leftSwipe
//            binding!!.rlParentDetails.animation = leftSwipe
//            binding!!.rlAddress.animation = leftSwipe
//        }

          //  val model = it[position]
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
            if(model.bloodGroup!= null){
                binding!!.tvBloodGroup.text = " "+model.bloodGroup
            }else{
                binding!!.tvBloodGroup.text = " N/A"
            }
            if(model.admissionNumber.toString()!= null){
                binding!!.tvAdmissionNumber.text = " "+model.admissionNumber.toString()
            }else{
                binding!!.tvAdmissionNumber.text = " N/A"
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

            if(model.studentContact!= null){
                if(model.studentContact.address != null){

                    if(model.studentContact.address.addressLine1 != null){
                        binding!!.tvHouseNumber.text = " "+model.studentContact.address?.addressLine1
                    }else{
                        binding!!.tvHouseNumber.text = "N/A"
                    }
                    if(model.studentContact.address.addressLine2 != null){
                        binding!!.tvStreet.text = " "+model.studentContact.address?.addressLine2
                    }else{
                        binding!!.tvStreet.text = " N/A"
                    }
                    if(model.studentContact.address.city != null){
                        binding!!.tvCity.text = " "+model.studentContact.address?.city
                    }else{
                        binding!!.tvCity.text = " N/A"
                    }
                    if(model.studentContact.address.district != null){
                        binding!!.tvDistrict.text = " "+model.studentContact.address?.district
                    }else{
                        binding!!.tvDistrict.text = " N/A"
                    }
                    if(model.studentContact.address.state!= null){
                        binding!!.tvState.text = " "+model.studentContact.address?.state
                    }else{
                        binding!!.tvState.text = " N/A"
                    }
                    if(model.studentContact.address.postalcode != null){
                        binding!!.tvPostal.text = " "+model.studentContact.address?.postalcode
                    }else{
                        binding!!.tvPostal.text = " N/A"
                    }
                    if(model.studentContact.parentName!= null){
                        binding!!.tvParentName.text = " "+model.studentContact.parentName
                    }else{
                        binding!!.tvParentName.text = " N/A"
                    }

                    if(model.studentContact.relationship!= null){
                        binding!!.tvRelationShip.text = " "+model.studentContact.relationship
                    }else{
                        binding!!.tvRelationShip.text = " N/A"
                    }

                    if(model.studentContact.mobile != null){
                        binding!!.tvMobile.text = " "+model.studentContact.mobile
                    }else{
                        binding!!.tvMobile.text = " N/A"
                    }
                }else{
                    binding!!.tvHouseNumber.text = "N/A"
                    binding!!.tvStreet.text = " N/A"
                    binding!!.tvCity.text = " N/A"
                    binding!!.tvDistrict.text = " N/A"
                    binding!!.tvState.text = " N/A"
                    binding!!.tvPostal.text = " N/A"
                    binding!!.tvMobile.text = " N/A"
                    binding!!.tvRelationShip.text = " N/A"
                    binding!!.tvParentName.text = " N/A"
                }
            }

    }

    private fun initViews(){
        binding!!.toolbar.imgMenu.visibility = View.GONE
        binding!!.toolbar.imgBack.visibility = View.VISIBLE
        binding!!.toolbar.txtToolbarTitle.text = getString(R.string.view_profile)

        if(securityList!= null){
            if(securityList!!.contains("M_STUDENT_PROFILE_EDIT")){
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
                    if(position == studentList.size-1){
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
                if(position == studentList.size-1){
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
                    if(position == studentList.size-1){
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
                    EditStudentProfileActivity.open(currActivity,student,0,null,securityList!!)

                }else{
                    EditStudentProfileActivity.open(currActivity,studentList[position],position,studentList,securityList!!)

                }
            }
        }
    }
}
