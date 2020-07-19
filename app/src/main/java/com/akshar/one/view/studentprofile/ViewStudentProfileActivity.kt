package com.akshar.one.view.studentprofile

import android.app.Activity
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


class ViewStudentProfileActivity : AppCompatActivity(), View.OnClickListener {


    private var binding : ActivityViewStudentProfileBinding? = null
    private var currActivity : Activity = this
    private var studentList = ArrayList<StudentListModel>()
    private var position : Int = 0
    private var right = false

    companion object {
        fun open(currActivity: Activity,studentList : ArrayList<StudentListModel>,position : Int) {
            val intent = Intent(currActivity, ViewStudentProfileActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("position",position)
            intent.putExtra("studentList",studentList)
            currActivity.startActivity(intent)
            currActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(currActivity,R.layout.activity_view_student_profile)
        studentList = intent.getSerializableExtra("studentList") as ArrayList<StudentListModel>
        position = intent.getIntExtra("position",0)
        setData(position)
        setListner()
        initViews()
    }

    private fun setData(position: Int){

        if(right){
            val RightSwipe = AnimationUtils.loadAnimation(this@ViewStudentProfileActivity, R.anim.right_slide)
            binding!!.rlStudentInfo.animation = RightSwipe
            binding!!.rlParentDetails.animation = RightSwipe
            binding!!.rlAddress.animation = RightSwipe
        }else if(!right){
            val leftSwipe = AnimationUtils.loadAnimation(this@ViewStudentProfileActivity, R.anim.left_slide)
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
            if(model.imageUrl!= null || model.imageUrl!=""){
                AppUtils.loadImageCrop(
                   model.imageUrl,
                    binding!!.imgUserImage,
                    R.drawable.circle_default_pic,
                    100,
                    100
                )
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
    }

    private fun initViews(){
        binding!!.toolbar.imgMenu.visibility = View.GONE
        binding!!.toolbar.imgBack.visibility = View.VISIBLE
        binding!!.toolbar.txtToolbarTitle.text = getString(R.string.view_profile)
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
            R.id.rlNext ->{
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
                EditStudentProfileActivity.open(currActivity,studentList[position],position,studentList)
            }
        }
    }
}
