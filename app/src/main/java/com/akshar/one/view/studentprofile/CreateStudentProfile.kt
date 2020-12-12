package com.akshar.one.view.studentprofile

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.akshar.one.R
import com.akshar.one.adapter.MySpinnerAdapter
import com.akshar.one.app.AksharSchoolApplication
import com.akshar.one.databinding.ActivityCreateStudentProfileBinding
import com.akshar.one.model.ClassDropDownModel
import com.akshar.one.model.SectionList
import com.akshar.one.model.StudentListModel
import com.akshar.one.util.*
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.student.StudentViewModel
import com.bumptech.glide.Glide
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_create_student_profile.*
import kotlinx.android.synthetic.main.activity_edit_student_profile.*
import kotlinx.android.synthetic.main.activity_edit_student_profile.etEmailAddress
import kotlinx.android.synthetic.main.activity_edit_student_profile.etFirstName
import kotlinx.android.synthetic.main.activity_edit_student_profile.etLastName
import kotlinx.android.synthetic.main.activity_edit_student_profile.etMobileNumber
import kotlinx.android.synthetic.main.main_toolbar.view.*
import java.io.File

class CreateStudentProfile : AppCompatActivity(),View.OnClickListener{

    private var currActivity : Activity = this
    private var binding : ActivityCreateStudentProfileBinding? = null
    private var studentModel : StudentListModel? = null
    private var studentViewModel : StudentViewModel? = null
    private var mSelectedImagePath: String = ""
    private var bloodGroup = ""
    private var state = ""
    private var image = ""
    private val RESULT_LOAD = 423
    private val REQUEST_CAMERA = 433
    private var file: Uri? = null
    private var dialog: Dialog? = null
    private var classDropDownModel : ClassDropDownModel = ClassDropDownModel()
    private var sectionList = SectionList()
    var bloodGroupList = arrayOf("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "Ab-")
    var states = arrayOf("Andra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Goa", "Gujarat", "Haryana","Himachal Pradesh",
        "Jammu and Kashmir","Jharkhand","Karnataka","Kerala","Madya Pradesh","Maharashtra","Manipur","Meghalaya","Mizoram","Nagaland","Orissa",
        "Punjab","Rajasthan","Sikkim","Tamil Nadu","Telagana","Tripura","Uttaranchal","Uttar Pradesh","West Bengal")


    companion object {
        fun open(currActivity: Activity,classDropDownModel : ClassDropDownModel ,sectionModel : SectionList) {
            val intent = Intent(currActivity, CreateStudentProfile::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("classDropDownModel",classDropDownModel)
            intent.putExtra("sectionModel",sectionModel)
            currActivity.startActivity(intent)
            currActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(currActivity,R.layout.activity_create_student_profile)
        classDropDownModel = intent.getSerializableExtra("classDropDownModel") as ClassDropDownModel
        sectionList = intent.getSerializableExtra("sectionModel") as SectionList
        initViews()
        setListner()
    }

    private fun initViews(){
        currActivity.application?.let {
            studentViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(StudentViewModel::class.java)
        }

        binding!!.etBloodGroup.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                bloodGroup = bloodGroupList[p2]
            }

        }

        val aa = MySpinnerAdapter(this, R.layout.spinner_item, bloodGroupList)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //Setting the ArrayAdapter data on the Spinner
        binding!!.etBloodGroup.adapter = aa

        binding!!.etState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                state = states[position]
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {

            }

        }

        val aa1 = MySpinnerAdapter(this,R.layout.spinner_item, states)
        aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //Setting the ArrayAdapter data on the Spinner
        binding!!.etState.adapter = aa1

        binding!!.toolbar.imgMenu.visibility = View.GONE
        binding!!.toolbar.imgBack.visibility = View.VISIBLE
        binding!!.toolbar.txtToolbarTitle.text = getString(com.akshar.one.R.string.create_profile)
    }

