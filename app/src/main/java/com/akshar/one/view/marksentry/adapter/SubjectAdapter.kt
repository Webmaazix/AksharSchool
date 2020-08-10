package com.akshar.one.view.marksentry.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.databinding.BirthdayCellBinding
import com.akshar.one.databinding.SubjectCellBinding
import com.akshar.one.model.BirthDayModel
import com.akshar.one.model.SubjectListModel
import com.akshar.one.util.AppUtils
import com.akshar.one.view.activity.MainActivity
import com.akshar.one.view.marksentry.inputselection.SelectOtherInputsActivity

import java.util.ArrayList

class SubjectAdapter(private val mContext: Context, private val list: ArrayList<SubjectListModel>) :
    RecyclerView.Adapter<SubjectAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.subject_cell,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model  = list[position]
        holder.binding.tvSubjectName.text = model.subjectName

        holder.binding.rlCategory.setOnClickListener{
            (mContext as SelectOtherInputsActivity).selectedCategory(model)
        }
    }

    override fun getItemCount(): Int {
       return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: SubjectCellBinding = SubjectCellBinding.bind(itemView)

    }
}
