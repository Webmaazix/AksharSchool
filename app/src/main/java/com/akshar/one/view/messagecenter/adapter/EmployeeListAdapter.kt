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
import com.akshar.one.view.activity.MainActivity
import com.akshar.one.view.employeeprofile.ViewEmployeeProfileActivity
import com.akshar.one.view.messagecenter.SendNotificationMessageActivity
import com.akshar.one.view.studentprofile.ViewStudentProfileActivity

class EmployeeListAdapter(private val mContext: Activity, private val list: ArrayList<EmployeeList>?) :
    RecyclerView.Adapter<EmployeeListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if(mContext is MainActivity){
            return ViewHolder(
                LayoutInflater.from(mContext).inflate(
                    R.layout.student_cell,
                    parent,
                    false
                )
            )

        }else{
            return ViewHolder(
                LayoutInflater.from(mContext).inflate(
                    R.layout.student_cell_message,
                    parent,
                    false
                )
            )

        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if(mContext is MainActivity) {
            val model = list?.get(position)
            var name = ""
            if(model?.lastName != null){
                name = model.firstName +" "+model.lastName
            }else if(model?.firstName!= null){
                name = model.firstName
            }else{
                name = "N/A"
            }
            holder.employeeBinding!!.tvStudentName.text = name

            var count = "00."
            if(position < 9){
                count = "0"+(position+1)+"."
                holder.employeeBinding!!.tvSerialNumber.text = count
            }else{
                count = (position+1).toString()+"."
                holder.employeeBinding!!.tvSerialNumber.text = count
            }



            holder.employeeBinding!!.rlMain.setOnClickListener{
                ViewEmployeeProfileActivity.open(mContext,list,position,(mContext as MainActivity).securityList)

            }

        }else{
            val model = list?.get(position)
            var name = ""
            if(model?.lastName != null){
                name = model.firstName +" "+model.lastName
            }else if(model?.firstName!= null){
                name = model.firstName
            }else{
                name = "N/A"
            }
            holder.binding!!.tvUserName.text = name

            holder.binding!!.tvCheckStudent.visibility = View.VISIBLE
            holder.binding!!.tvCheckStudent.isChecked = model?.isSelected!!

            if(mContext is SendNotificationMessageActivity){
                holder.binding!!.tvCheckStudent.visibility = View.VISIBLE
            }

            holder.binding!!.tvCheckStudent.setOnCheckedChangeListener { buttonView, isChecked ->
                model.isSelected = isChecked
            }


        }



    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding : StudentCellMessageBinding? = null
        var employeeBinding : StudentCellBinding? = null
        init {
            if(mContext is MainActivity){
                employeeBinding = StudentCellBinding.bind(itemView)
            }else{
                 binding= StudentCellMessageBinding.bind(itemView)
            }
        }



    }
}
