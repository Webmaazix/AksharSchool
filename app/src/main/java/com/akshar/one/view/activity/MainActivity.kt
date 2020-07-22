package com.akshar.one.view.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.akshar.one.R
import com.akshar.one.view.assignhomework.AssignHomeworkFragment
import com.akshar.one.view.attendance.AttendanceFragment
import com.akshar.one.view.examschedule.ScheduledExamList
import com.akshar.one.view.feeandpayments.StudentListForFeesFragment
import com.akshar.one.manager.SessionManager
import com.akshar.one.util.AndroidUtil
import com.akshar.one.view.noticeboard.NoticeboardActivity
import com.akshar.one.view.timetable.TimeTableActivity
import com.akshar.one.util.CheckPermission
import com.akshar.one.view.attendance.AttendanceEntryFragment
import com.akshar.one.view.home.DashboardActivity
import com.akshar.one.view.studentprofile.StudentListFragment
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.main.MainViewModel
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_toolbar.*
import kotlinx.android.synthetic.main.nav_header_main.view.*

class MainActivity : BaseActivity(),  NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {


    var drawerLayout: DrawerLayout? = null
    var mSlideState = false
    private var currActivity : Activity = this
    private var mainViewModel: MainViewModel? = null

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
                            nav_view.getHeaderView(0).tvUserName.text = loginModel.appsList!![0].username
                        }
                    }
                }
                nav_view.getHeaderView(0).imgUserProfile.setOnClickListener{
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

        mainViewModel = ViewModelProvider(
            ViewModelStore(),
            ViewModelFactory(application)
        ).get(MainViewModel::class.java)

        observers()
        fetchCourses()
    }

    private fun fetchCourses() {
        mainViewModel?.getClassRoomDropdownService()
    }

    private fun observers() {
        mainViewModel?.getIsLoading()?.observe(this, Observer {
            showProgressIndicator(it)
        })

        mainViewModel?.getErrorMutableLiveData()?.observe(this, Observer {
            it?.let {
                AndroidUtil.showMessageDialog(this, it.message)
            }
        })

//        drawerLayout?.setDrawerListener(toggle)
        nav_view.itemIconTintList = null;
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

    fun setToolbarTitle(title: String?) {
        txtToolbarTitle.text = title
    }

    fun setToolbarBackground(boolean: Boolean){
        toolbar.background = if(boolean) resources.getDrawable(R.drawable.yellow_top_square,null) else resources.getDrawable(R.color.light_yellow,null)
    }

    private fun showProgressIndicator(isLoading: Boolean?) {
//        linProgressIndicator.visibility = if (isLoading == true) View.VISIBLE else View.GONE
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

                replaceFragment(AttendanceEntryFragment.newInstance(), AttendanceEntryFragment::javaClass.name, true)

            }
            R.id.nav_settings -> {
            }
            R.id.nav_assign_homework -> {
                toolbar.background = currActivity.resources.getDrawable(R.drawable.yellow_top_square)

                replaceFragment(AssignHomeworkFragment.newInstance(), AssignHomeworkFragment::javaClass.name, false)

            }
            R.id.nav_notice_board -> {
                NoticeboardActivity.open(currActivity)
            }
            R.id.nav_marks_entry -> {
            }
            R.id.nav_exam_schedule -> {
                ScheduledExamList.open(currActivity)
            }
            R.id.nav_fees_payment -> {
                toolbar.background = currActivity.resources.getDrawable(R.drawable.yellow_top_square)
                replaceFragment(StudentListForFeesFragment.newInstance(), StudentListForFeesFragment::javaClass.name, false)
            }
            R.id.nav_message_center -> {
            }

        }
        drawerLayout!!.closeDrawer(GravityCompat.START);
        return true

    }

    fun openCameraDialog() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery")
        val builder = AlertDialog.Builder(currActivity)
        builder.setTitle("Add Profile Pic!")
        builder.setItems(options) { dialogInterface, i ->
            if (options[i].toString().equals("Take Photo", ignoreCase = true)) {
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (takePictureIntent.resolveActivity(packageManager) != null) {
                    startActivityForResult(takePictureIntent, 101)
                }
            } else if (options[i].toString().equals("Choose from Gallery", ignoreCase = true)) {
                val intent = Intent(Intent.ACTION_PICK)
                // Sets the type as image/*. This ensures only components of type image are selected
                intent.type = "image/*"
                //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
                val mimeTypes = arrayOf("image/jpeg", "image/png")
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                // Launching the Intent
                startActivityForResult(intent, 102)
            } else if (options[i] == "Cancel") {
                dialogInterface.dismiss()
            }
        }
        builder.show()
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
}
