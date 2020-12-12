package com.akshar.one.view.attendance2.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.databinding.AttendanceEntryCellLayoutBinding
import com.akshar.one.databinding.StudentCellBinding
import com.akshar.one.extension.visible
import com.akshar.one.model.StudentAttendanceModel
import com.akshar.one.model.StudentListModel
import com.akshar.one.view.feeandpayments.StudentFeesDetails
import com.akshar.one.view.feeandpayments.StudentListForFeesFragment
import com.akshar.one.view.studentprofile.ViewStudentProfileActivity
import java.util.logging.Handler

class StudentAttendanceAdapter(private val mContext: Activity, private val studentList: ArrayList<StudentAttendanceModel>,var from : String) :
    RecyclerView.Adapter<StudentAttendanceAdapter.ViewHolder>() {

    private var clickTrue = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.attendance_entry_cell_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = studentList.get(position)
        var name = ""

        holder.binding.txtStudentName.text = model.fullName

        var count = "00."
        if(position < 9){
            count = "0"+(position+1)+"."
            holder.binding.txtRollNo.text = count
        }else{
            count = (position+1).toString()+"."
            holder.binding.txtRollNo.text = count
        }

        if(from.equals("Employee")){
//            holder.binding.llAttendanceEmployee.visibility = View.VISIBLE
//            holder.binding.llAttendance.visibility = View.GONE

            if(model.attendanceInd.equals("P",true)){
                holder.binding.llAttendanceEmployee.visibility = View.GONE
                clickTrue = true
                holder.binding.txtAttendanceSwitch.isChecked = true
                if(model.lateEntryFlag.equals("Y",true)){
                    holder.binding.txtAttendanceStatus.background = mContext.getDrawable(R.drawable.orange_rounded_radius_2_bg)
                    holder.binding.txtAttendanceStatus.text = mContext.getString(R.string.late)
                    holder.binding.txtAttendanceStatus.visible()
                }else{
                    holder.binding.txtAttendanceStatus.background = mContext.getDrawable(R.drawable.green_radious_2_bg)
                    holder.binding.txtAttendanceStatus.text = mContext.getString(R.string.present)
                    holder.binding.txtAttendanceStatus.visible()
                }

            }else if(model.attendanceInd.equals("A",true)){
                holder.binding.llAttendanceEmployee.visibility = View.GONE
                holder.binding.txtAttendanceSwitch.isChecked = false
                clickTrue = false
                holder.binding.txtAttendanceStatus.background = mContext.getDrawable(R.drawable.light_red_rounded_radius_2_bg)
                holder.binding.txtAttendanceStatus.text = mContext.getString(R.string.absent)
                holder.binding.txtAttendanceStatus.visible()
            }else if(model.attendanceInd.equals("L",true)){
                holder.binding.llAttendanceEmployee.visibility = View.GONE
                holder.binding.txtAttendanceSwitch.isChecked = false
                clickTrue = false
                holder.binding.txtAttendanceStatus.background = mContext.getDrawable(R.drawable.light_blue_rounded_radius_2_bg)
                holder.binding.txtAttendanceStatus.text = mContext.getString(R.string.on_leave)
                holder.binding.txtAttendanceStatus.visible()
            }else if(model.attendanceInd.equals("W",true)){
                holder.binding.llAttendanceEmployee.visibility = View.GONE
                holder.binding.txtAttendanceSwitch.isChecked = false
                clickTrue = false
                holder.binding.txtAttendanceStatus.background = mContext.getDrawable(R.drawable.yellow_radious_2_bg)
                holder.binding.txtAttendanceStatus.text = mContext.getString(R.string.week_off)
                holder.binding.txtAttendanceStatus.visible()
            }else if(model.attendanceInd.equals("H",true)){
                holder.binding.llAttendanceEmployee.visibility = View.GONE
                holder.binding.txtAttendanceSwitch.isChecked = false
                clickTrue = false
                holder.binding.txtAttendanceStatus.background = mContext.getDrawable(R.drawable.light_black_radious_2_bg)
                holder.binding.txtAttendanceStatus.text = mContext.getString(R.string.holiday)
                holder.binding.txtAttendanceStatus.visible()
            }

        }else{
//            holder.binding.llAttendanceEmployee.visibility = View.GONE
//            holder.binding.llAttendance.visibility = View.VISIBLE

            if(model.attendanceInd.equals("P",true)){
                holder.binding.llAttendance.visibility = View.GONE
                holder.binding.txtAttendanceSwitch.isChecked = true
                clickTrue = true
                if(model.lateEntryFlag.equals("Y",true)){
                    holder.binding.txtAttendanceStatus.background = mContext.getDrawable(R.drawable.orange_rounded_radius_2_bg)
                    holder.binding.txtAttendanceStatus.text = mContext.getString(R.string.late)
                    holder.binding.txtAttendanceStatus.visible()
                }else{
                    holder.binding.txtAttendanceStatus.background = mContext.getDrawable(R.drawable.green_radious_2_bg)
                    holder.binding.txtAttendanceStatus.text = mContext.getString(R.string.present)
                    holder.binding.txtAttendanceStatus.visible()
                }

            }else if(model.attendanceInd.equals("A",true)){
                holder.binding.llAttendance.visibility = View.GONE
                holder.binding.txtAttendanceSwitch.isChecked = false
                clickTrue = false
                holder.binding.txtAttendanceStatus.background = mContext.getDrawable(R.drawable.light_red_rounded_radius_2_bg)
                holder.binding.txtAttendanceStatus.text = mContext.getString(R.string.absent)
                holder.binding.txtAttendanceStatus.visible()
            }else if(model.attendanceInd.equals("L",true)){
                holder.binding.llAttendance.visibility = View.GONE
                holder.binding.txtAttendanceSwitch.isChecked = false
                clickTrue = false
                holder.binding.txtAttendanceStatus.background = mContext.getDrawable(R.drawable.light_blue_rounded_radius_2_bg)
                holder.binding.txtAttendanceStatus.text = mContext.getString(R.string.on_leave)
                holder.binding.txtAttendanceStatus.visible()
            }else if(model.attendanceInd.equals("W",true)){
                holder.binding.llAttendance.visibility = View.GONE
                holder.binding.txtAttendanceSwitch.isChecked = false
                clickTrue = false
                holder.binding.txtAttendanceStatus.background = mContext.getDrawable(R.drawable.yellow_radious_2_bg)
                holder.binding.txtAttendanceStatus.text = mContext.getString(R.string.week_off)
                holder.binding.txtAttendanceStatus.visible()
            }else if(model.attendanceInd.equals("H",true)){
                holder.binding.llAttendance.visibility = View.GONE
                holder.binding.txtAttendanceSwitch.isChecked = false
                clickTrue = false
                holder.binding.txtAttendanceStatus.background = mContext.getDrawable(R.drawable.light_black_radious_2_bg)
                holder.binding.txtAttendanceStatus.text = mContext.getString(R.string.holiday)
                holder.binding.txtAttendanceStatus.visible()
            }

        }

        holder.binding.txtAttendanceSwitch.setOnClickListener{
            clickTrue = !clickTrue
            if(from.equals("Employee")){
                if(clickTrue){
                    if(model.attendanceInd!= null && !model.attendanceInd.equals("")){
                        model.attendanceInd = "P"
                        notifyItemChanged(position)
                    }
                    holder.binding.llAttendanceEmployee.visibility = View.GONE
                }else{
                    holder.binding.llAttendanceEmployee.visibility = View.VISIBLE
                }
            }else{
                if(clickTrue){
                    if(model.attendanceInd!= null && !model.attendanceInd.equals("")){
                        model.attendanceInd = "P"
                        notifyItemChanged(position)
                    }
                    holder.binding.llAttendance.visibility = View.GONE
                }else{
                    holder.binding.llAttendance.visibility = View.VISIBLE
                }
            }

        }


//        holder.binding.txtAttendanceSwitch.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
//            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
//
//                if(from.equals("Employee")){
//                    if(p1){
//                        if(model.attendanceInd!= null && !model.attendanceInd.equals("")){
//                            model.attendanceInd = "P"
//                            notifyItemChanged(position)
//                        }
//                        holder.binding.llAttendanceEmployee.visibility = View.GONE
//                    }else{
//                        holder.binding.llAttendanceEmployee.visibility = View.VISIBLE
//                    }
//                }else{
//                    if(p1){
//                        holder.binding.llAttendance.visibility = View.GONE
//                    }else{
//                        holder.binding.llAttendance.visibility = View.VISIBLE
//                    }
//                }
//
//            }
//        })
        //student
        holder.binding.txtAbsent.setOnClickListener{
            model.attendanceInd = "A"
            holder.binding.llAttendance.visibility = View.GONE
            notifyItemChanged(position)
        }

        holder.binding.txtLeave.setOnClickListener{
            model.attendanceInd = "L"
            holder.binding.llAttendance.visibility = View.GONE
            notifyItemChanged(position)

        }

        holder.binding.txtLate.setOnClickListener{
            model.attendanceInd = "P"
            model.lateEntryFlag = "Y"
            holder.binding.llAttendance.visibility = View.GONE
            notifyItemChanged(position)
        }

        //employee
        holder.binding.txtAbsentEmployee.setOnClickListener{
            model.attendanceInd = "A"
            holder.binding.llAttendanceEmployee.visibility = View.GONE
            notifyItemChanged(position)
        }

        holder.binding.txtLeaveEmployee.setOnClickListener{
            model.attendanceInd = "L"
            holder.binding.llAttendanceEmployee.visibility = View.GONE
            notifyItemChanged(position)

        }

        holder.binding.txtLateEmployee.setOnClickListener{
            model.attendanceInd = "P"
            model.lateEntryFlag = "Y"
            holder.binding.llAttendanceEmployee.visibility = View.GONE
            notifyItemChanged(position)
        }

        holder.binding.txtWeekOffEmployee.setOnClickListener{
            model.attendanceInd = "W"
            holder.binding.llAttendanceEmployee.visibility = View.GONE
            notifyItemChanged(position)
        }

        holder.binding.cLDetail.setOnClickListener {

        }

    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: AttendanceEntryCellLayoutBinding = AttendanceEntryCellLayoutBinding.bind(itemView)

    }
}
