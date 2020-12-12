package com.akshar.one.view.timetable.adapter

import android.app.Activity
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.databinding.RowTimeTableBinding
import com.akshar.one.model.TimeTableSchedule
import com.akshar.one.view.activity.MainActivity
import com.akshar.one.view.home.adapter.TimeTableAdapter
import com.akshar.one.view.timetable.ClassTimeTableFragment
import com.akshar.one.view.timetable.ParentTimetableActivity

import java.util.ArrayList

class TimeTableRowAdapter(private val mContext: Activity, private val timeTableList: ArrayList<TimeTableSchedule>?, private var fragment : Fragment?) :
    RecyclerView.Adapter<TimeTableRowAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    
        
        return ViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_time_table,
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val model = timeTableList!!.get(position)
        
        


                  if(fragment is ClassTimeTableFragment){
                      val teacherName = model.teacher.fullName
                      holder.binding.tvClassName.text = teacherName
                  }else if(mContext is ParentTimetableActivity){
                      val teacherName = model.teacher.fullName
                      holder.binding.tvClassName.text = teacherName

                  }else{
                      val className = model.classroom.courseName + " " + model.classroom.classroomName
                      holder.binding.tvClassName.text = className
                  }

                holder.binding.tvSubjectName.text = model.subjectName

                if (model.subjectName != null) {
                    if (model.subjectName.contains("Telugu", true)) {
                        holder.binding.llNotification.backgroundTintList = mContext.resources.getColorStateList(R.color.timetable_telugu)
                        holder.binding.imgNotification.setImageResource(R.drawable.timetable_telugu)
                    } else if (model.subjectName.contains("Hindi", true)) {
                        holder.binding.llNotification.backgroundTintList = mContext.resources.getColorStateList(R.color.timetable_hindi)
                        holder.binding.imgNotification.setImageResource(R.drawable.timetable_hindi)
                    } else if (model.subjectName.contains("English", true)) {
                        holder.binding.llNotification.backgroundTintList = mContext.resources.getColorStateList(R.color.timetable_english)
                        holder.binding.imgNotification.setImageResource(R.drawable.timetable_english)
                    } else if (model.subjectName.contains("Mathematics", true)) {
                        holder.binding.llNotification.backgroundTintList = mContext.resources.getColorStateList(R.color.timetable_math)
                        holder.binding.imgNotification.setImageResource(R.drawable.timetable_math)
                    } else if (model.subjectName.contains("Science", true)) {
                        holder.binding.llNotification.backgroundTintList = mContext.resources.getColorStateList(R.color.timetable_science)
                        holder.binding.imgNotification.setImageResource(R.drawable.timetable_science)
                    } else if (model.subjectName.contains("Environmental Science", true)) {
                        holder.binding.llNotification.backgroundTintList = mContext.resources.getColorStateList(R.color.timetable_environmental)
                        holder.binding.imgNotification.setImageResource(R.drawable.timetable_environmental_study)
                    } else if (model.subjectName.contains("Biology", true)) {
                        holder.binding.llNotification.backgroundTintList = mContext.resources.getColorStateList(R.color.timetable_bio)
                        holder.binding.imgNotification.setImageResource(R.drawable.timetable_bio)
                    } else if (model.subjectName.contains("Physics", true)) {
                        holder.binding.llNotification.backgroundTintList = mContext.resources.getColorStateList(R.color.timetable_physics)
                        holder.binding.imgNotification.setImageResource(R.drawable.timetable_physics)
                    } else if (model.subjectName.contains("Social Studies", true)) {
                        holder.binding.llNotification.backgroundTintList = mContext.resources.getColorStateList(R.color.timetable_socialstudy)
                        holder.binding.imgNotification.setImageResource(R.drawable.timetable_social_study)
                    } else if (model.subjectName.contains("Civics", true)) {
                        holder.binding.llNotification.backgroundTintList = mContext.resources.getColorStateList(R.color.timetable_civics)
                        holder.binding.imgNotification.setImageResource(R.drawable.timetable_civics)
                    } else if (model.subjectName.contains("Commerce", true)) {
                        holder.binding.llNotification.backgroundTintList = mContext.resources.getColorStateList(R.color.timetable_commerce)
                        holder.binding.imgNotification.setImageResource(R.drawable.timetable_commerce)
                    } else if (model.subjectName.contains("Economics", true)) {
                        holder.binding.llNotification.backgroundTintList = mContext.resources.getColorStateList(R.color.timetable_economics)
                        holder.binding.imgNotification.setImageResource(R.drawable.timetable_economics)
                    } else if (model.subjectName.contains("Chemistry", true)) {
                        holder.binding.llNotification.backgroundTintList = mContext.resources.getColorStateList(R.color.timetable_chemistry)
                        holder.binding.imgNotification.setImageResource(R.drawable.timetable_chemistry)
                    } else {
                        holder.binding.llNotification.backgroundTintList = mContext.resources.getColorStateList(R.color.timetable_other)
                        holder.binding.imgNotification.setImageResource(R.drawable.timetableother)
                    }

                }
            

        
    }

    override fun getItemCount(): Int {
        return timeTableList!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: RowTimeTableBinding = RowTimeTableBinding.bind(itemView)

    }
}
