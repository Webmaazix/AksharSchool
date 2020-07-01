package com.akshar.one.view.attendance.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.database.entity.AttendanceCategoryEntity
import com.akshar.one.database.entity.ClassRoomEntity
import com.akshar.one.databinding.AttendanceClassroomCellLayoutBinding
import com.akshar.one.view.attendance.AttendanceBottomSheetDialogListener
import com.akshar.one.view.attendance.AttendanceCategoryListener
import com.akshar.one.viewmodels.attendance.AttendanceClassRoomViewModel

class ClassRoomAdapter(
    private val attendanceClassRoomViewModel: AttendanceClassRoomViewModel,
    private val attendanceBottomSheetDialogListener: AttendanceBottomSheetDialogListener
) :
    RecyclerView.Adapter<ClassRoomAdapter.Holder>() {

    private var classRoomList: List<ClassRoomEntity>? = null

    fun setClassRoomList(classRoomList: List<ClassRoomEntity>?) {
        classRoomList?.let {
            this.classRoomList = it
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val attendanceClassroomCellLayoutBinding =
            DataBindingUtil.inflate<AttendanceClassroomCellLayoutBinding>(
                LayoutInflater.from(parent.context),
                R.layout.attendance_classroom_cell_layout,
                parent,
                false
            )
        return Holder(
            attendanceClassroomCellLayoutBinding,
            attendanceClassRoomViewModel,
            attendanceBottomSheetDialogListener
        )
    }

    override fun getItemCount(): Int {
        return classRoomList?.size ?: 0
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(position)
    }

    class Holder(
        private val attendanceClassroomCellLayoutBinding: AttendanceClassroomCellLayoutBinding,
        private val attendanceClassRoomViewModel: AttendanceClassRoomViewModel,
        private val attendanceBottomSheetDialogListener: AttendanceBottomSheetDialogListener
    ) :
        RecyclerView.ViewHolder(attendanceClassroomCellLayoutBinding.root),
        AttendanceCategoryListener, AdapterView.OnItemSelectedListener {

        var attendanceCategoryAdapter: AttendanceCategoryAdapter? = null
        var classRoomEntity: ClassRoomEntity? = null
        var mSpinnerInitialized: Boolean = false

        fun bind(position: Int) {
            classRoomEntity = attendanceClassRoomViewModel.getClassRoomAt(position)
            attendanceClassroomCellLayoutBinding.attendanceClassRoomViewModel =
                attendanceClassRoomViewModel
            attendanceClassroomCellLayoutBinding.position = position
            setAttendanceCategoryAdapter(position, attendanceClassRoomViewModel)
            attendanceClassroomCellLayoutBinding.relRow.setOnClickListener {
                attendanceClassRoomViewModel.getAttendanceCategories(position, this)
            }
            attendanceClassroomCellLayoutBinding.executePendingBindings()
        }

        private fun setAttendanceCategoryAdapter(
            position: Int,
            attendanceClassRoomViewModel: AttendanceClassRoomViewModel
        ) {
            attendanceCategoryAdapter = AttendanceCategoryAdapter(attendanceClassRoomViewModel)
            attendanceClassroomCellLayoutBinding.spinner.adapter = attendanceCategoryAdapter
            attendanceClassroomCellLayoutBinding.spinner.onItemSelectedListener = this
            attendanceClassRoomViewModel.getAttendanceCategoriesFromDB(position, this)
        }

        override fun updateAttendanceCategory(attendanceCategoryEntityList: List<AttendanceCategoryEntity>?) {
            attendanceCategoryEntityList?.takeIf { !it.isNullOrEmpty() }?.apply {
                attendanceClassroomCellLayoutBinding.spinner.visibility = View.VISIBLE
                attendanceCategoryAdapter?.setAttendanceCategoryEntityList(this)
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            if (!mSpinnerInitialized) {
                mSpinnerInitialized = true
                return
            }

            val attendanceCategory = attendanceCategoryAdapter?.getItem(position)
            attendanceBottomSheetDialogListener.onAttendanceCategorySelectedAction(classRoomEntity,attendanceCategory)
        }

    }
}