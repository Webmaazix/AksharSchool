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
import com.akshar.one.databinding.RowTermListBinding
import com.akshar.one.model.FeeHeadList
import com.akshar.one.model.FeeTermList
import com.akshar.one.model.FeesDetailModel
import com.akshar.one.util.AppUtil


import java.util.ArrayList

class FeeTermAdapter(private val mContext: Activity, private val list: ArrayList<FeeTermList>?) :
    RecyclerView.Adapter<FeeTermAdapter.ViewHolder>() {

    lateinit var adapter : FeeTermAdapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_term_list,
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list?.get(position)

        holder.binding.tvTermName.text = model?.feeTerm?.feeTerm
        val date = AppUtil.formatDate(model?.dueDate!!)
        val dateString = "Due Date :  $date"
        holder.binding.tvDueDate.text = dateString

        holder.binding.tvActuaAmount.text = "₹ "+ model?.feeAmount.toString()
        holder.binding.tvConcession.text = "₹ "+model?.concessionAmount.toString()
        holder.binding.tvFinalAmount.text = "₹ "+ model?.finalAmount.toString()
        holder.binding.tvDueAmount.text = "₹ "+model?.dueAmount.toString()

        if(position == list!!.size-1){
            holder.binding.vv2.visibility = View.GONE
        }else{
            holder.binding.vv2.visibility = View.VISIBLE
        }

    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: RowTermListBinding = RowTermListBinding.bind(itemView)

    }
}
