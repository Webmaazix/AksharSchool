package com.akshar.one.view.attendance.employee.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.databinding.EmployeeAttendanceEntryCellLayoutBinding
import com.akshar.one.extension.gone
import com.akshar.one.extension.visible
import com.akshar.one.model.StudentAttendanceModel
import com.akshar.one.viewmodels.attendance.AttendanceEntryViewModel
import com.akshar.one.viewmodels.attendance.EmployeeAttendanceEntryViewModel

class EmployeeAttendanceAdapter(private val employeeAttendanceEntryViewModel: EmployeeAttendanceEntryViewModel) :
    RecyclerView.Adapter<EmployeeAttendanceAdapter.Holder>() {

    private var studentAttendanceList: MutableList<StudentAttendanceModel>? = null

    fun setStudentAttendanceList(studentAttendanceList: List<StudentAttendanceModel>?) {
        this.studentAttendanceList?.clear()
        studentAttendanceList?.let {
            this.studentAttendanceList = it as MutableList
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val employeeAttendanceEntryCellLayoutBinding =
            DataBindingUtil.inflate<EmployeeAttendanceEntryCellLayoutBinding>(
                LayoutInflater.from(parent.context),
                R.layout.employee_attendance_entry_cell_layout,
                parent,
                false
            )
        return Holder(
            parent.context,
            employeeAttendanceEntryCellLayoutBinding,
            employeeAttendanceEntryViewModel
        )
    }

    override fun getItemCount(): Int {
        return studentAttendanceList?.size ?: 0
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(position)
    }

    class Holder(
        private val context: Context,
        private val employeeAttendanceEntryCellLayoutBinding: EmployeeAttendanceEntryCellLayoutBinding,
        private val employeeAttendanceEntryViewModel: EmployeeAttendanceEntryViewModel
    ) : RecyclerView.ViewHolder(employeeAttendanceEntryCellLayoutBinding.root),
        CompoundButton.OnCheckedChangeListener, View.OnClickListener {

        private var pos = 0

        fun bind(position: Int) {
            this.pos = position
            employeeAttendanceEntryCellLayoutBinding.employeeAttendanceEntryViewModel =
                employeeAttendanceEntryViewModel
            employeeAttendanceEntryCellLayoutBinding.position = position
            updateUI()
            employeeAttendanceEntryCellLayoutBinding.txtAttendanceSwitch.setOnCheckedChangeListener(
                this
            )
            employeeAttendanceEntryCellLayoutBinding.txtAbsent.setOnClickListener(this)
            employeeAttendanceEntryCellLayoutBinding.txtLate.setOnClickListener(this)
            employeeAttendanceEntryCellLayoutBinding.txtLeave.setOnClickListener(this)
            employeeAttendanceEntryCellLayoutBinding.executePendingBindings()
        }

        private fun updateUI() {
            val isChecked =
                employeeAttendanceEntryViewModel.getEmployeeAt(pos)?.attendanceInd?.equals(
                    AttendanceEntryViewModel.PRESENT,
                    true
                ) == true
            employeeAttendanceEntryCellLayoutBinding.txtAttendanceSwitch.isChecked = isChecked
            if (isChecked) {
                employeeAttendanceEntryCellLayoutBinding.llAttendance.gone()
            } else {
                employeeAttendanceEntryCellLayoutBinding.llAttendance.visible()
            }
        }

        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
            if (isChecked) {
                employeeAttendanceEntryCellLayoutBinding.llAttendance.gone()
                updateAttendanceStatusLabel(AttendanceEntryViewModel.PRESENT)
            } else {
                employeeAttendanceEntryCellLayoutBinding.llAttendance.visible()
            }
        }

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.txtAbsent -> {
                    updateAttendanceStatusLabel(AttendanceEntryViewModel.ABSENT)
                }
                R.id.txtLate -> {
                    updateAttendanceStatusLabel(AttendanceEntryViewModel.LATE_ENTRY)
                }
                R.id.txtLeave -> {
                    updateAttendanceStatusLabel(AttendanceEntryViewModel.LEAVE)
                }

            }
        }

        private fun updateAttendanceStatusLabel(status: String) {

            when (status) {
                AttendanceEntryViewModel.PRESENT -> {
                    employeeAttendanceEntryCellLayoutBinding.txtAttendanceStatus.gone()
                }
                AttendanceEntryViewModel.ABSENT -> {
                    employeeAttendanceEntryCellLayoutBinding.txtAttendanceStatus.background =
                        context.getDrawable(R.drawable.light_red_rounded_radius_2_bg)
                    employeeAttendanceEntryCellLayoutBinding.txtAttendanceStatus.text =
                        context.getString(R.string.absent)
                    employeeAttendanceEntryCellLayoutBinding.txtAttendanceStatus.visible()
                }
                AttendanceEntryViewModel.LEAVE -> {
                    employeeAttendanceEntryCellLayoutBinding.txtAttendanceStatus.background =
                        context.getDrawable(R.drawable.light_blue_rounded_radius_2_bg)
                    employeeAttendanceEntryCellLayoutBinding.txtAttendanceStatus.text =
                        context.getString(R.string.on_leave)
                    employeeAttendanceEntryCellLayoutBinding.txtAttendanceStatus.visible()
                }
                AttendanceEntryViewModel.LATE_ENTRY -> {
                    employeeAttendanceEntryCellLayoutBinding.txtAttendanceStatus.background =
                        context.getDrawable(R.drawable.orange_rounded_radius_2_bg)
                    employeeAttendanceEntryCellLayoutBinding.txtAttendanceStatus.text =
                        context.getString(R.string.late)
                    employeeAttendanceEntryCellLayoutBinding.txtAttendanceStatus.visible()
                }

            }
            employeeAttendanceEntryViewModel.updateAttendance(pos, status)
        }

    }
}