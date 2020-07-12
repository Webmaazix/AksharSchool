package com.akshar.one.view.attendance.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.database.entity.CourseEntity
import com.akshar.one.databinding.AttendanceCourseCellLayoutBinding
import com.akshar.one.viewmodels.attendance.AttendanceCourseViewModel

class CourseAdapter(private val attendanceCourseViewModel: AttendanceCourseViewModel): RecyclerView.Adapter<CourseAdapter.Holder>() {

    private var courseList: List<CourseEntity>? = null

    fun setCourseList(courseList: List<CourseEntity>?){
        courseList?.let {
            this.courseList = it
            notifyDataSetChanged()
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val attendanceCourseCellLayoutBinding =
            DataBindingUtil.inflate<AttendanceCourseCellLayoutBinding>(LayoutInflater.from(parent.context), R.layout.attendance_course_cell_layout, parent,  false)
        return Holder(attendanceCourseCellLayoutBinding)
    }

    override fun getItemCount(): Int {
        return courseList?.size ?: 0
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(attendanceCourseViewModel, position)
    }

    class Holder(private val attendanceCourseCellLayoutBinding: AttendanceCourseCellLayoutBinding ) : RecyclerView.ViewHolder(attendanceCourseCellLayoutBinding.root) {

        fun bind(attendanceCourseViewModel: AttendanceCourseViewModel, position: Int) {
            attendanceCourseCellLayoutBinding.attendanceCourseViewModel = attendanceCourseViewModel
            attendanceCourseCellLayoutBinding.position = position
            attendanceCourseCellLayoutBinding.executePendingBindings()
        }

    }
}