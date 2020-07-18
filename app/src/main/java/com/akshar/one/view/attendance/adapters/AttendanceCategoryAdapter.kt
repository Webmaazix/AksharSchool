package com.akshar.one.view.attendance.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.database.entity.AttendanceCategoryEntity
import com.akshar.one.databinding.AttendanceCourseCellLayoutBinding
import com.akshar.one.view.attendance.AttendanceCategoryListener
import com.akshar.one.viewmodels.attendance.AttendanceClassRoomViewModel
import com.akshar.one.viewmodels.attendance.AttendanceCourseViewModel
import kotlinx.android.synthetic.main.attendance_category_drop_down_cell.view.*
import kotlinx.android.synthetic.main.select_section_cell.view.*

class AttendanceCategoryAdapter(private val context:Context, private val attendanceCategoryEntityList: List<AttendanceCategoryEntity>?, private val attendanceCategoryListener: AttendanceCategoryListener?) : RecyclerView.Adapter<AttendanceCategoryAdapter.Holder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.select_section_cell,parent,false)
        return Holder(view, attendanceCategoryListener)
    }

    override fun getItemCount(): Int = attendanceCategoryEntityList?.size ?: 0

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(attendanceCategoryEntityList?.get(position))
    }

    class Holder(itemView:View, private val attendanceCategoryListener: AttendanceCategoryListener?) : RecyclerView.ViewHolder(itemView) {

        fun bind(attendanceCategoryEntity: AttendanceCategoryEntity?) {
            itemView.txtSection.text = attendanceCategoryEntity?.category
            itemView.txtSection.setOnClickListener {
                attendanceCategoryEntity?.let { categoryEntity ->
                    attendanceCategoryListener?.onAttendanceCategorySelected(
                        categoryEntity
                    )
                }
            }
        }

    }


}