package com.akshar.one.view.attendance.student.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.database.entity.ShiftEntity
import com.akshar.one.view.attendance.AttendanceCategoryListener
import kotlinx.android.synthetic.main.select_section_cell.view.*

class AttendanceCategoryAdapter(private val context:Context, private val shiftEntityList: List<ShiftEntity>?, private val attendanceCategoryListener: AttendanceCategoryListener?) : RecyclerView.Adapter<AttendanceCategoryAdapter.Holder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.select_section_cell,parent,false)
        return Holder(
            view,
            attendanceCategoryListener
        )
    }

    override fun getItemCount(): Int = shiftEntityList?.size ?: 0

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(shiftEntityList?.get(position))
    }

    class Holder(itemView:View, private val attendanceCategoryListener: AttendanceCategoryListener?) : RecyclerView.ViewHolder(itemView) {

        fun bind(shiftEntity: ShiftEntity?) {
            itemView.txtSection.text = shiftEntity?.name
            itemView.txtSection.setOnClickListener {
                shiftEntity?.let { categoryEntity ->
                    attendanceCategoryListener?.onAttendanceCategorySelected(
                        categoryEntity
                    )
                }
            }
        }

    }


}