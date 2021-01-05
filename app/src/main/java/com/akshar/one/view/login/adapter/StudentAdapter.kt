package com.akshar.one.view.login.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.databinding.BirthdayCellBinding
import com.akshar.one.databinding.RowChildBinding
import com.akshar.one.databinding.RowSelectChildBinding
import com.akshar.one.manager.SessionManager
import com.akshar.one.model.AppList
import com.akshar.one.model.BirthDayModel
import com.akshar.one.util.AppUtil
import com.akshar.one.util.AppUtils
import com.akshar.one.view.activity.MainActivity
import com.akshar.one.view.login.SelectRoles

import java.util.ArrayList

class StudentAdapter(private val mContext: Context, private val list: ArrayList<AppList>,var selectedRole : AppList?) :
    RecyclerView.Adapter<StudentAdapter.ViewHolder>() {

    private var selectedPosition  = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if(mContext is SelectRoles){
            return ViewHolder(
                LayoutInflater.from(mContext).inflate(
                    R.layout.row_select_child,
                    parent,
                    false
                )
            )
        }else{
            return ViewHolder(
                LayoutInflater.from(mContext).inflate(
                    R.layout.row_child,
                    parent,
                    false
                )
            )
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list.get(position)
        if(mContext is SelectRoles){
            holder.binding!!.tvStudentName.text = model.studentName
            holder.binding!!.tvClassSection.text = model.className
            holder.binding!!.tvSchoolName.text = model.schoolName

            if(selectedRole!= null){
                val id  = selectedRole!!.id
                if(id.equals(model.id)){
                    holder.binding!!.imgDone.visibility = View.VISIBLE
                    holder.binding!!.rlMainBg.background = mContext.resources.getDrawable(R.drawable.green_stroke_shape)
                }else{
                    holder.binding!!.imgDone.visibility = View.GONE
                    holder.binding!!.rlMainBg.background = mContext.resources.getDrawable(R.drawable.white_nopadding_shape)

                }
            }else{
                if(selectedPosition == position){
                    holder.binding!!.imgDone.visibility = View.VISIBLE
                    holder.binding!!.rlMainBg.background = mContext.resources.getDrawable(R.drawable.green_stroke_shape)
                }else{
                    holder.binding!!.imgDone.visibility = View.GONE
                    holder.binding!!.rlMainBg.background = mContext.resources.getDrawable(R.drawable.white_nopadding_shape)
                }

            }

            var colorRes = 0
            when (position % 3) {
                0 -> colorRes = R.color.pinkTimetable
                1 -> colorRes = R.color.blueTimetable
                2 -> colorRes = R.color.orangeTimetable

            }
            holder.binding!!.rlMainBg!!.backgroundTintList = mContext.resources.getColorStateList(colorRes)



            if(model.url!= null && model.url!=""){
                holder.binding!!.flLayout.visibility = View.GONE
                holder.binding!!.imgUserProfile.visibility = View.VISIBLE
                AppUtils.loadImageCrop(
                    model.url,
                    holder.binding!!.imgUserProfile,
                    R.drawable.circle_default_pic,
                    80,
                    80
                )
            }else{
                holder.binding!!.flLayout.visibility = View.VISIBLE
                holder.binding!!.imgUserProfile.visibility = View.GONE
                var colorRes = 0
                when (position % 3) {
                    0 -> colorRes = R.color.light_pink
                    1 -> colorRes = R.color.light_blue1
                    2 -> colorRes = R.color.light_green1

                }
                var colorResText = 0

                if(colorRes == R.color.light_pink){
                    colorResText = R.color.pink
                }else if(colorRes == R.color.light_blue1){
                    colorResText  = R.color.light_blue
                }else if(colorRes == R.color.light_green1){
                    colorResText = R.color.green_normal
                }



//            holder.binding.imgNoImage.imageTintList =  mContext.resources.getColorStateList(colorRes)
                holder.binding!!.tvShortName.setTextColor(colorResText)

                if(model.schoolName!= null && model.schoolName != ""){
                    holder.binding!!.tvShortName.setText(
                        model.schoolName.substring(0, 2).toUpperCase()
                    )

                }
            }

            holder.itemView.setOnClickListener{
                selectedRole == null
                selectedPosition = position
//                model.isSelected = !model.isSelected
                SessionManager.setLoginRole(model)
                notifyDataSetChanged()
                (mContext as SelectRoles).updateRoleAdapter()
                MainActivity.open(mContext as Activity)
            }


        }else{
            holder.bindingChild!!.tvStudentName.text = model.studentName

            holder.itemView.setOnClickListener{
                selectedRole == null
                model.isSelected = !model.isSelected
                SessionManager.setLoginRole(model)
                notifyDataSetChanged()
                MainActivity.open(mContext as Activity)

            }

            if(model.url!= null && model.url!=""){
                holder.bindingChild!!.flLayout.visibility = View.GONE
                holder.bindingChild!!.imgUserProfile.visibility = View.VISIBLE
                AppUtils.loadImageCrop(
                    model.url,
                    holder.bindingChild!!.imgUserProfile,
                    R.drawable.circle_default_pic,
                    80,
                    80
                )
            }else{
                holder.bindingChild!!.flLayout.visibility = View.VISIBLE
                holder.bindingChild!!.imgUserProfile.visibility = View.GONE
                var colorRes = 0
                when (position % 3) {
                    0 -> colorRes = R.color.light_pink
                    1 -> colorRes = R.color.light_blue1
                    2 -> colorRes = R.color.light_green1

                }
                var colorResText = 0

                if(colorRes == R.color.light_pink){
                    colorResText = R.color.pink
                }else if(colorRes == R.color.light_blue1){
                    colorResText  = R.color.light_blue
                }else if(colorRes == R.color.light_green1){
                    colorResText = R.color.green_normal
                }



//            holder.binding.imgNoImage.imageTintList =  mContext.resources.getColorStateList(colorRes)
                holder.bindingChild!!.tvShortName.setTextColor(colorResText)

                holder.bindingChild!!.tvShortName.setText(
                    model.schoolName.substring(0, 2).toUpperCase()
                )
            }
        }

//        holder.bindingChild!!.tvStudentName.text = model.studentName

//        if(mContext is SelectRoles){
//            holder.binding.tvClassSection.text = model.className
//            holder.binding.tvSchoolName.text = model.schoolName
//
//            if(selectedRole!= null){
//                val name  = selectedRole!!.schoolName
//                if(name.equals(model.schoolName)){
//                    holder.binding.imgDone.visibility = View.VISIBLE
//                    holder.binding.rlMainBg.background = mContext.resources.getDrawable(R.drawable.green_stroke_shape)
//                }else{
//                    holder.binding.imgDone.visibility = View.GONE
//                    holder.binding.rlMainBg.background = mContext.resources.getDrawable(R.drawable.white_nopadding_shape)
//
//                }
//            }
//
//            var colorRes = 0
//            when (position % 3) {
//                0 -> colorRes = R.color.light_pink
//                1 -> colorRes = R.color.light_blue1
//                2 -> colorRes = R.color.light_orange1
//
//            }
//            holder.binding.rlMainBg!!.backgroundTintList = mContext.resources.getColorStateList(colorRes)
//
//
//            if(model.isSelected){
//                holder.binding.imgDone.visibility = View.VISIBLE
//                holder.binding.rlMainBg.background = mContext.resources.getDrawable(R.drawable.green_stroke_shape)
//            }else{
//                holder.binding.imgDone.visibility = View.GONE
//                holder.binding.rlMainBg.background = mContext.resources.getDrawable(R.drawable.white_nopadding_shape)
//            }
//
//        }


    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding : RowSelectChildBinding? = null
        var bindingChild : RowChildBinding? = null

        init {
            if(mContext is SelectRoles){
               binding = RowSelectChildBinding.bind(itemView)
            }else{
                bindingChild = RowChildBinding.bind(itemView)
            }
        }
    }
}
