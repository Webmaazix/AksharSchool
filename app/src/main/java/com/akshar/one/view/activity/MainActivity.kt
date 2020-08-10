package com.akshar.one.view.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.akshar.one.R
import com.akshar.one.api.service.ApiInterface
import com.akshar.one.app.AksharSchoolApplication
import com.akshar.one.view.attendance.AttendanceFragment
import com.akshar.one.view.examschedule.ScheduledExamList
import com.akshar.one.view.feeandpayments.StudentListForFeesFragment
import com.akshar.one.manager.SessionManager
import com.akshar.one.util.AndroidUtil
import com.akshar.one.util.AppUtils
import com.akshar.one.view.noticeboard.NoticeboardActivity
import com.akshar.one.view.timetable.TimeTableActivity
import com.akshar.one.util.CheckPermission
import com.akshar.one.view.home.DashboardActivity
import com.akshar.one.view.marksentry.inputselection.ClassSectionSelectActivity
import com.akshar.one.view.messagecenter.MessageCenterFragment
import com.akshar.one.view.studentprofile.StudentListFragment
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.student.StudentViewModel
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_toolbar.*
import kotlinx.android.synthetic.main.main_toolbar.view.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class MainActivity : BaseActivity(),  NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {


    var drawerLayout: DrawerLayout? = null
    var mSlideState = false
    private var currActivity : Activity = this
    private var studentViewModel : StudentViewModel? = null
    private val RESULT_LOAD = 423
    private val REQUEST_CAMERA = 433
    private var file: Uri? = null
    private var profileFile: File? = null
    private var mSelectedImagePath: String = ""
    private var image = ""

    companion object {
        fun open(currActivity: Activity) {
            val intent = Intent(currActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            currActivity.startActivity(intent)
            currActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
            currActivity.finish()

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        nav_view.setNavigationItemSelectedListener(this)
        drawerLayout = findViewById(R.id.drawer_layout)
        currActivity.application?.let {
            studentViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(StudentViewModel::class.java)
        }

        val toggle = object : ActionBarDrawerToggle(
            this,
            drawerLayout,

            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        ) {

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                if(SessionManager.getLoginModel()!= null){
                    val loginModel = SessionManager.getLoginModel()
                    if(loginModel!!.appsList!= null){
                        if(loginModel.appsList!![0].username!= null){
                            if(loginModel.appsList[0].username!!.length <= 25){
                                nav_view.getHeaderView(0).tvUserName.text = loginModel.appsList[0].username
                            }else{
                                var subString = loginModel.appsList[0].username!!.substring(0,25)
                                nav_view.getHeaderView(0).tvUserName.text = "$subString..."

                            }
                        }
                    }
                }
                nav_view.getHeaderView(0).imgUserProfile.setOnClickListener{
                    drawerLayout?.closeDrawer(Gravity.LEFT);
                    if (CheckPermission.checkCameraPermission(currActivity))
                        openCameraDialog()
                    else
                        CheckPermission.requestCameraPermission(
                            currActivity,
                            CheckPermission.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS
                        )
                }
            }

            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)

            }


        }


        drawerLayout?.setDrawerListener(toggle)
        nav_view.itemIconTintList = null;
        nav_view.menu.getItem(0).isChecked = true;
        setListner()
        toolbar.background = currActivity.resources.getDrawable(R.drawable.white_square_nopadding_shape)
        getWindow().statusBarColor = Color.WHITE;
        txtToolbarTitle.text = currActivity.resources.getString(R.string.home)
        replaceFragment(DashboardActivity.newInstance(), DashboardActivity::javaClass.name, false)
    }

    private fun setListner() {
        imgMenu.setOnClickListener(this)
    }

    fun isLastAddedFragment(fragmentName: String?): Boolean {
        val index = supportFragmentManager.backStackEntryCount - 1
        if (index >= 0) {
            val backEntry =
                supportFragmentManager.getBackStackEntryAt(index)
            backEntry.let {
                val tag = it.name
                return tag != null && tag.equals(fragmentName, ignoreCase = true)
            }
        }
        return false
    }

    fun addFragment(fragment: Fragment?, name: String?, addToBackStack: Boolean) {
        if (!isLastAddedFragment(name)) {
            val fragmentTransaction =
                supportFragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.container, fragment!!, name)
            if (addToBackStack) {
                fragmentTransaction.addToBackStack(name)
            }
            fragmentTransaction.commitAllowingStateLoss()
        }
    }

    fun replaceFragment(fragment: Fragment?, name: String?, addToBackStack: Boolean) {
        if (!isLastAddedFragment(name)) {
            val fragmentTransaction =
                supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container, fragment!!, name)
            if (addToBackStack) {
                fragmentTransaction.addToBackStack(name)
            }
            fragmentTransaction.commit()
        }
    }

    fun getLastFragmentFromBackStack(): Fragment? {
        var lastFragment: Fragment? = null
        val backStackCount = supportFragmentManager.backStackEntryCount
        if (backStackCount > 0) {
            val backStackEntry =
                supportFragmentManager.getBackStackEntryAt(backStackCount - 1)
            if (backStackEntry.name != null && backStackEntry.name?.isNotEmpty() == true) {
                lastFragment =
                    supportFragmentManager.findFragmentByTag(backStackEntry.name)
            }
        }
        return lastFragment
    }

    fun setToolbarTitle(title: String) {
        txtToolbarTitle.text = title
    }

    private fun openCamera(){

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_logout -> {
            }
            R.id.nav_dashboard -> {
                txtToolbarTitle.text = currActivity.resources.getString(R.string.home)
                toolbar.background = currActivity.resources.getDrawable(R.drawable.white_square_nopadding_shape)
                replaceFragment(DashboardActivity.newInstance(), DashboardActivity::javaClass.name, false)
            }
            R.id.nav_time_table -> {
                TimeTableActivity.open(currActivity)
             //   replaceFragment(DashboardActivity.newInstance(), DashboardActivity::javaClass.name, true)

            }
            R.id.nav_student_profile -> {
                toolbar.background = currActivity.resources.getDrawable(R.drawable.yellow_top_square)

                replaceFragment(StudentListFragment.newInstance(), StudentListFragment::javaClass.name, false)

            }

            R.id.nav_attandance_entry -> {
                toolbar.background = currActivity.resources.getDrawable(R.drawable.yellow_top_square)

                replaceFragment(AttendanceFragment.newInstance(), AttendanceFragment::javaClass.name, false)


            }
            R.id.nav_settings -> {
            }
            R.id.nav_assign_homework -> {
                toolbar.background = currActivity.resources.getDrawable(R.drawable.yellow_top_square)

               // replaceFragment(AssignHomeworkFragment.newInstance(), AssignHomeworkFragment::javaClass.name, false)
            }
            R.id.nav_notice_board -> {
                NoticeboardActivity.open(currActivity)
            }
            R.id.nav_marks_entry -> {
               ClassSectionSelectActivity.open(currActivity)
            }
            R.id.nav_exam_schedule -> {
                ScheduledExamList.open(currActivity)
            }
            R.id.nav_fees_payment -> {
                toolbar.background = currActivity.resources.getDrawable(R.drawable.yellow_top_square)
                replaceFragment(StudentListForFeesFragment.newInstance(), StudentListForFeesFragment::javaClass.name, false)
            }
            R.id.nav_message_center -> {
                toolbar.txtToolbarTitle.text = "Message Center"
                replaceFragment(MessageCenterFragment.newInstance(), MessageCenterFragment::javaClass.name, false)

            }

        }
        drawerLayout!!.closeDrawer(GravityCompat.START);
        return true

    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {

            R.id.imgMenu -> {
                if (!mSlideState) {
                    drawerLayout?.openDrawer(Gravity.LEFT);
                } else {
                    drawerLayout?.closeDrawer(Gravity.LEFT);
                }
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
            CropImage.activity(FilePath).setInitialCropWindowPaddingRatio(0f)
                .start(this@MainActivity)
        } else if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            CropImage.activity(file).setInitialCropWindowPaddingRatio(0f)
                .start(this@MainActivity)
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.getUri()
                profileFile = File(resultUri.getPath()!!)
                image = ""
                mSelectedImagePath = resultUri.toString()
                Glide.with(currActivity).load(mSelectedImagePath).into(nav_view.getHeaderView(0).imgUserProfile)
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
            if(AksharSchoolApplication.context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true){
                val loginModel = SessionManager.getLoginModel()
                if (loginModel != null) {
                    it!!.uploadImage(loginModel.appsList?.get(0)!!.id!!)
                }
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
        image_upload_call.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("system",t.message)
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Log.d("success",response.toString())
            }


        })
    }

}
