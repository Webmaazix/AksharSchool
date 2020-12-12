package com.akshar.one.view.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.databinding.BirthdayCellBinding
import com.akshar.one.databinding.NotificationCellBinding
import com.akshar.one.model.BirthDayModel
import com.akshar.one.model.NoticeBoardModel
import com.akshar.one.util.AppUtil
import com.akshar.one.util.AppUtils
import com.akshar.one.view.activity.MainActivity

import java.util.ArrayList

class NoticeBoardAdapter(private val mContext: Context, private val list: ArrayList<NoticeBoardModel>?) :
    RecyclerView.Adapter<NoticeBoardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.notification_cell,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list?.get(position)
        holder.binding.tvTitle.text =  model?.title
        holder.binding.tvDesc.text =  model?.description
    }

    override fun getItemCount(): Int {
        if(mContext is MainActivity){
            if(list!!.size > 3){
                return 3
            }else{
                return list.size
            }
        }else{
            return list!!.size
        }

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: NotificationCellBinding = NotificationCellBinding.bind(itemView)

    }
}
