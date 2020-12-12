package com.akshar.one.view.login.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.databinding.BirthdayCellBinding
import com.akshar.one.databinding.RowSchoolListBinding
import com.akshar.one.manager.SessionManager
import com.akshar.one.model.AppList
import com.akshar.one.util.AppUtils
import com.akshar.one.view.activity.MainActivity
import com.akshar.one.view.login.SelectRoles

import java.util.ArrayList

class RoleAdapter(private val mContext: Context, private val list: ArrayList<AppList>,private var loginRole : AppList?) :
    RecyclerView.Adapter<RoleAdapter.ViewHolder>() {

    private var selectedPosition  = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_school_list,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list.get(position)
        holder.binding.tvSchoolName.text = model.schoolName
        holder.binding.tvEmployeeCount.text = model.employeeCount.toString()
        holder.binding.tvStudentCount.text = model.studentCount.toString()


        if(model.url!= null && model.url!=""){
            holder.binding.flLayout.visibility = View.GONE
            holder.binding.imgUserProfile.visibility = View.VISIBLE
            AppUtils.loadImageCrop(
                model.url,
                holder.binding.imgUserProfile,
                R.drawable.circle_default_pic,
                80,
                80
            )
        }else {
            holder.binding.flLayout.visibility = View.VISIBLE
            holder.binding.imgUserProfile.visibility = View.GONE

                holder.binding.tvShortName.setText(
                    model.schoolName.substring(0, 2).toUpperCase()
                )


        }

        if(loginRole!= null){
            val id  = loginRole!!.id
            if(id.equals(model.id)){
                holder.binding.imgDone.visibility = View.VISIBLE
                holder.binding.rlMainBg.background = mContext.resources.getDrawable(R.drawable.green_stroke_shape)
            }else{
                holder.binding.imgDone.visibility = View.GONE
                holder.binding.rlMainBg.background = mContext.resources.getDrawable(R.drawable.white_nopadding_shape)

            }
        }else {
            if(selectedPosition == position){
                holder.binding.imgDone.visibility = View.VISIBLE
                holder.binding.rlMainBg.background = mContext.resources.getDrawable(R.drawable.green_stroke_shape)
            }else{
                holder.binding.imgDone.visibility = View.GONE
                holder.binding.rlMainBg.background = mContext.resources.getDrawable(R.drawable.white_nopadding_shape)
            }

        }


        holder.itemView.setOnClickListener{
            loginRole == null
            selectedPosition = position
//            model.selectedPosition = position
            SessionManager.setLoginRole(model)
            notifyDataSetChanged()
            (mContext as SelectRoles).updateStudentAdapter()
            MainActivity.open(mContext as Activity)

        }
    }

    override fun getItemCount(): Int {
        return list.size


    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: RowSchoolListBinding = RowSchoolListBinding.bind(itemView)

    }
}
