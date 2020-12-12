package com.akshar.one.view.feeandpayments.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.databinding.EmployeeCellBinding
import com.akshar.one.databinding.StudentCellBinding
import com.akshar.one.databinding.StudentCellMessageBinding
import com.akshar.one.model.EmployeeList
import com.akshar.one.model.StudentListModel
import com.akshar.one.view.activity.MainActivity
import com.akshar.one.view.employeeprofile.ViewEmployeeProfileActivity
import com.akshar.one.view.feeandpayments.MakePaymentActivity
import com.akshar.one.view.messagecenter.SendNotificationMessageActivity
import com.akshar.one.view.studentprofile.ViewStudentProfileActivity

class EmployeeListAdapterPayment(private val mContext: Activity, private var list: ArrayList<EmployeeList>?) :
    RecyclerView.Adapter<EmployeeListAdapterPayment.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

            return ViewHolder(
                LayoutInflater.from(mContext).inflate(
                    R.layout.employee_cell,
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
            holder.employeeBinding!!.tvStudentName.text = name
            holder.employeeBinding!!.tvDepartment.text = model?.department

            var count = "00."
            if(position < 9){
                count = "0"+(position+1)+"."
                holder.employeeBinding!!.tvEmployeeId.text = count
            }else{
                count = (position+1).toString()+"."
                holder.employeeBinding!!.tvEmployeeId.text = count
            }



            holder.employeeBinding!!.rlMain.setOnClickListener{
                (mContext as MakePaymentActivity).selectedEmployee(model!!)
            }
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var employeeBinding : EmployeeCellBinding? = null
        init {
            employeeBinding = EmployeeCellBinding.bind(itemView)

        }



    }

    fun filterData(filterList:ArrayList<EmployeeList>) {
        list = filterList
        notifyDataSetChanged()
    }

}
