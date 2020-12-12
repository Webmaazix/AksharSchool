package com.akshar.one.view.marksentry.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.databinding.SectionRowBinding
import com.akshar.one.databinding.SubjectMarksRowBinding
import com.akshar.one.model.MarksTestListParent
import com.akshar.one.model.SubjectListParent

import java.util.ArrayList

class SubjectMarksParentAdapter(private val mContext: Context, private val list: ArrayList<SubjectListParent>) :
    RecyclerView.Adapter<SubjectMarksParentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.subject_marks_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model  = list[position]

        holder.binding.tvSubjectName.text = model.subjectName
        holder.binding.tvAchievedMarks.text = model.marksScored
        holder.binding.tvTotalMarks.text = model.maxMarks

        var colorRes = 0
        when (position % 4) {
            0 -> colorRes = R.color.light_pink
            1 -> colorRes = R.color.light_blue1
            2 -> colorRes = R.color.light_orange1
            3 -> colorRes = R.color.light_green1

        }
        holder.binding.rlMain!!.backgroundTintList = mContext.resources.getColorStateList(colorRes)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: SubjectMarksRowBinding = SubjectMarksRowBinding.bind(itemView)

    }
}
