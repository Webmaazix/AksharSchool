package com.akshar.one.view.attendance.student.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.databinding.AttendanceEntryCellLayoutBinding
import com.akshar.one.extension.gone
import com.akshar.one.extension.visible
import com.akshar.one.model.StudentAttendanceModel
import com.akshar.one.viewmodels.attendance.AttendanceEntryViewModel

class StudentAttendanceAdapter(private val attendanceEntryViewModel: AttendanceEntryViewModel): RecyclerView.Adapter<StudentAttendanceAdapter.Holder>() {

    private var studentAttendanceList: MutableList<StudentAttendanceModel>? = null

    fun setStudentAttendanceList(studentAttendanceList: List<StudentAttendanceModel>?){
        this.studentAttendanceList?.clear()
        studentAttendanceList?.let {
            this.studentAttendanceList = it as MutableList
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val attendanceEntryCellLayoutBinding = DataBindingUtil.inflate<AttendanceEntryCellLayoutBinding>(LayoutInflater.from(parent.context), R.layout.attendance_entry_cell_layout, parent,  false)
        return Holder(
            parent.context,
            attendanceEntryCellLayoutBinding,
            attendanceEntryViewModel
        )
    }

    override fun getItemCount(): Int {
        return studentAttendanceList?.size ?: 0
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(position)
    }

    class Holder(private val context: Context, private val attendanceEntryCellLayoutBinding: AttendanceEntryCellLayoutBinding, private val attendanceEntryViewModel: AttendanceEntryViewModel) : RecyclerView.ViewHolder(attendanceEntryCellLayoutBinding.root), CompoundButton.OnCheckedChangeListener, View.OnClickListener {

        private var pos = 0

        fun bind(position: Int) {
            this.pos = position
            attendanceEntryCellLayoutBinding.attendanceEntryViewModel = attendanceEntryViewModel
            attendanceEntryCellLayoutBinding.position = position
            updateUI()
            attendanceEntryCellLayoutBinding.txtAttendanceSwitch.setOnCheckedChangeListener(this)
            attendanceEntryCellLayoutBinding.txtAbsent.setOnClickListener(this)
            attendanceEntryCellLayoutBinding.txtLate.setOnClickListener(this)
            attendanceEntryCellLayoutBinding.txtLeave.setOnClickListener(this)
            attendanceEntryCellLayoutBinding.executePendingBindings()
        }

        private fun updateUI() {
            val isChecked = attendanceEntryViewModel.getStudentAt(pos)?.attendanceInd?.equals(AttendanceEntryViewModel.PRESENT,true) == true
            attendanceEntryCellLayoutBinding.txtAttendanceSwitch.isChecked = isChecked
            if(isChecked){
                attendanceEntryCellLayoutBinding.llAttendance.gone()
            }else{
                attendanceEntryCellLayoutBinding.llAttendance.visible()
            }
        }

        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
            if(isChecked){
                attendanceEntryCellLayoutBinding.llAttendance.gone()
                updateAttendanceStatusLabel(AttendanceEntryViewModel.PRESENT)
            }else{
                attendanceEntryCellLayoutBinding.llAttendance.visible()
            }
        }

        override fun onClick(v: View?) {
            when(v?.id){
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

            when(status){
                AttendanceEntryViewModel.PRESENT -> {
                    attendanceEntryCellLayoutBinding.txtAttendanceStatus.gone()
                }
                AttendanceEntryViewModel.ABSENT -> {
                    attendanceEntryCellLayoutBinding.txtAttendanceStatus.background = context.getDrawable(R.drawable.light_red_rounded_radius_2_bg)
                    attendanceEntryCellLayoutBinding.txtAttendanceStatus.text = context.getString(R.string.absent)
                    attendanceEntryCellLayoutBinding.txtAttendanceStatus.visible()
                }
                AttendanceEntryViewModel.LEAVE -> {
                    attendanceEntryCellLayoutBinding.txtAttendanceStatus.background = context.getDrawable(R.drawable.light_blue_rounded_radius_2_bg)
                    attendanceEntryCellLayoutBinding.txtAttendanceStatus.text = context.getString(R.string.on_leave)
                    attendanceEntryCellLayoutBinding.txtAttendanceStatus.visible()
                }
                AttendanceEntryViewModel.LATE_ENTRY -> {
                    attendanceEntryCellLayoutBinding.txtAttendanceStatus.background = context.getDrawable(R.drawable.orange_rounded_radius_2_bg)
                    attendanceEntryCellLayoutBinding.txtAttendanceStatus.text = context.getString(R.string.late)
                    attendanceEntryCellLayoutBinding.txtAttendanceStatus.visible()
                }

            }
            attendanceEntryViewModel.updateAttendance(pos, status)
        }

    }
}