    private fun setListner(){
        binding!!.toolbar.imgBack.setOnClickListener(this)
        binding!!.tvSave.setOnClickListener(this)
        binding!!.imgUserImage.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.imgBack ->{
                onBackPressed()
            }
            R.id.tvSave ->{
                if(validation()){
                    updateStudentData()
                }
            }
            R.id.imgUserImage ->{
                if (CheckPermission.checkCameraPermission(currActivity))
                    openCameraDialog()
                else
                    CheckPermission.requestCameraPermission(
                        this@CreateStudentProfile,
                        CheckPermission.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS
                    )
            }

        }

    }

    private fun openCameraDialog() {
        val options = arrayOf<CharSequence>("Camera", "Gallery")
        val builder = AlertDialog.Builder(currActivity)
        builder.setItems(options) { dialogInterface, i ->
            if (options[i] == "Camera") {
                takePicture()
            } else if (options[i] == "Gallery") {
                galleryIntent()
            }
        }
        builder.show()
    }

    private fun galleryIntent() {
        try {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, RESULT_LOAD)

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun takePicture() {
        try {

            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val capturedFile = AppUtils.getOutPutMediaFile(currActivity)
            if (capturedFile != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    file = FileProvider.getUriForFile(
                        currActivity,
                        currActivity.getApplicationContext().getPackageName() + ".provider",
                        capturedFile!!
                    )
                } else {
                    file = Uri.fromFile(capturedFile)
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, file)
                intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                intent.flags = Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                startActivityForResult(intent, REQUEST_CAMERA)

            } else {
                Toast.makeText(currActivity, getString(R.string.permissionNeccesary), Toast.LENGTH_LONG)
                    .show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_LOAD && resultCode == Activity.RESULT_OK && data != null) {
            val FilePath = data.data
            CropImage.activity(FilePath).setInitialCropWindowPaddingRatio(0f)
                .start(this@CreateStudentProfile)
        } else if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            CropImage.activity(file).setInitialCropWindowPaddingRatio(0f)
                .start(this@CreateStudentProfile)
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.getUri()
                val file = File(resultUri.getPath()!!)
                image = ""
                mSelectedImagePath = resultUri.toString()
                Glide.with(currActivity).load(mSelectedImagePath).into(binding!!.imgUserImage)
               //  callUploadPhotoApi();

            }
        }

    }


    private fun callUploadPhotoApi(){
//        studentViewModel.let {
//            if(AksharSchoolApplication.context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true){
//                it!!.uploadImage(studentModel?.studentProfileId!!)
//            }
//        }

//        studentViewModel?.getErrorMutableLiveData()?.observe(this, Observer {
//            it.let {
//                AndroidUtil.showToast(currActivity,it.message,true)
//            }
//        })

//        studentViewModel?.getImageLiveData()?.observe(this, Observer {
//            it.let {
//                AndroidUtil.showToast(currActivity,"Image Uploaded Successfully",false)
//            }
//        })



    }


    private fun validation() : Boolean{
        var isValid = true

        if(binding!!.etFirstName.text.toString().isEmpty()){
            AndroidUtil.showToast(currActivity,"First Name is required",true)
            isValid = false
        } else if(binding!!.etLastName.text.toString().isEmpty()){
            AndroidUtil.showToast(currActivity,"Last Name is required",true)
            isValid = false
        }else if(binding!!.etEmailAddress.text.toString().isEmpty()){
            AndroidUtil.showToast(currActivity,"email is required",true)
            isValid = false
        }else if(!binding!!.rbMale.isChecked && !binding!!.rbFemale.isChecked){
            AndroidUtil.showToast(currActivity,"Student Gender is required",true)
            isValid = false
        }else if(binding!!.etAdmissionNumber.text.toString().isEmpty()){
            AndroidUtil.showToast(currActivity,"Admission number is required",true)
            isValid = false
        }else if(binding!!.etParentName.text.toString().isEmpty()){
            AndroidUtil.showToast(currActivity,"Parent name is required",true)
            isValid = false
        }else if(binding!!.etRelationShip.text.toString().isEmpty()){
            AndroidUtil.showToast(currActivity,"Parent Relationship is required",true)
            isValid = false
        }else if(binding!!.etMobileNumber.text.toString().isEmpty()){
            AndroidUtil.showToast(currActivity,"Parent Mobile number is required",true)
            isValid = false
        }else if(binding!!.etHouseNumber.text.toString().isEmpty()){
            AndroidUtil.showToast(currActivity,"House number is required",true)
            isValid = false
        }else if(binding!!.etStreet.text.toString().isEmpty()){
            AndroidUtil.showToast(currActivity,"Street is required",true)
            isValid = false
        }else if(binding!!.etCity.text.toString().isEmpty()){
            AndroidUtil.showToast(currActivity,"City is required",true)
            isValid = false
        }else if(state.isEmpty()){
            AndroidUtil.showToast(currActivity,"State is required",true)
            isValid = false
        }else if(bloodGroup.isEmpty()){
            AndroidUtil.showToast(currActivity,"blood group is required",true)
            isValid = false
        }else if(binding!!.etDistrict.text.toString().isEmpty()){
            AndroidUtil.showToast(currActivity,"District is required",true)
            isValid = false
        }else if(binding!!.etPostal.text.toString().isEmpty()){
            AndroidUtil.showToast(currActivity,"Postal code is required",true)
            isValid = false
        }

        return isValid
    }

    private fun updateStudentData(){

        val model  = StudentListModel()
        model.firstName = binding!!.etFirstName.text.toString()
        model.lastName = binding!!.etLastName.text.toString()
        model.admissionNumber = binding!!.etAdmissionNumber.text.toString()
        model.classroomId = sectionList.classroomId
        model.classroom.courseName = classDropDownModel.courseName
        model.classroom.classroomName = sectionList.classroomName
        var gender = ""
        if(binding!!.rbFemale.isChecked){
            gender = "Female"
        }else{
            gender = "Male"
        }
        model.gender = gender
        model.bloodGroup = bloodGroup
        model.studentContact.email = binding!!.etEmailAddress.text.toString()
        model.studentContact.parentName = binding!!.etParentName.text.toString()
        model.studentContact.relationship = binding!!.etRelationShip.text.toString()
        model.studentContact.mobile = binding!!.etMobileNumber.text.toString()
        model.studentContact.address.addressLine1 = binding!!.etHouseNumber.text.toString()
        model.studentContact.address.addressLine2 = binding!!.etStreet.text.toString()
        model.studentContact.address.city = binding!!.etCity.text.toString()
        model.studentContact.address.state = state
        model.studentContact.address.district = binding!!.etDistrict.text.toString()
        model.studentContact.address.postalcode = binding!!.etPostal.text.toString()
        studentViewModel.let {
            if(AksharSchoolApplication.context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true){
                showProgressBar()
                it!!.CreateStudentProfile(model)
            }
        }
        observer()

    }

    private fun observer(){

        studentViewModel?.getIsLoading()?.observe(this, Observer {
            showProgressIndicator(it)
        })

        studentViewModel?.getErrorMutableLiveData()?.observe(this, Observer {
            hideProgressBar()
            it.let {
                AndroidUtil.showToast(currActivity,it.message,true)
            }
        })

        studentViewModel?.getSuccessLiveData()?.observe(this, Observer {
            hideProgressBar()
            it.let {
                AndroidUtil.showToast(currActivity,"User Created Successfully",false)
                onBackPressed()
            }
        })

    }


    private fun showProgressIndicator(isLoading: Boolean?) {
        binding!!.linProgressIndicator.visibility = if (isLoading == true) View.VISIBLE else View.GONE
    }


    fun showProgressBar(){
        dialog =  AppUtils.showProgress(currActivity)
    }

    fun hideProgressBar(){
        AppUtils.hideProgress(dialog!!)
    }


}
