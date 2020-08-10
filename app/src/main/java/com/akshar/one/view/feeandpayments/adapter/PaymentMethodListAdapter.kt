package com.akshar.one.view.feeandpayments.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.databinding.SectionRowBinding
import com.akshar.one.databinding.StudentCellBinding
import com.akshar.one.databinding.StudentCellMessageBinding
import com.akshar.one.model.BankAccount
import com.akshar.one.model.EmployeeDepartmentList
import com.akshar.one.model.StudentListModel
import com.akshar.one.view.feeandpayments.MakePaymentActivity
import com.akshar.one.view.messagecenter.SendNotificationMessageActivity
import com.akshar.one.view.studentprofile.ViewStudentProfileActivity

class PaymentMethodListAdapter(private val mContext: Activity, private val list: ArrayList<String>) :
    RecyclerView.Adapter<PaymentMethodListAdapter.ViewHolder>() {

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
        val model = list.get(position)
        holder.binding.tvClassName.text = model
        holder.itemView.setOnClickListener{
            (mContext as MakePaymentActivity).sendPaymentMethod(model!!)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: SectionRowBinding = SectionRowBinding.bind(itemView)

    }
}
