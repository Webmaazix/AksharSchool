package com.akshar.one.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.view.attandance.AttandanceEntryActivity
import com.akshar.one.database.entity.ClassRoomEntity
import com.akshar.one.databinding.AttendanceClassroomCellLayoutBinding

import java.util.ArrayList

class ClassSectionAdapter(private val mContext: Context, private val classRoomList: ArrayList<ClassRoomEntity>?) :
    RecyclerView.Adapter<ClassSectionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.attendance_classroom_cell_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val classRoomList = classRoomList?.get(position)
        holder.binding.txtTitle.text = classRoomList!!.classroomName

        holder.binding.rlMain.setOnClickListener {
            AttandanceEntryActivity.open(mContext as Activity)
        }


    }

    override fun getItemCount(): Int {
        return classRoomList!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: AttendanceClassroomCellLayoutBinding = AttendanceClassroomCellLayoutBinding.bind(itemView)

    }
}
