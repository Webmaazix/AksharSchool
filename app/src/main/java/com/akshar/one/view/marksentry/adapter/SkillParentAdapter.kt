package com.akshar.one.view.marksentry.adapter

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.databinding.BirthdayCellBinding
import com.akshar.one.databinding.RowParentSkillsBinding
import com.akshar.one.databinding.RowStudentMarksEntryBinding
import com.akshar.one.databinding.SubjectCellBinding
import com.akshar.one.model.BirthDayModel
import com.akshar.one.model.SkillListParent
import com.akshar.one.model.StundentsMarksList
import com.akshar.one.model.SubjectListModel
import com.akshar.one.util.AppUtils
import com.akshar.one.view.activity.MainActivity
import com.akshar.one.view.marksentry.StudentMarksEntry
import com.akshar.one.view.marksentry.inputselection.SelectOtherInputsActivity

import java.util.ArrayList

class SkillParentAdapter(private val mContext: Activity,
                         private val list: ArrayList<SkillListParent>) :
    RecyclerView.Adapter<SkillParentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_parent_skills,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val model =list[position]
        holder.binding.tvSubCategoryName.text = model.skillName
        holder.binding.tvGrade.text = model.grade

        var colorRes = 0
        when (position % 2) {
            0 -> colorRes = R.color.white
            1 -> colorRes = R.color.blue_notice

        }
        holder.binding.rlSubCategory!!.backgroundTintList = mContext.resources.getColorStateList(colorRes)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: RowParentSkillsBinding = RowParentSkillsBinding.bind(itemView)
    }
}
