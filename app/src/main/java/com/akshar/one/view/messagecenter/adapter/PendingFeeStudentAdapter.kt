package com.akshar.one.view.messagecenter.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.databinding.StudentCellBinding
import com.akshar.one.model.AbsentStudentList
import com.akshar.one.model.FeeStudentList
import com.akshar.one.model.StudentListModel
import com.akshar.one.view.messagecenter.ReportActivity
import com.akshar.one.view.messagecenter.SendNotificationMessageActivity
import com.akshar.one.view.studentprofile.ViewStudentProfileActivity

class PendingFeeStudentAdapter(private val mContext: Activity, private val studentList: ArrayList<FeeStudentList>?) :
    RecyclerView.Adapter<PendingFeeStudentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.student_cell,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = studentList?.get(position)

        holder.binding.tvStudentName.text = model!!.student.fullName

        holder.binding.imgRight.visibility = View.GONE

        var count = "00."
        if(position < 9){
            count = "0"+(position+1)+"."
            holder.binding.tvSerialNumber.text = count
        }else{
            count = (position+1).toString()+"."
            holder.binding.tvSerialNumber.text = count
        }

        if(studentList!!.size <= 50){
            holder.binding.cbCheckBox.visibility = View.VISIBLE
        }else{
            holder.binding.cbCheckBox.visibility = View.GONE
        }

        holder.binding.cbCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            model.student.isSelected = isChecked
        }

    }

    override fun getItemCount(): Int {
        return studentList!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: StudentCellBinding = StudentCellBinding.bind(itemView)

    }
}
