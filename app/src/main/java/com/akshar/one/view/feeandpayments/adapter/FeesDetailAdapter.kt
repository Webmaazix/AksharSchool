package com.akshar.one.view.feeandpayments.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.databinding.RowFeeDetailBinding
import com.akshar.one.model.FeesDetailModel
import com.akshar.one.model.StudentListModel
import com.akshar.one.view.feeandpayments.FeeDetailActivity
import com.akshar.one.view.feeandpayments.PayFeeActivity
import com.akshar.one.view.feeandpayments.StudentFeesDetails


import java.util.ArrayList

class FeesDetailAdapter(private val mContext: Activity, private val list: ArrayList<FeesDetailModel>?,
                        var studentId : Int,var studentModel : StudentListModel) :
    RecyclerView.Adapter<FeesDetailAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_fee_detail,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list?.get(position)
        val academicYear = "Academic year : "+model?.academicYear
        holder.binding.tvYear.text = academicYear
        var finalAmount = 0.0
        var dueAmount = 0.0
        var overDueAmount = 0.0
        for(data in  model!!.feeHeadList){
            for(price in data.feeTermList){
                if(finalAmount == 0.0){
                    finalAmount = price.finalAmount
                }else{
                    finalAmount += price.finalAmount
                }

                if(dueAmount == 0.0){
                    dueAmount = price.dueAmount
                }else{
                    dueAmount += price.dueAmount
                }

            }
        }
        holder.binding.tvFinalAmount.text = "₹ $finalAmount"
        holder.binding.tvDueAmount.text = "₹ $dueAmount"

        holder.binding.rlMain.setOnClickListener{
            FeeDetailActivity.open(mContext,model,studentId,studentModel )
        }
        holder.binding.tvPay.setOnClickListener{
            PayFeeActivity.open(mContext, model, studentId, studentModel)
        }

    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: RowFeeDetailBinding = RowFeeDetailBinding.bind(itemView)

    }
}
