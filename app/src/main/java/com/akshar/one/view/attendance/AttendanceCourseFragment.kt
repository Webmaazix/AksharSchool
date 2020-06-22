package com.akshar.one.view.attendance

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.akshar.one.R
import com.akshar.one.database.entity.ClassRoomEntity
import com.akshar.one.databinding.FragmentAttendanceCourseBinding
import com.akshar.one.util.AndroidUtil
import com.akshar.one.view.activity.MainActivity
import com.akshar.one.view.fragment.BaseFragment
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.attendance.AttendanceCourseViewModel
import kotlinx.android.synthetic.main.fragment_login.*

class AttendanceCourseFragment : BaseFragment() {

    private var fragmentAttendanceCourseBinding: FragmentAttendanceCourseBinding? = null
    private var attendanceCourseViewModel : AttendanceCourseViewModel? = null
    private var mainActivity: MainActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentAttendanceCourseBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_attendance_course, container, false)
        return fragmentAttendanceCourseBinding?.root
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
        fragmentAttendanceCourseBinding?.attendanceCourseViewModel = attendanceCourseViewModel
        observers()
        fetchCourses()
    }

    private fun observers() {
        attendanceCourseViewModel?.getIsLoading()?.observe(this, Observer {
            showProgressIndicator(it)
        })

        attendanceCourseViewModel?.getCourseListMutableLiveData()?.observe(this, Observer {
            it?.let { courseList ->
                attendanceCourseViewModel?.setCourseInAdapter(courseList)
            }
        })

        attendanceCourseViewModel?.getClassRoomListMutableLiveData()?.observe(this, Observer {
            it?.let {
                openBottomSheetSelectionDialog(it)
            }
        })
        attendanceCourseViewModel?.getErrorMutableLiveData()?.observe(this, Observer {
            it?.let {
                AndroidUtil.showMessageDialog(context,it.message)
            }
        })
    }

    private fun openBottomSheetSelectionDialog(classRoomEntityList: List<ClassRoomEntity>) {

        val attendanceClassRoomBottomSheetDialog = AttendanceClassRoomBottomSheetDialog.newInstance(classRoomEntityList)
        mainActivity?.supportFragmentManager?.let { attendanceClassRoomBottomSheetDialog.show(it, AttendanceClassRoomBottomSheetDialog::class.java.name) }
    }

    private fun showProgressIndicator(isLoading: Boolean?) {
        linProgressIndicator.visibility = if (isLoading == true) View.VISIBLE else View.GONE
    }

    private fun fetchCourses(){
        attendanceCourseViewModel?.getCourses()
    }

    companion object {
        @JvmStatic
        fun newInstance() = AttendanceCourseFragment()
    }
}