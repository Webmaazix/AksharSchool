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
import com.akshar.one.databinding.ClassroomBottomSheetDialogFragmentBinding

import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.attendance.AttendanceClassRoomViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AttendanceClassRoomBottomSheetDialog() : BottomSheetDialogFragment() {

    private var classroomBottomSheetDialogFragmentBinding : ClassroomBottomSheetDialogFragmentBinding? = null
    private var attendanceClassRoomViewModel: AttendanceClassRoomViewModel? = null
    private var classRoomEntityList: List<ClassRoomEntity>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        classroomBottomSheetDialogFragmentBinding = DataBindingUtil.inflate(inflater,
            R.layout.classroom_bottom_sheet_dialog_fragment, container, false)
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
        classroomBottomSheetDialogFragmentBinding?.attendanceClassRoomViewModel = attendanceClassRoomViewModel

        observers()
        fetchClassRooms()
    }

    private fun observers() {

        attendanceClassRoomViewModel?.getClassRoomListMutableLiveData()?.observe(this, Observer {
            it?.let {
                attendanceClassRoomViewModel?.setClassRoomInAdapter(it)
            }
        })
    }

    private fun fetchClassRooms() {
        attendanceClassRoomViewModel?.getClassRoomListMutableLiveData()?.value = classRoomEntityList

    }

    companion object {
        @JvmStatic
        fun newInstance(classRoomEntityList: List<ClassRoomEntity>) = AttendanceClassRoomBottomSheetDialog().apply {
            this.classRoomEntityList = classRoomEntityList
        }
    }
}