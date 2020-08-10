package com.akshar.one.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.databinding.SectionRowBinding
import com.akshar.one.model.*
import com.akshar.one.view.examschedule.ScheduleExamActivity
import com.akshar.one.view.examschedule.ScheduledExamList
import com.akshar.one.view.marksentry.inputselection.SelectOtherInputsActivity
import com.akshar.one.view.messagecenter.ReportActivity
import com.akshar.one.view.studentprofile.EditStudentProfileActivity
import com.akshar.one.view.studentprofile.StudentListFragment
import com.akshar.one.view.timetable.ClassTimeTableFragment

import java.util.ArrayList

class SkillLiatAdapter(private val mContext: Activity, private val list: ArrayList<SkillSetModel>?,
                     private var subCategory: SubCategoryList, private var category : SkillListModel) :
    RecyclerView.Adapter<SkillLiatAdapter.ViewHolder>() {

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
        val model = list?.get(position)
        holder.binding.tvClassName.text = model!!.skillName
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: SectionRowBinding = SectionRowBinding.bind(itemView)

        init {
            itemView.setOnClickListener {
                    if(mContext is SelectOtherInputsActivity){
                        (mContext).skillClicked(subCategory,category,list!![adapterPosition])
                    }


            }

        }

    }
}
