package com.akshar.one.view.home.adapter

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.databinding.HomeworkCellBinding
import com.akshar.one.manager.SessionManager
import com.akshar.one.model.PeriodTimeTable
import com.akshar.one.model.TimeTableModel
import com.akshar.one.view.activity.MainActivity

import java.util.ArrayList

class TimeTableAdapter(private val mContext: Context, private val timeTableList: ArrayList<PeriodTimeTable>?) :
    RecyclerView.Adapter<TimeTableAdapter.ViewHolder>() {
    private var screenWidth = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val displayMetrics = DisplayMetrics()
        (mContext as MainActivity).windowManager.getDefaultDisplay().getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels

        return ViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.homework_cell,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val timeTableModel = timeTableList?.get(position)

        if(SessionManager.getLoginRole()?.appName.equals("SmartParent",true)){
            holder.binding.tvHomeWork.visibility = View.GONE
            holder.binding.tvAttendance.visibility = View.GONE
        }else{
            holder.binding.tvHomeWork.visibility = View.VISIBLE
            holder.binding.tvAttendance.visibility = View.VISIBLE
        }

        val itemWidth = screenWidth / 1.20

        val lp = holder.binding.rlMain.layoutParams
        lp.height = lp.height
        lp.width = itemWidth.toInt()
        holder.binding.rlMain.layoutParams = lp

        if(timeTableModel?.timetable!= null){
            if(timeTableModel.timetable.size == 1){
                val model = timeTableModel.timetable.get(0)
                if(SessionManager.getLoginRole()?.appName.equals("SmartParent")) {
                    val teacherName = model.teacher.fullName
                    holder.binding.tvTimeLine.text = teacherName
                }else{
                    val className = model.classroom.courseName +" "+model.classroom.classroomName
                    holder.binding.tvTimeLine.text = className
                }

                holder.binding.tvSubjectName.text = model.subjectName
                val time = model.startTime +" - "+model.endTime

                if(model.subjectName != null){
                    if(model.subjectName.contains("Telugu",true)){
                        holder.binding.cvMain.background = mContext.resources.getDrawable(R.drawable.subject_telugu)
                        holder.binding.tvHomeWork.setTextColor(mContext.resources.getColor(R.color.telugu_blue))
                    }else if(model.subjectName.contains("Hindi",true)){
                        holder.binding.cvMain.background = mContext.resources.getDrawable(R.drawable.subject_hindi)
                        holder.binding.tvHomeWork.setTextColor(mContext.resources.getColor(R.color.hindi_orange))
                    }else if(model.subjectName.contains("English",true)){
                        holder.binding.cvMain.background = mContext.resources.getDrawable(R.drawable.subject_english)
                        holder.binding.tvHomeWork.setTextColor(mContext.resources.getColor(R.color.english_pink))
                    }else if(model.subjectName.contains("Mathematics",true)){
                        holder.binding.cvMain.background = mContext.resources.getDrawable(R.drawable.subject_math)
                        holder.binding.tvHomeWork.setTextColor(mContext.resources.getColor(R.color.math_purple))
                    }else if(model.subjectName.contains("Science",true)){
                        holder.binding.cvMain.background = mContext.resources.getDrawable(R.drawable.subject_science)
                        holder.binding.tvHomeWork.setTextColor(mContext.resources.getColor(R.color.science_blue))
                    }else if(model.subjectName.contains("Environmental Science",true)){
                        holder.binding.cvMain.background = mContext.resources.getDrawable(R.drawable.subject_environmentalstudy)
                        holder.binding.tvHomeWork.setTextColor(mContext.resources.getColor(R.color.environment_green))
                    }else if(model.subjectName.contains("Biology",true)){
                        holder.binding.cvMain.background = mContext.resources.getDrawable(R.drawable.subject_bio)
                        holder.binding.tvHomeWork.setTextColor(mContext.resources.getColor(R.color.bio_purple))
                    }else if(model.subjectName.contains("Physics",true)){
                        holder.binding.cvMain.background = mContext.resources.getDrawable(R.drawable.subject_physics)
                        holder.binding.tvHomeWork.setTextColor(mContext.resources.getColor(R.color.physics_pink))
                    }else if(model.subjectName.contains("Social Studies",true)){
                        holder.binding.cvMain.background = mContext.resources.getDrawable(R.drawable.subject_socialstudy)
                        holder.binding.tvHomeWork.setTextColor(mContext.resources.getColor(R.color.socialstudy_purple))
                    }else if(model.subjectName.contains("Civics",true)){
                        holder.binding.cvMain.background = mContext.resources.getDrawable(R.drawable.subject_civics)
                        holder.binding.tvHomeWork.setTextColor(mContext.resources.getColor(R.color.civics_green))
                    }else if(model.subjectName.contains("Commerce",true)){
                        holder.binding.cvMain.background = mContext.resources.getDrawable(R.drawable.subject_commerce)
                        holder.binding.tvHomeWork.setTextColor(mContext.resources.getColor(R.color.commerce_peach))
                    }else if(model.subjectName.contains("Economics",true)){
                        holder.binding.cvMain.background = mContext.resources.getDrawable(R.drawable.subject_economics)
                        holder.binding.tvHomeWork.setTextColor(mContext.resources.getColor(R.color.economics_purple))
                    }else if(model.subjectName.contains("Chemistry",true)){
                        holder.binding.cvMain.background = mContext.resources.getDrawable(R.drawable.subject_chemistry)
                        holder.binding.tvHomeWork.setTextColor(mContext.resources.getColor(R.color.chemistry_blue))
                    }else{
                        holder.binding.cvMain.setBackgroundResource(R.drawable.undefined_subject_green)
                        holder.binding.tvHomeWork.setTextColor(mContext.resources.getColor(R.color.undefined_green))
                    }

                }
                holder.binding.tvTime.text = time
            }else{
                val model = timeTableModel.timetable.get(0)
                if(SessionManager.getLoginRole()?.appName.equals("SmartParent")) {
                    val teacherName = model.teacher.fullName
                    holder.binding.tvTimeLine.text = teacherName
                }else{
                    var className = ""
                    for(model in timeTableModel.timetable){
                        if(className.equals("")){
                            className = model.classroom.courseName +" "+model.classroom.classroomName
                        }else{
                            className = className+" , "+ model.classroom.classroomName
                        }
                    }
                    holder.binding.tvTimeLine.text = className

                }

//                val className = model.classroom.courseName +" "+model.classroom.classroomName

                holder.binding.tvSubjectName.text = model.subjectName
                val time = model.startTime +" - "+model.endTime

                if(model.subjectName != null){
                    if(model.subjectName.contains("Telugu",true)){
                        holder.binding.cvMain.background = mContext.resources.getDrawable(R.drawable.subject_telugu)
                        holder.binding.tvHomeWork.setTextColor(mContext.resources.getColor(R.color.telugu_blue))
                    }else if(model.subjectName.contains("Hindi",true)){
                        holder.binding.cvMain.background = mContext.resources.getDrawable(R.drawable.subject_hindi)
                        holder.binding.tvHomeWork.setTextColor(mContext.resources.getColor(R.color.hindi_orange))
                    }else if(model.subjectName.contains("English",true)){
                        holder.binding.cvMain.background = mContext.resources.getDrawable(R.drawable.subject_english)
                        holder.binding.tvHomeWork.setTextColor(mContext.resources.getColor(R.color.english_pink))
                    }else if(model.subjectName.contains("Mathematics",true)){
                        holder.binding.cvMain.background = mContext.resources.getDrawable(R.drawable.subject_math)
                        holder.binding.tvHomeWork.setTextColor(mContext.resources.getColor(R.color.math_purple))
                    }else if(model.subjectName.contains("Science",true)){
                        holder.binding.cvMain.background = mContext.resources.getDrawable(R.drawable.subject_science)
                        holder.binding.tvHomeWork.setTextColor(mContext.resources.getColor(R.color.science_blue))
                    }else if(model.subjectName.contains("Environmental Science",true)){
                        holder.binding.cvMain.background = mContext.resources.getDrawable(R.drawable.subject_environmentalstudy)
                        holder.binding.tvHomeWork.setTextColor(mContext.resources.getColor(R.color.environment_green))
                    }else if(model.subjectName.contains("Biology",true)){
                        holder.binding.cvMain.background = mContext.resources.getDrawable(R.drawable.subject_bio)
                        holder.binding.tvHomeWork.setTextColor(mContext.resources.getColor(R.color.bio_purple))
                    }else if(model.subjectName.contains("Physics",true)){
                        holder.binding.cvMain.background = mContext.resources.getDrawable(R.drawable.subject_physics)
                        holder.binding.tvHomeWork.setTextColor(mContext.resources.getColor(R.color.physics_pink))
                    }else if(model.subjectName.contains("Social Studies",true)){
                        holder.binding.cvMain.background = mContext.resources.getDrawable(R.drawable.subject_socialstudy)
                        holder.binding.tvHomeWork.setTextColor(mContext.resources.getColor(R.color.socialstudy_purple))
                    }else if(model.subjectName.contains("Civics",true)){
                        holder.binding.cvMain.background = mContext.resources.getDrawable(R.drawable.subject_civics)
                        holder.binding.tvHomeWork.setTextColor(mContext.resources.getColor(R.color.civics_green))
                    }else if(model.subjectName.contains("Commerce",true)){
                        holder.binding.cvMain.background = mContext.resources.getDrawable(R.drawable.subject_commerce)
                        holder.binding.tvHomeWork.setTextColor(mContext.resources.getColor(R.color.commerce_peach))
                    }else if(model.subjectName.contains("Economics",true)){
                        holder.binding.cvMain.background = mContext.resources.getDrawable(R.drawable.subject_economics)
                        holder.binding.tvHomeWork.setTextColor(mContext.resources.getColor(R.color.economics_purple))
                    }else if(model.subjectName.contains("Chemistry",true)){
                        holder.binding.cvMain.background = mContext.resources.getDrawable(R.drawable.subject_chemistry)
                        holder.binding.tvHomeWork.setTextColor(mContext.resources.getColor(R.color.chemistry_blue))
                    }else{
                        holder.binding.cvMain.setBackgroundResource(R.drawable.undefined_subject_green)
                        holder.binding.tvHomeWork.setTextColor(mContext.resources.getColor(R.color.undefined_green))
                    }

                }
                holder.binding.tvTime.text = time
            }

        }


    }

    override fun getItemCount(): Int {
        return timeTableList!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         val binding: HomeworkCellBinding = HomeworkCellBinding.bind(itemView)
    }
}
