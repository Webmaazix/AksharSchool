package com.akshar.one.view.studentprofile

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
import com.akshar.one.databinding.DialogSelectClassAndSectionBinding
import com.akshar.one.imagecropper.Crop
import com.akshar.one.manager.SessionManager
import com.akshar.one.model.ClassDropDownModel
import com.akshar.one.model.SectionList
import com.akshar.one.util.*
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
    lateinit var classDropDownAdapter : ClassDropDownAdapter
    private var classDropdownList = ArrayList<ClassDropDownModel>()
    private var dialogSelectClassSectionBinding : DialogSelectClassAndSectionBinding? = null
    private var dialog : Dialog? = null
    private var classRoomId : Int = 0;
     private var  lockAspectRatio : Boolean= false
     private var  setBitmapMaxWidthHeight : Boolean= false
    private var ASPECT_RATIO_X : Int = 16
    private var ASPECT_RATIO_Y = 9
    private var bitmapMaxWidth = 1000
    private var bitmapMaxHeight = 1000
    private var IMAGE_COMPRESSION = 80
    private var securityList : ArrayList<String>? = null
    private var timeTableViewModel : TimeTableViewModel? = null
    private var model : StudentListModel? =null
    var bloodGroupList = arrayOf("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "Ab-")
    var states = arrayOf("Andra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Goa", "Gujarat", "Haryana","Himachal Pradesh",
        "Jammu and Kashmir","Jharkhand","Karnataka","Kerala","Madya Pradesh","Maharashtra","Manipur","Meghalaya","Mizoram","Nagaland","Orissa",
        "Punjab","Rajasthan","Sikkim","Tamil Nadu","Telagana","Tripura","Uttaranchal","Uttar Pradesh","West Bengal")


    companion object {
        fun open(currActivity: Activity, studentModel : StudentListModel,position : Int,studentList : ArrayList<StudentListModel>?,securityList :
        ArrayList<String>) {
            val intent = Intent(currActivity, EditStudentProfileActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("studentModel",studentModel)
            intent.putExtra("studentList",studentList)
            intent.putExtra("position",position)
            intent.putExtra("securityList",securityList)
            currActivity.startActivity(intent)
            currActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(currActivity,R.layout.activity_edit_student_profile)
        if(SessionManager.getLoginRole()?.appName.equals("SmartParent",true)) {

        }else{
            studentList = intent.getSerializableExtra("studentList") as ArrayList<StudentListModel>

        }
        studentModel = intent.getSerializableExtra("studentModel") as StudentListModel
        securityList = intent.getSerializableExtra("securityList") as ArrayList<String>
        position = intent.getIntExtra("position",0)
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

            if(studentModel!!.imageUrl!= null || studentModel!!.imageUrl!=""){
                AppUtils.loadImageCrop(
                    studentModel!!.imageUrl,
                    binding!!.imgUserImage,
                    R.drawable.circle_default_pic,
                    100,
                    100
                )
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
                    if(studentModel!!.studentContact.address.state!= null){
                        state = studentModel!!.studentContact.address.state!!
                    }else{
                        state = ""
                    }

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

        currActivity.application?.let {
            timeTableViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(TimeTableViewModel::class.java)
        }
        timeTableViewModel?.let {
            if(context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true){
                it.getClassRoomDropdown()
            }
        }

        observer()


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
        binding!!.rlClassSection.setOnClickListener(this)
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
            R.id.rlClassSection ->{
                openDialog()
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
        }else if(!binding!!.rbMale.isChecked && !binding!!.rbFemale.isChecked){
            AndroidUtil.showToast(currActivity,"Student Gender is required",true)
            isValid = false
        }else if(binding!!.etParentName.text.toString().isEmpty()){
            AndroidUtil.showToast(currActivity,"Parent name is required",true)
            isValid = false
        }else if(binding!!.etMobileNumber.text.toString().isEmpty()){
            AndroidUtil.showToast(currActivity,"Parent Mobile number is required",true)
            isValid = false
        }

        return isValid
    }

    private fun updateStudentData(){
         model  = StudentListModel()
        model!!.studentProfileId = studentModel?.studentProfileId!!
        if(classRoomId == 0){
            model!!.classroomId = studentModel?.classroomId
        }else{
            model!!.classroomId = classRoomId
        }
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

        timeTableViewModel?.getClassRoomLiveData()?.observe(this, Observer {
            classDropdownList.clear()
            classDropdownList.addAll(it)
        })

    }

    private fun showProgressIndicator(isLoading: Boolean?) {
        binding!!.linProgressIndicator.visibility = if (isLoading == true) View.VISIBLE else View.GONE
    }

    override fun onBackPressed() {
        if(SessionManager.getLoginRole()?.appName.equals("SmartParent",true)){
            ViewStudentProfileActivity.open(currActivity,null,position,securityList)

        }else{
            if(model != null){
                studentList.set(position,model!!)
            }

            ViewStudentProfileActivity.open(currActivity,studentList,position,securityList)
        }

    }

    private fun openDialog(){
        dialog = Dialog(currActivity as Context)
        dialogSelectClassSectionBinding = DataBindingUtil.inflate(
            LayoutInflater.from(currActivity),
            R.layout.dialog_select_class_and_section,null,false)
        dialog!!.setContentView(dialogSelectClassSectionBinding!!.getRoot())
        Objects.requireNonNull<Window>(dialog!!.getWindow())
            .setBackgroundDrawableResource(android.R.color.transparent)

        if(classDropdownList.size > 0){
            dialogSelectClassSectionBinding!!.rlNotFound.visibility = View.GONE
            dialogSelectClassSectionBinding!!.rlClassesDropdown.visibility = View.VISIBLE
            dialogSelectClassSectionBinding!!.rlClassesDropdown.setHasFixedSize(true)
            dialogSelectClassSectionBinding!!.rlClassesDropdown.layoutManager = LinearLayoutManager(currActivity,
                LinearLayoutManager.VERTICAL,false)
            ClassDropDownAdapter.selectedChild = -1
            ClassDropDownAdapter.clickParent =-1;

            classDropDownAdapter = ClassDropDownAdapter(currActivity,classDropdownList,null,object :
                ClassDropDownAdapter.SectionSelection {
                override fun selectionCallback(parent: Int, child: Int) {
                    classDropDownAdapter.notifyDataSetChanged()
                }

            })
            dialogSelectClassSectionBinding!!.rlClassesDropdown.adapter = classDropDownAdapter
            classDropDownAdapter.notifyDataSetChanged()
        }else{
            dialogSelectClassSectionBinding!!.rlNotFound.visibility = View.VISIBLE
            dialogSelectClassSectionBinding!!.rlClassesDropdown.visibility = View.GONE
        }


        dialogSelectClassSectionBinding!!.imgCancel.setOnClickListener {
            dialog!!.dismiss()
        }

        dialog!!.setCancelable(true)
        dialog!!.show()
    }

    fun sectionClicked(data : ClassDropDownModel, model : SectionList){
        classRoomId = model.classroomId
        val className = data.courseName+" "+model.classroomName
        binding!!.tvSelectClassSection.text = className
        dialog!!.dismiss()
    }


}
