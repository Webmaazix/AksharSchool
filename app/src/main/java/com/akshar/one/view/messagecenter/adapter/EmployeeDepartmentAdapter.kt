package com.akshar.one.view.messagecenter.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.databinding.SectionRowBinding
import com.akshar.one.databinding.StudentCellBinding
import com.akshar.one.databinding.StudentCellMessageBinding
import com.akshar.one.model.EmployeeDepartmentList
import com.akshar.one.model.StudentListModel
import com.akshar.one.view.messagecenter.SendNotificationMessageActivity
import com.akshar.one.view.studentprofile.ViewStudentProfileActivity

class EmployeeDepartmentAdapter(private val mContext: Activity, private val list: ArrayList<EmployeeDepartmentList>?) :
    RecyclerView.Adapter<EmployeeDepartmentAdapter.ViewHolder>() {

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
        val name = model?.value

        holder.binding.tvClassName.text = name
        if(model?.isSelected!!){
            holder.binding.imgCheck.visibility = View.VISIBLE
            holder.binding.tvClassName.setTextColor(mContext.resources.getColor(R.color.light_blue))
        }else{
            holder.binding.imgCheck.visibility = View.GONE
            holder.binding.tvClassName.setTextColor(mContext.resources.getColor(R.color.black))
        }
        holder.itemView.setOnClickListener{
            model.isSelected  =   !model.isSelected
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: SectionRowBinding = SectionRowBinding.bind(itemView)

    }
}
