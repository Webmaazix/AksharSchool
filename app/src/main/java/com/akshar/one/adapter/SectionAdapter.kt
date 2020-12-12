package com.akshar.one.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.databinding.SectionRowBinding
import com.akshar.one.view.examschedule.ScheduleExamActivity
import com.akshar.one.view.examschedule.ScheduledExamList
import com.akshar.one.model.ClassDropDownModel
import com.akshar.one.model.SectionList
import com.akshar.one.view.attendance2.AttendanceEntryFragment
import com.akshar.one.view.feeandpayments.StudentListForFeesFragment
import com.akshar.one.view.marksentry.inputselection.ClassSectionSelectActivity
import com.akshar.one.view.marksentry.inputselection.SelectOtherInputsActivity
import com.akshar.one.view.messagecenter.ReportActivity
import com.akshar.one.view.messagecenter.SendMarksAndFeeReport
import com.akshar.one.view.studentprofile.EditStudentProfileActivity
import com.akshar.one.view.studentprofile.StudentListFragment
import com.akshar.one.view.timetable.ClassTimeTableFragment

import java.util.ArrayList

class SectionAdapter(private val mContext: Activity, var parent: Int,
                     var child:Int, private val list: ArrayList<SectionList>?, private var fragment: Fragment?,
                     private var model: ClassDropDownModel, val callback: ClassDropDownAdapter.SectionSelection
) :
    RecyclerView.Adapter<SectionAdapter.ViewHolder>() {





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.section_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val section = list?.get(position)
        holder.binding.tvClassName.text = section!!.classroomName
        if(child == position){
            holder.binding.imgCheck.visibility = View.VISIBLE
            holder.binding.tvClassName.setTextColor(mContext.resources.getColor(R.color.bluee))
        }else{
            holder.binding.imgCheck.visibility = View.GONE
            holder.binding.tvClassName.setTextColor(mContext.resources.getColor(R.color.black))
        }


    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: SectionRowBinding = SectionRowBinding.bind(itemView)

        init {
            itemView.setOnClickListener {
                callback.selectionCallback(parent,adapterPosition)
                if(fragment!= null){
                    if(fragment is ClassTimeTableFragment){
                        (fragment as ClassTimeTableFragment).sectionClicked(model , model.classroomsList.get(adapterPosition))
                    }else if(fragment is StudentListFragment){
                        (fragment as StudentListFragment).sectionClicked(model ,model.classroomsList[adapterPosition])
                    }else if(fragment is StudentListForFeesFragment){
                        (fragment as StudentListForFeesFragment).sectionClicked(model ,model.classroomsList[adapterPosition])
                    }else if(fragment is AttendanceEntryFragment){
                        (fragment as AttendanceEntryFragment).sectionClicked(model ,model.classroomsList[adapterPosition])
                    }
                }else{
                    if(mContext is EditStudentProfileActivity){
                        (mContext).sectionClicked(model,model.classroomsList[adapterPosition])
                    }else if(mContext is ScheduleExamActivity){
                        (mContext).sectionClicked(model,model.classroomsList[adapterPosition])
                    }else if(mContext is ScheduledExamList){
                        (mContext).sectionClicked(model,model.classroomsList[adapterPosition])
                    }else if(mContext is ReportActivity){
//                        (mContext).sectionClicked(model,model.classroomsList[adapterPosition])
                    }else if(mContext is SelectOtherInputsActivity){
                        (mContext).sectionClicked(model,model.classroomsList[adapterPosition])
                    }else if(mContext is ClassSectionSelectActivity){
                        (mContext).sectionClicked(model,model.classroomsList[adapterPosition])
                    }else if(mContext is SendMarksAndFeeReport){
                        (mContext).sectionClicked(model,model.classroomsList[adapterPosition])
                    }
                }

            }

        }

    }
}
