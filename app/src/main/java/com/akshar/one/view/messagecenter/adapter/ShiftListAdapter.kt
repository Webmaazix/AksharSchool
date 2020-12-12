package com.akshar.one.view.messagecenter.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.databinding.SectionRowBinding
import com.akshar.one.databinding.StudentCellBinding
import com.akshar.one.model.AbsentStudentList
import com.akshar.one.model.ShiftList
import com.akshar.one.model.StudentListModel
import com.akshar.one.view.attendance2.AttendanceEntryFragment
import com.akshar.one.view.studentprofile.ViewStudentProfileActivity

class ShiftListAdapter(private val mContext: Activity, private val studentList: ArrayList<ShiftList>,
                       val fragment : AttendanceEntryFragment?) :
    RecyclerView.Adapter<ShiftListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.section_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = studentList.get(position)

        holder.binding.tvClassName.text = model.shiftName

        if(model.isSelected){
            holder.binding.imgCheck.visibility = View.VISIBLE
            holder.binding.tvClassName.setTextColor(mContext.resources.getColor(R.color.bluee))
        }else{
            holder.binding.imgCheck.visibility = View.GONE
            holder.binding.tvClassName.setTextColor(mContext.resources.getColor(R.color.black))
        }

        holder.itemView.setOnClickListener{
            if(fragment != null){
                (fragment).selectedShift(model)
            }
        }

    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: SectionRowBinding = SectionRowBinding.bind(itemView)
        init {
            itemView.setOnClickListener{
                studentList.get(adapterPosition).isSelected = !studentList.get(adapterPosition).isSelected
                notifyItemChanged(adapterPosition)
            }
        }


    }
}
