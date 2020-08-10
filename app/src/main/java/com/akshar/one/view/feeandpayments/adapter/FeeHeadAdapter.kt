package com.akshar.one.view.feeandpayments.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.databinding.RowFeeDetailBinding
import com.akshar.one.databinding.RowFeeHeadDetailBinding
import com.akshar.one.model.FeeHeadList
import com.akshar.one.model.FeesDetailModel
import com.akshar.one.view.feeandpayments.PayFeeActivity


import java.util.ArrayList

class FeeHeadAdapter(private val mContext: Activity, private val list: ArrayList<FeeHeadList>?) :
    RecyclerView.Adapter<FeeHeadAdapter.ViewHolder>() {

    lateinit var adapter : FeeTermAdapter
    lateinit var adapter1 : PayFeeTermAdapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_fee_head_detail,
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list?.get(position)
      
        holder.binding.tvFeeHeadName.text = model?.feeHead

        if(mContext is PayFeeActivity){
            holder.binding.rvTermsList.setHasFixedSize(true)
            holder.binding.rvTermsList.layoutManager = LinearLayoutManager(mContext,
                LinearLayoutManager.VERTICAL,false)
            adapter1 = PayFeeTermAdapter(
                mContext,
                model?.feeTermList)

            holder.binding.rvTermsList.adapter = adapter1

        }else{
            holder.binding.rvTermsList.setHasFixedSize(true)
            holder.binding.rvTermsList.layoutManager = LinearLayoutManager(mContext,
                LinearLayoutManager.VERTICAL,false)
            adapter = FeeTermAdapter(
                mContext,
                model?.feeTermList)

            holder.binding.rvTermsList.adapter = adapter
        }



    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: RowFeeHeadDetailBinding = RowFeeHeadDetailBinding.bind(itemView)

    }
}
