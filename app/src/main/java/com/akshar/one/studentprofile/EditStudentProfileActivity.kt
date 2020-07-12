package com.akshar.one.studentprofile

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.akshar.one.R
import com.akshar.one.app.AksharSchoolApplication.Companion.context
import com.akshar.one.databinding.ActivityEditStudentProfileBinding
import com.akshar.one.model.StudentListModel
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.student.StudentViewModel
import kotlinx.android.synthetic.main.activity_edit_student_profile.*
import kotlinx.android.synthetic.main.main_toolbar.view.*
import android.widget.AdapterView
import android.R.attr.country
import android.R.attr.mode
import android.content.Context
import android.content.ContextWrapper
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import com.akshar.one.adapter.MySpinnerAdapter
import com.akshar.one.api.service.ApiInterface
import com.akshar.one.app.AksharSchoolApplication
import com.akshar.one.util.*
import com.bumptech.glide.Glide
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_login.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class EditStudentProfileActivity : AppCompatActivity(), View.OnClickListener{

    private var currActivity : Activity = this
    private var binding : ActivityEditStudentProfileBinding? = null
    private var studentModel : StudentListModel? = null
    private var studentList : ArrayList<StudentListModel> = ArrayList<StudentListModel>()
    private var studentViewModel : StudentViewModel? = null
    private var bloodGroup = ""
    private var mSelectedImagePath: String = ""
    private var state = ""
    private var image = ""
    private val RESULT_LOAD = 423
    private val REQUEST_CAMERA = 433
    private var file: Uri? = null
    private var profileFile: File? = null
    private var position = 0
    private var model : StudentListModel? =null
    var bloodGroupList = arrayOf("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "Ab-")
    var states = arrayOf("Andra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Goa", "Gujarat", "Haryana","Himachal Pradesh",
        "Jammu and Kashmir","Jharkhand","Karnataka","Kerala","Madya Pradesh","Maharashtra","Manipur","Meghalaya","Mizoram","Nagaland","Orissa",
        "Punjab","Rajasthan","Sikkim","Tamil Nadu","Telagana","Tripura","Uttaranchal","Uttar Pradesh","West Bengal")


    companion object {
        fun open(currActivity: Activity, studentModel : StudentListModel,position : Int,studentList : ArrayList<StudentListModel>) {
            val intent = Intent(currActivity, EditStudentProfileActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("studentModel",studentModel)
            intent.putExtra("studentList",studentList)
            intent.putExtra("position",position)
            currActivity.startActivity(intent)
            currActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(currActivity,R.layout.activity_edit_student_profile)
        studentModel = intent.getSerializableExtra("studentModel") as StudentListModel
        position = intent.getIntExtra("position",0)
        studentList = intent.getSerializableExtra("studentList") as ArrayList<StudentListModel>
        initViews()
        setData()
        setListner()
    }

    private fun setData(){
        if( studentModel!= null){
            if(studentModel!!.firstName!= null){
                binding!!.etFirstName.setText(studentModel!!.firstName)
            }
            if(studentModel!!.lastName!= null){
                binding!!.etLastName.setText(studentModel!!.lastName)
            }
            if(studentModel!!.admissionNumber!= null){
                binding!!.etAdmissionNumber.setText(studentModel!!.admissionNumber)
            }


            if( studentModel!!.bloodGroup != null){
                bloodGroup = studentModel!!.bloodGroup!!
            }

            if(studentModel!!.gender != null){
                val gender = studentModel!!.gender
                if(gender.equals("Female",true)){
                    binding!!.rbMale.isChecked = false
                    binding!!.rbFemale.isChecked = true
                }else if(gender.equals("Male",true)){
                    binding!!.rbMale.isChecked = true
                    binding!!.rbFemale.isChecked = false
                }
            }

            if(studentModel!!.studentContact!= null){
                if(studentModel!!.studentContact.parentName!= null){
                    binding!!.etParentName.setText(studentModel!!.studentContact.parentName)

                }
                if(studentModel!!.studentContact.relationship!= null){
                    binding!!.etRelationShip.setText(studentModel!!.studentContact.relationship)
                }

                if(studentModel!!.studentContact.mobile!= null){
                    binding!!.etMobileNumber.setText(studentModel!!.studentContact.mobile)
                }
                if(studentModel!!.studentContact.email!= null){
                    binding!!.etEmailAddress.setText(studentModel!!.studentContact.email)

                }
                if(studentModel!!.studentContact.address!= null){
                    if(studentModel!!.studentContact.address.addressLine1!= null){
                        binding!!.etHouseNumber.setText(studentModel!!.studentContact.address.addressLine1)
                    }
                    if(studentModel!!.studentContact.address.addressLine2!= null){
                        binding!!.etStreet.setText(studentModel!!.studentContact.address.addressLine2)
                    }
                    if(studentModel!!.studentContact.address.city!= null){
                        binding!!.etCity.setText(studentModel!!.studentContact.address.city)
                    }

                    if(studentModel!!.studentContact.address.district!= null){
                        binding!!.etDistrict.setText(studentModel!!.studentContact.address.district)
                    }
                    if(studentModel!!.studentContact.address.postalcode!= null){
                        binding!!.etPostal.setText(studentModel!!.studentContact.address.postalcode)
                    }
                    state = studentModel!!.studentContact.address.state!!
                }

            }

        }

        val myAdap = binding!!.etBloodGroup.adapter as ArrayAdapter<String> //cast to an ArrayAdapter

        val spinnerPosition = myAdap.getPosition(bloodGroup)

        binding!!.etBloodGroup.setSelection(spinnerPosition)

        val myAdap1 = binding!!.etState.adapter as ArrayAdapter<String> //cast to an ArrayAdapter

        val spinnerPosition1 = myAdap1.getPosition(state)

        binding!!.etState.setSelection(spinnerPosition1)

    }

    private fun initViews(){
        currActivity.application?.let {
            studentViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(StudentViewModel::class.java)
        }

        binding!!.etBloodGroup.onItemSelectedListener = object : OnItemSelectedListener{
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

        binding!!.etState.onItemSelectedListener = object : OnItemSelectedListener {
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
        binding!!.toolbar.txtToolbarTitle.text = getString(com.akshar.one.R.string.edit_profile)
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
                        this@EditStudentProfileActivity,
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
                .start(this@EditStudentProfileActivity)
        } else if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            CropImage.activity(file).setInitialCropWindowPaddingRatio(0f)
                .start(this@EditStudentProfileActivity)
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.getUri()
                profileFile = File(resultUri.getPath()!!)
                image = ""
                mSelectedImagePath = resultUri.toString()
                Glide.with(currActivity).load(mSelectedImagePath).into(binding!!.imgUserImage)
                 callUploadPhotoApi();

            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCameraDialog()
        }
    }

    private fun callUploadPhotoApi(){
        studentViewModel.let {
            if(context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true){
                it!!.uploadImage(studentModel?.studentProfileId!!)
            }
        }

        studentViewModel?.getErrorMutableLiveData()?.observe(this, Observer {
            it.let {
                AndroidUtil.showToast(currActivity,it.message,true)
            }
        })

        studentViewModel?.getImageLiveData()?.observe(this, Observer {
            it.let {
                val url = it.URL
                imageUpload(url)
                AndroidUtil.showToast(currActivity,"Image Uploaded Successfully",false)
            }
        })



    }


    private fun imageUpload(url : String){
        val sign_path=profileFile
        val requestBody= RequestBody.create("image/jpg".toMediaTypeOrNull(),sign_path!!);
         val retro_image_upload =  Retrofit.Builder()
                                 .baseUrl(AksharSchoolApplication.getAppContext()?.getString(R.string.BASE_URL))
                                 .addConverterFactory(GsonConverterFactory.create())
                                 .build();
         val final_upload = retro_image_upload.create(ApiInterface::class.java);
         val image_upload_call = final_upload.aws_upload(url,requestBody);
         image_upload_call.enqueue(object : Callback<Void>{
             override fun onFailure(call: Call<Void>, t: Throwable) {
                 Log.d("system",t.message)
             }

             override fun onResponse(call: Call<Void>, response: Response<Void>) {
                 Log.d("success",response.toString())
             }


         })
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
         model  = StudentListModel()
        model!!.studentProfileId = studentModel?.studentProfileId!!
        model!!.classroomId = studentModel?.classroomId!!
        model!!.firstName = etFirstName.text.toString()
        model!!.lastName = etLastName.text.toString()
        model!!.admissionNumber = etAdmissionNumber.text.toString()
        var gender = ""
        if(binding!!.rbFemale.isChecked){
            gender = "Female"
        }else{
            gender = "Male"
        }
        model!!.gender = gender
        model!!.bloodGroup = bloodGroup
        model!!.studentContact.studentProfileId = studentModel?.studentProfileId
        model!!.studentContact.parentName = etParentName.text.toString()
        model!!.studentContact.relationship = etRelationShip.text.toString()
        model!!.studentContact.mobile = etMobileNumber.text.toString()
        model!!.studentContact.email = etEmailAddress.text.toString()
        model!!.studentContact.address.addressLine1 = etHouseNumber.text.toString()
        model!!.studentContact.address.addressLine2 = etStreet.text.toString()
        model!!.studentContact.address.city = etCity.text.toString()
        model!!.studentContact.address.state = state
        model!!.studentContact.address.district = etDistrict.text.toString()
        model!!.studentContact.address.postalcode = etPostal.text.toString()
        studentViewModel.let {
            if(context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true){
                it!!.updateStudentProfile(studentModel?.studentProfileId!!, model!!)
            }
        }

        observer()
    }

    private fun observer(){
        studentViewModel?.getIsLoading()?.observe(this, Observer {
            showProgressIndicator(it)
        })

        studentViewModel?.getErrorMutableLiveData()?.observe(this, Observer {
            it.let {
                AndroidUtil.showToast(currActivity,it.message,true)
            }
        })

        studentViewModel?.getSuccessLiveData()?.observe(this, Observer {
            it.let {
                AndroidUtil.showToast(currActivity,"User Updated Successfully",false)
                onBackPressed()
            }
        })

    }

    private fun showProgressIndicator(isLoading: Boolean?) {
        binding!!.linProgressIndicator.visibility = if (isLoading == true) View.VISIBLE else View.GONE
    }

    override fun onBackPressed() {
        if(model != null){
            studentList.set(position,model!!)
        }

        ViewStudentProfileActivity.open(currActivity,studentList,position)
    }

}
