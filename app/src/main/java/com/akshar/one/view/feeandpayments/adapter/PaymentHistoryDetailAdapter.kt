package com.akshar.one.view.feeandpayments.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.databinding.RowFeeDetailBinding
import com.akshar.one.databinding.RowPaymentHistoryBinding
import com.akshar.one.databinding.RowPaymentHistoryDetailBinding
import com.akshar.one.model.PaymentHistoryModel
import com.akshar.one.model.PaymentList
import com.akshar.one.util.AppUtil
import com.akshar.one.view.feeandpayments.PayementHistoryDetailActivity


import java.util.ArrayList

class PaymentHistoryDetailAdapter(private val mContext: Activity, private val list: ArrayList<PaymentList>?) :
    RecyclerView.Adapter<PaymentHistoryDetailAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_payment_history_detail,
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list?.get(position)
        val feeHeadName = model?.feeHead+" "+model?.feeTerm
        holder.binding.tvTermName.text =feeHeadName
        holder.binding.tvAmount.text= model?.paymentAmount.toString()
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: RowPaymentHistoryDetailBinding = RowPaymentHistoryDetailBinding.bind(itemView)

    }
}
