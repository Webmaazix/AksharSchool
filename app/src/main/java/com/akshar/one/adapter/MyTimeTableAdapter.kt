package com.akshar.one.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.app.AksharSchoolApplication.Companion.context
import com.akshar.one.databinding.HomeworkCellBinding
import com.akshar.one.databinding.RowClassTimetableBinding
import com.akshar.one.model.TimeTableModel
import com.akshar.one.view.activity.MainActivity

import java.util.ArrayList

class MyTimeTableAdapter(private val mContext: Activity, private val timeTableList: ArrayList<TimeTableModel>?) :
    RecyclerView.Adapter<MyTimeTableAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_class_timetable,
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val timeTableModel = timeTableList?.get(position)


        val className = timeTableModel!!.classroom?.courseName +" "+timeTableModel.classroom?.classroomName
        holder.binding.tvClassName.text = className
        if(timeTableModel.subjectName.isNullOrEmpty()){
            holder.binding.tvSubjectName.text = "N/A"
        }else
           holder.binding.tvSubjectName.text = timeTableModel.subjectName

        holder.binding.tvStartTime.text = timeTableModel.startTime
        holder.binding.tvEndTime.text = timeTableModel.endTime
    }

    override fun getItemCount(): Int {
        return timeTableList!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: RowClassTimetableBinding = RowClassTimetableBinding.bind(itemView)

    }
}
