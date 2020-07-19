package com.akshar.one.view.attendance

import android.app.Activity
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
import com.akshar.one.database.entity.AttendanceCategoryEntity
import com.akshar.one.database.entity.ClassRoomEntity
import com.akshar.one.databinding.ClassroomBottomSheetDialogFragmentBinding
import com.akshar.one.view.activity.MainActivity
import com.akshar.one.view.attendance.adapters.ClassRoomAdapter
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.attendance.AttendanceClassRoomViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AttendanceClassRoomBottomSheetDialog() : BottomSheetDialogFragment(), AttendanceBottomSheetDialogListener {

    private var classroomBottomSheetDialogFragmentBinding: ClassroomBottomSheetDialogFragmentBinding? =
        null
    private var attendanceClassRoomViewModel: AttendanceClassRoomViewModel? = null
    private var classRoomEntityList: List<ClassRoomEntity>? = null
    private lateinit var currActivity : Activity
    private var courseAdapter: ClassRoomAdapter? = null
    private var mainActivity: MainActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        classroomBottomSheetDialogFragmentBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.classroom_bottom_sheet_dialog_fragment, container, false
        )
        return classroomBottomSheetDialogFragmentBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        classroomBottomSheetDialogFragmentBinding?.imgClose?.setOnClickListener { dismiss() }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.application?.let {
            attendanceClassRoomViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(AttendanceClassRoomViewModel::class.java)
        }
        classroomBottomSheetDialogFragmentBinding?.attendanceClassRoomViewModel =
            attendanceClassRoomViewModel

        courseAdapter = attendanceClassRoomViewModel?.let { ClassRoomAdapter(it) }
        classroomBottomSheetDialogFragmentBinding?.recyclerView?.adapter = courseAdapter

        observers()
        fetchClassRooms()
    }

    private fun observers() {

        attendanceClassRoomViewModel?.getClassRoomListMutableLiveData()?.observe(this, Observer {
            it?.let {
                courseAdapter?.setClassRoomList(it,mainActivity as Activity)
            }
        })
    }

    private fun fetchClassRooms() {
        attendanceClassRoomViewModel?.getClassRoomListMutableLiveData()?.value = classRoomEntityList

    }

    companion object {
        @JvmStatic
        fun newInstance(classRoomEntityList: List<ClassRoomEntity>) =
            AttendanceClassRoomBottomSheetDialog().apply {
                this.classRoomEntityList = classRoomEntityList
            }
    }

    override fun onAttendanceCategorySelectedAction(
        classRoomEntity: ClassRoomEntity?,
        attendanceCategoryEntity: AttendanceCategoryEntity?
    ) {
        val attendanceEntryFragment = AttendanceEntryFragment.newInstance()
        mainActivity?.replaceFragment(attendanceEntryFragment, AttendanceEntryFragment::class.java.simpleName, true)
        dismiss()
    }
}