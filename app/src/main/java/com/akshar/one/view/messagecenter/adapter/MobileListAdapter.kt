package com.akshar.one.view.messagecenter.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.databinding.MobileRowBinding
import com.akshar.one.databinding.SectionRowBinding
import com.akshar.one.databinding.StudentCellBinding
import com.akshar.one.model.AbsentStudentList
import com.akshar.one.model.ShiftList
import com.akshar.one.model.StudentListModel
import com.akshar.one.view.attendance2.AttendanceEntryFragment
import com.akshar.one.view.studentprofile.ViewStudentProfileActivity

class MobileListAdapter(private val mContext: Activity, private val list: ArrayList<String>) :
    RecyclerView.Adapter<MobileListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.mobile_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list.get(position)

        holder.binding.tvMobileNumber.text = model

        holder.binding.imgRemove.setOnClickListener{
            list.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: MobileRowBinding = MobileRowBinding.bind(itemView)
        init {

        }


    }
}
