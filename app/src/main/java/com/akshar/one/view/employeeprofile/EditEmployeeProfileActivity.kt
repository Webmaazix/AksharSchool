package com.akshar.one.view.employeeprofile

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
import android.app.Dialog
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.FileProvider.getUriForFile
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.akshar.one.adapter.ClassDropDownAdapter
import com.akshar.one.adapter.MySpinnerAdapter
import com.akshar.one.api.service.ApiInterface
import com.akshar.one.app.AksharSchoolApplication
import com.akshar.one.databinding.ActivityEditEmployeeProfileBinding
import com.akshar.one.databinding.DialogSelectClassAndSectionBinding
import com.akshar.one.imagecropper.Crop
import com.akshar.one.manager.SessionManager
import com.akshar.one.model.ClassDropDownModel
import com.akshar.one.model.EmployeeList
import com.akshar.one.model.SectionList
import com.akshar.one.util.*
import com.akshar.one.viewmodels.employee.EmployeeViewModel
import com.akshar.one.viewmodels.timetable.TimeTableViewModel
import com.bumptech.glide.Glide
import com.theartofdev.edmodo.cropper.CropImage
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class EditEmployeeProfileActivity : AppCompatActivity(), View.OnClickListener{

    private var currActivity : Activity = this
    private var binding : ActivityEditEmployeeProfileBinding? = null
    private var employeeModel : EmployeeList? = null
    private var employeeList : ArrayList<EmployeeList> = ArrayList<EmployeeList>()
    private var employeeViewModel : EmployeeViewModel? = null

    private var mSelectedImagePath: String = ""
    private var bloodGroup = ""
    private var state = ""
    private var image = ""
    private val RESULT_LOAD = 423
    private val REQUEST_CAMERA = 433
    private var file: Uri? = null
    private var profileFile: File? = null
    private var position = 0
    private var securityList : ArrayList<String>? = null
    private var timeTableViewModel : TimeTableViewModel? = null
    private var model : EmployeeList? =null
    var bloodGroupList = arrayOf("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "Ab-")
    var states = arrayOf("Andra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Goa", "Gujarat", "Haryana","Himachal Pradesh",
        "Jammu and Kashmir","Jharkhand","Karnataka","Kerala","Madya Pradesh","Maharashtra","Manipur","Meghalaya","Mizoram","Nagaland","Orissa",
        "Punjab","Rajasthan","Sikkim","Tamil Nadu","Telagana","Tripura","Uttaranchal","Uttar Pradesh","West Bengal")


    companion object {
        fun open(currActivity: Activity, employeeModel : EmployeeList,position : Int,employeeList : ArrayList<EmployeeList>?,securityList :
        ArrayList<String>) {
            val intent = Intent(currActivity, EditEmployeeProfileActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("employeeModel",employeeModel)
            intent.putExtra("employeeList",employeeList)
            intent.putExtra("position",position)
            intent.putExtra("securityList",securityList)
            currActivity.startActivity(intent)
            currActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(currActivity,R.layout.activity_edit_employee_profile)
        if(SessionManager.getLoginRole()?.appName.equals("SmartParent",true)) {

        }else{
            employeeList = intent.getSerializableExtra("employeeList") as ArrayList<EmployeeList>

        }
        employeeModel = intent.getSerializableExtra("employeeModel") as EmployeeList
        securityList = intent.getSerializableExtra("securityList") as ArrayList<String>
        position = intent.getIntExtra("position",0)
        initViews()
        setData()
        setListner()
    }

    private fun setData(){
        if( employeeModel!= null){
            if(employeeModel!!.firstName!= null){
                binding!!.etFirstName.setText(employeeModel!!.firstName)
            }
            if(employeeModel!!.lastName!= null){
                binding!!.etLastName.setText(employeeModel!!.lastName)
            }
            if(employeeModel!!.imageUrl!= null || employeeModel!!.imageUrl!=""){
                AppUtils.loadImageCrop(
                    employeeModel!!.imageUrl,
                    binding!!.imgUserImage,
                    R.drawable.circle_default_pic,
                    100,
                    100
                )
            }
            
            if(employeeModel!!.gender != null){
                val gender = employeeModel!!.gender
                if(gender.equals("Female",true)){
                    binding!!.rbMale.isChecked = false
                    binding!!.rbFemale.isChecked = true
                }else if(gender.equals("Male",true)){
                    binding!!.rbMale.isChecked = true
                    binding!!.rbFemale.isChecked = false
                }
            }

            if(employeeModel!!.address!= null){
                if(employeeModel!!.address.addressLine1!= null){
                    binding!!.etHouseNumber.setText(employeeModel!!.address.addressLine1)
                }
                if(employeeModel!!.address.addressLine2!= null){
                    binding!!.etStreet.setText(employeeModel!!.address.addressLine2)
                }
                if(employeeModel!!.address.city!= null){
                    binding!!.etCity.setText(employeeModel!!.address.city)
                }

                if(employeeModel!!.address.district!= null){
                    binding!!.etDistrict.setText(employeeModel!!.address.district)
                }
                if(employeeModel!!.address.postalcode!= null){
                    binding!!.etPostal.setText(employeeModel!!.address.postalcode)
                }
                if(employeeModel!!.address.state!= null){
                    state = employeeModel!!.address.state!!
                }else{
                    state = ""
                }

            }
          

        }

        val myAdap1 = binding!!.etState.adapter as ArrayAdapter<String> //cast to an ArrayAdapter

        val spinnerPosition1 = myAdap1.getPosition(state)

        binding!!.etState.setSelection(spinnerPosition1)

    }

    private fun initViews(){
        currActivity.application?.let {
            employeeViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(EmployeeViewModel::class.java)
        }

        observer()

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
                        this,
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
                        capturedFile
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
            beginCrop(FilePath)
//            CropImage.activity(FilePath).setInitialCropWindowPaddingRatio(0f)
//                .start(this@EditStudentProfileActivity)
        } else if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            beginCrop(file)
//            CropImage.activity(file).setInitialCropWindowPaddingRatio(0f)
//                .start(this@EditStudentProfileActivity)
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
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data!!)

//            mSelectedImagePath = resultUri.toString()
            callUploadPhotoApi();
        }

    }

    private fun beginCrop(source: Uri?) {
        val destination = Uri.fromFile(File(cacheDir, "cropped"))
        Crop.of(source, destination).asSquare().start(this)
    }

    private fun handleCrop(resultCode: Int, result: Intent) {
        if (resultCode == Activity.RESULT_OK) {
            Glide.with(currActivity).load(Crop.getOutput(result)).into(binding!!.imgUserImage)
            val resultUri = Crop.getOutput(result)
            profileFile = File(resultUri.getPath()!!)
            image = ""
            mSelectedImagePath = resultUri.toString()
//            resultView.setImageURI(Crop.getOutput(result))
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).message, Toast.LENGTH_SHORT).show()
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
        employeeViewModel.let {
            if(context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true){
                it!!.uploadEmployeeImage(employeeModel?.employeeProfileId!!)
            }
        }

        employeeViewModel?.getErrorMutableLiveData()?.observe(this, Observer {
            it.let {
                AndroidUtil.showToast(currActivity,it.message,true)
            }
        })

        employeeViewModel?.getImageLiveData()?.observe(this, Observer {
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
        }else if(!binding!!.rbMale.isChecked && !binding!!.rbFemale.isChecked){
            AndroidUtil.showToast(currActivity,"Employee Gender is required",true)
            isValid = false
        }else if(binding!!.etEmailAddress.text.toString().isEmpty()){
            AndroidUtil.showToast(currActivity,"email is required",true)
            isValid = false
        }else if(binding!!.etMobileNumber.text.toString().isEmpty()){
            AndroidUtil.showToast(currActivity,"Mobile number is required",true)
            isValid = false
        }

        return isValid
    }

    private fun updateStudentData(){
        model  = EmployeeList()
        model!!.employeeProfileId = employeeModel?.employeeProfileId!!

        model!!.firstName = etFirstName.text.toString()
        model!!.lastName = etLastName.text.toString()
        var gender = ""
        if(binding!!.rbFemale.isChecked){
            gender = "Female"
        }else{
            gender = "Male"
        }
        model!!.gender = gender
        model!!.email = binding!!.etEmailAddress.text.toString()
        model!!.phonePrimary = binding!!.etMobileNumber.text.toString()
        model!!.department = binding!!.etDepartment.text.toString()
        model!!.designation = binding!!.etDesignation.text.toString()

        model!!.address.addressLine1 = binding!!.etHouseNumber.text.toString()
        model!!.address.addressLine2 = binding!!.etStreet.text.toString()
        model!!.address.city = binding!!.etCity.text.toString()
        model!!.address.state = state
        model!!.address.district = binding!!.etDistrict.text.toString()
        model!!.address.postalcode = binding!!.etPostal.text.toString()

        employeeViewModel.let {
            if(context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true){
                it!!.updateEmployeeProfile(model!!)
            }
        }

        observer()
    }

    private fun observer(){
        employeeViewModel?.getIsLoading()?.observe(this, Observer {
            showProgressIndicator(it)
        })

        employeeViewModel?.getErrorMutableLiveData()?.observe(this, Observer {
            it.let {
                AndroidUtil.showToast(currActivity,it.message,true)
            }
        })

        employeeViewModel?.getSuccessLiveData()?.observe(this, Observer {
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
                employeeList.set(position,model!!)
        }
        ViewEmployeeProfileActivity.open(currActivity,employeeList,position,securityList)
    }


}
