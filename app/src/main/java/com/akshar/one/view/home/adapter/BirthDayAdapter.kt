package com.akshar.one.view.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.databinding.BirthdayCellBinding
import com.akshar.one.model.BirthDayModel
import com.akshar.one.util.AppUtil
import com.akshar.one.util.AppUtils
import com.akshar.one.view.activity.MainActivity

import java.util.ArrayList

class BirthDayAdapter(private val mContext: Context, private val birthDayModel: ArrayList<BirthDayModel>?) :
    RecyclerView.Adapter<BirthDayAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.birthday_cell,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val birthDayModel = birthDayModel?.get(position)
        var name = ""
        if(birthDayModel?.lastName!= null){
            name = birthDayModel.firstName+" "+birthDayModel.lastName
        }else{
            name = birthDayModel?.firstName!!
        }
        holder.binding.tvClassName.text = "N/A"
        val bdyDate = AppUtil.formatBirthdayDate(birthDayModel.birthday!!)
        holder.binding.tvDate.text = bdyDate

        holder.binding.tvUserName.text  = name

        if(birthDayModel.imageUrl!= null || birthDayModel.imageUrl!=""){
            AppUtils.loadImageCrop(
                birthDayModel.imageUrl,
                holder.binding.imgUserProfile,
                R.drawable.circle_default_pic,
                80,
                80
            )
        }
    }

    override fun getItemCount(): Int {
        if(mContext is MainActivity){
            if(birthDayModel!!.size > 3){
                return 3
            }else{
               return birthDayModel.size
            }
        }else{
            return birthDayModel!!.size
        }

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: BirthdayCellBinding = BirthdayCellBinding.bind(itemView)

    }
}
