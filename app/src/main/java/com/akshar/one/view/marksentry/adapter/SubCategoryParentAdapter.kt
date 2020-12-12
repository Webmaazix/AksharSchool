package com.akshar.one.view.marksentry.adapter

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.databinding.BirthdayCellBinding
import com.akshar.one.databinding.RowParentSubcategoryBinding
import com.akshar.one.databinding.RowStudentMarksEntryBinding
import com.akshar.one.databinding.SubjectCellBinding
import com.akshar.one.model.*
import com.akshar.one.util.AppUtils
import com.akshar.one.view.activity.MainActivity
import com.akshar.one.view.marksentry.StudentMarksEntry
import com.akshar.one.view.marksentry.inputselection.SelectOtherInputsActivity

import java.util.ArrayList

class SubCategoryParentAdapter(private val mContext: Activity,
                         private val list: ArrayList<SubCategoryListParent>) :
    RecyclerView.Adapter<SubCategoryParentAdapter.ViewHolder>() {

    private lateinit var skillAdapter: SkillParentAdapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_parent_subcategory,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model =list[position]
        holder.binding.tvSubCategoryName.text = model.subCategoryName
        holder.binding.rvSkillList.setHasFixedSize(true)
        holder.binding.rvSkillList.layoutManager = LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false)
        skillAdapter = SkillParentAdapter(mContext,model.skillList)
        holder.binding.rvSkillList.adapter = skillAdapter

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: RowParentSubcategoryBinding = RowParentSubcategoryBinding.bind(itemView)

    }
}
