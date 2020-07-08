package com.akshar.one.view.activity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.akshar.one.R
import com.akshar.one.util.AndroidUtil
import com.akshar.one.view.attendance.AttendanceCourseFragment
import com.akshar.one.view.attendance.AttendanceEntryFragment
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.main.MainViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.main_toolbar.*

class MainActivity : BaseActivity() {

    private var mainViewModel: MainViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        mainViewModel?.getCourseListMutableLiveData()?.observe(this, Observer {
            replaceFragment(
                AttendanceEntryFragment.newInstance(),
                AttendanceCourseFragment::javaClass.name,
                true
            )
        })

//        mainViewModel?.getDegreeListMutableLiveData()?.observe(this, Observer {
//            replaceFragment(
//                AttendanceEntryFragment.newInstance(),
//                AttendanceCourseFragment::javaClass.name,
//                true
//            )
//        })
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

    private fun showProgressIndicator(isLoading: Boolean?) {
        linProgressIndicator.visibility = if (isLoading == true) View.VISIBLE else View.GONE
    }
}
