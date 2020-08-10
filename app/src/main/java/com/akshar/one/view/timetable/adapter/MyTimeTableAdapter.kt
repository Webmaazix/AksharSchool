package com.akshar.one.view.timetable.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.databinding.RowClassTimetableBinding
import com.akshar.one.model.PeriodTimeTable
import com.akshar.one.model.TimeTableModel
import com.akshar.one.util.AppUtil

import java.util.ArrayList

class MyTimeTableAdapter(private val mContext: Activity, private val timeTableList: ArrayList<PeriodTimeTable>?) :
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

        if(timeTableModel?.timetable!= null){
            if(timeTableModel.timetable.size == 1){
                holder.binding.rlTime1.visibility = View.GONE
                holder.binding.rlSubject1.visibility = View.GONE
                holder.binding.rlSubject.visibility = View.VISIBLE
                holder.binding.rlTime.visibility = View.VISIBLE
                val model = timeTableModel.timetable[0]
                val className = model.classroom.courseName +" "+model.classroom.classroomName
                holder.binding.tvClassName.text = className
                if(model.subjectName.isNullOrEmpty()){
                    holder.binding.tvSubjectName.text = "N/A"
                }else
                    holder.binding.tvSubjectName.text = model.subjectName

                holder.binding.tvStartTime.text = model.startTime
                holder.binding.tvEndTime.text = model.endTime
            }else if(timeTableModel.timetable.size == 2){
                holder.binding.rlTime1.visibility = View.VISIBLE
                holder.binding.rlSubject1.visibility = View.VISIBLE
                holder.binding.rlSubject.visibility = View.GONE
                holder.binding.rlTime.visibility = View.GONE

                val model = timeTableModel.timetable[0]
                val className = model.classroom.courseName +" "+model.classroom.classroomName
                holder.binding.tvClassName1.text = className
                if(model.subjectName.isNullOrEmpty()){
                    holder.binding.tvSubjectName1.text = "N/A"
                }else
                    holder.binding.tvSubjectName1.text = model.subjectName

                val model1 = timeTableModel.timetable[1]
                val className1 = model1.classroom.courseName +" "+model1.classroom.classroomName
                holder.binding.tvClassName2.text = className1
                if(model1.subjectName.isNullOrEmpty()){
                    holder.binding.tvSubjectName2.text = "N/A"
                }else
                    holder.binding.tvSubjectName2.text = model1.subjectName

                holder.binding.tvStartTime1.text = model.startTime
                holder.binding.tvEndTime1.text = model.endTime
            }

        }

    }

    override fun getItemCount(): Int {
        return timeTableList!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: RowClassTimetableBinding = RowClassTimetableBinding.bind(itemView)

    }
}
