package com.akshar.one.view.timetable.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.databinding.RowClassTimetableBinding
import com.akshar.one.model.PeriodTimeTable
import com.akshar.one.model.TimeTableModel
import com.akshar.one.util.AppUtil

import java.util.ArrayList

class MyTimeTableAdapter(private val mContext: Activity, private val timeTableList: ArrayList<PeriodTimeTable>?,
                         private var fragment : Fragment?) :
    RecyclerView.Adapter<MyTimeTableAdapter.ViewHolder>() {

    lateinit var adapter : TimeTableRowAdapter

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
            val model = timeTableModel.timetable[0]
            val startTime = AppUtil.parseTime(model.startTime)
            val endTime = AppUtil.parseTime(model.endTime)
            val time = "$startTime - $endTime"
            holder.binding.tvStartTime.text = time

            holder.binding.rvTimeTable.setHasFixedSize(true)
            holder.binding.rvTimeTable.layoutManager = LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false)
            adapter = TimeTableRowAdapter(mContext,timeTableModel.timetable,fragment)
            holder.binding.rvTimeTable.adapter = adapter


            if(timeTableModel.timetable.size == 1){
                holder.binding.imgLine.layoutParams.height = 240
            }else if(timeTableModel.timetable.size == 2){
                holder.binding.imgLine.layoutParams.height = 410
            }

//            holder.binding.rvTimeTable.measure(View.MeasureSpec.makeMeasureSpec(holder.binding.rvTimeTable.width,
//                View.MeasureSpec.EXACTLY), View.MeasureSpec.UNSPECIFIED)
//            val height = holder.binding.rvTimeTable.measuredHeight
//
//            holder.binding.imgLine.layoutParams.height = height
//            holder.binding.imgLine.requestLayout()

        }

    }

    override fun getItemCount(): Int {
        return timeTableList!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding : RowClassTimetableBinding = RowClassTimetableBinding.bind(itemView)

    }
}
