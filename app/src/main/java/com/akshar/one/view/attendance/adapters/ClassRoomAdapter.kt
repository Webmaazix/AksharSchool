package com.akshar.one.view.attendance.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.attandance.AttandanceEntryActivity
import com.akshar.one.database.entity.ClassRoomEntity
import com.akshar.one.databinding.AttendanceClassroomCellLayoutBinding
import com.akshar.one.viewmodels.attendance.AttendanceClassRoomViewModel

class ClassRoomAdapter(private val attendanceClassRoomViewModel: AttendanceClassRoomViewModel): RecyclerView.Adapter<ClassRoomAdapter.Holder>() {

    private var classRoomList: List<ClassRoomEntity>? = null
    private lateinit var currActivity : Activity

    fun setClassRoomList(classRoomList: List<ClassRoomEntity>?,currActivity : Activity){
        classRoomList?.let {
            this.classRoomList = it
            this.currActivity = currActivity
            notifyDataSetChanged()
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val attendanceClassroomCellLayoutBinding =
            DataBindingUtil.inflate<AttendanceClassroomCellLayoutBinding>(LayoutInflater.from(parent.context), R.layout.attendance_classroom_cell_layout, parent,  false)
        return Holder(attendanceClassroomCellLayoutBinding)
    }

    override fun getItemCount(): Int {
        return classRoomList?.size ?: 0
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(attendanceClassRoomViewModel, position)
    }

    class Holder(private val attendanceClassroomCellLayoutBinding: AttendanceClassroomCellLayoutBinding ) : RecyclerView.ViewHolder(attendanceClassroomCellLayoutBinding.root) {

        fun bind(attendanceClassRoomViewModel: AttendanceClassRoomViewModel, position: Int) {
            attendanceClassroomCellLayoutBinding.attendanceClassRoomViewModel = attendanceClassRoomViewModel
            attendanceClassroomCellLayoutBinding.position = position
            attendanceClassroomCellLayoutBinding.executePendingBindings()
        }

    }
}