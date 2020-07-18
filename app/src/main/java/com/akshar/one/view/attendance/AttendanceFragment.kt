package com.akshar.one.view.attendance


import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.LinearLayoutManager

import com.akshar.one.R
import com.akshar.one.adapter.CourceListAdapter
import com.akshar.one.database.entity.CourseEntity
import com.akshar.one.databinding.FragmentAttendanceBinding
import com.akshar.one.util.AndroidUtil
import com.akshar.one.view.activity.MainActivity
import com.akshar.one.view.fragment.BaseFragment
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.attendance.AttendanceCourseViewModel

/**
 * A simple [Fragment] subclass.
 */
class AttendanceFragment : BaseFragment() {

    private var fragmentAttendanceBinding: FragmentAttendanceBinding? = null
    private var attendanceCourseViewModel : AttendanceCourseViewModel? = null
    private var mainActivity: MainActivity? = null
    private var courseList = ArrayList<CourseEntity>()
    private lateinit var courceAdapter : CourceListAdapter
    private lateinit var currActivity : Activity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentAttendanceBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_attendance, container, false)
        currActivity = activity!!
        return fragmentAttendanceBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        mainActivity?.setToolbarTitle(getString(R.string.attendance))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.application?.let {
            attendanceCourseViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(AttendanceCourseViewModel::class.java)
        }


        attendanceCourseViewModel?.let {
            if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                it.getCourses()
            }
        }
        setAdapter()

        observers()
       // fetchCourses()
    }

    private fun setAdapter(){
        fragmentAttendanceBinding!!.rvClasses.setHasFixedSize(true)
        fragmentAttendanceBinding!!.rvClasses.layoutManager = LinearLayoutManager(currActivity,LinearLayoutManager.VERTICAL,false)
        courceAdapter = CourceListAdapter(currActivity,courseList)
        fragmentAttendanceBinding!!.rvClasses.adapter = courceAdapter
    }

    private fun observers() {
        attendanceCourseViewModel?.getIsLoading()?.observe(this, Observer {
            showProgressIndicator(it)
        })
//
        attendanceCourseViewModel?.getCourseListMutableLiveData()?.observe(this, Observer {
            it?.let {
                courseList.clear()
                courseList.addAll(it)
                courceAdapter.notifyDataSetChanged()
            }
        })
        attendanceCourseViewModel?.getErrorMutableLiveData()?.observe(this, Observer {
            it?.let {
                AndroidUtil.showMessageDialog(context,it.message)
            }
        })

    }

    private fun showProgressIndicator(isLoading: Boolean?) {
      //  linProgressIndicator.visibility = if (isLoading == true) View.VISIBLE else View.GONE
    }

    private fun fetchCourses(){
        attendanceCourseViewModel?.getCourses()
    }

    companion object {
        @JvmStatic
        fun newInstance() = AttendanceFragment()
    }

}
