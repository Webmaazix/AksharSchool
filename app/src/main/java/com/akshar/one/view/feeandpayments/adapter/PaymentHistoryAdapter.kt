package com.akshar.one.view.feeandpayments.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.databinding.RowFeeDetailBinding
import com.akshar.one.databinding.RowPaymentHistoryBinding
import com.akshar.one.model.PaymentHistoryModel
import com.akshar.one.util.AppUtil
import com.akshar.one.view.feeandpayments.PayementHistoryDetailActivity


import java.util.ArrayList

class PaymentHistoryAdapter(private val mContext: Activity, private val list: ArrayList<PaymentHistoryModel>?) :
    RecyclerView.Adapter<PaymentHistoryAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_payment_history,
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list?.get(position)
        holder.binding.tvInvoiceNumber.text = model?.invoiceNumber.toString()

        val paymentDate = AppUtil.formatDate(model?.paymentDate!!)

        holder.binding.tvDate.text = paymentDate
        holder.binding.tvStudentName.text = model.student.fullName
        holder.binding.tvPaymentMethod.text = model.paymentMethod

        holder.binding.rlMain.setOnClickListener{
            PayementHistoryDetailActivity.open(mContext,model)
        }
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: RowPaymentHistoryBinding = RowPaymentHistoryBinding.bind(itemView)

    }
}
