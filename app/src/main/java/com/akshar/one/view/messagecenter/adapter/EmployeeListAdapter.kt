package com.akshar.one.view.messagecenter.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.databinding.StudentCellBinding
import com.akshar.one.databinding.StudentCellMessageBinding
import com.akshar.one.model.EmployeeList
import com.akshar.one.model.StudentListModel
import com.akshar.one.view.messagecenter.SendNotificationMessageActivity
import com.akshar.one.view.studentprofile.ViewStudentProfileActivity

class EmployeeListAdapter(private val mContext: Activity, private val list: ArrayList<EmployeeList>?) :
    RecyclerView.Adapter<EmployeeListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.student_cell_message,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list?.get(position)
        var name = ""
        if(model?.lastName != null){
            name = model.firstName +" "+model.lastName
        }else if(model?.firstName!= null){
            name = model.firstName
        }else{
            name = "N/A"
        }
        holder.binding.tvUserName.text = name

        holder.binding.tvCheckStudent.isChecked = model?.isSelected!!

        if(mContext is SendNotificationMessageActivity){
            holder.binding.tvCheckStudent.visibility = View.VISIBLE
        }

        holder.binding.tvCheckStudent.setOnCheckedChangeListener { buttonView, isChecked ->
            model.isSelected = isChecked
        }


    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: StudentCellMessageBinding = StudentCellMessageBinding.bind(itemView)

    }
}
