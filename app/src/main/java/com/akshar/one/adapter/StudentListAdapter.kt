package com.akshar.one.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.attandance.AttandanceEntryActivity
import com.akshar.one.database.entity.ClassRoomEntity
import com.akshar.one.databinding.AttendanceClassroomCellLayoutBinding
import com.akshar.one.databinding.BirthdayCellBinding
import com.akshar.one.databinding.HomeworkCellBinding
import com.akshar.one.databinding.StudentCellBinding
import com.akshar.one.model.BirthDayModel
import com.akshar.one.model.StudentListModel
import com.akshar.one.model.TimeTableModel
import com.akshar.one.studentprofile.ViewStudentProfileActivity

class StudentListAdapter(private val mContext: Activity, private val studentList: ArrayList<StudentListModel>?) :
    RecyclerView.Adapter<StudentListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.student_cell,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = studentList?.get(position)
        var name = ""
        if(model?.lastName != null){
            name = model.firstName +" "+model.lastName
        }else if(model?.firstName!= null){
            name = model.firstName!!
        }else{
            name = "N/A"
        }
        holder.binding.tvStudentName.text = name

        var count = "00."
        if(position < 9){
            count = "0"+(position+1)+"."
            holder.binding.tvSerialNumber.text = count
        }else{
            count = (position+1).toString()+"."
            holder.binding.tvSerialNumber.text = count
        }


        holder.binding.rlMain.setOnClickListener {
           ViewStudentProfileActivity.open(mContext,studentList!!,position)
        }

    }

    override fun getItemCount(): Int {
        return studentList!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: StudentCellBinding = StudentCellBinding.bind(itemView)

    }
}
