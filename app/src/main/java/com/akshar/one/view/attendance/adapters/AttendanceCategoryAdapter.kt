package com.akshar.one.view.attendance.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.akshar.one.R
import com.akshar.one.database.entity.AttendanceCategoryEntity
import com.akshar.one.viewmodels.attendance.AttendanceClassRoomViewModel
import kotlinx.android.synthetic.main.attendance_category_drop_down_cell.view.*

class AttendanceCategoryAdapter() : BaseAdapter() {

    private lateinit var layoutInflater: LayoutInflater

    constructor(attendanceClassRoomViewModel: AttendanceClassRoomViewModel) : this() {
        layoutInflater = LayoutInflater.from(attendanceClassRoomViewModel.getApplication())
    }

    private var attendanceCategoryEntityList: MutableList<AttendanceCategoryEntity>? = ArrayList()

    fun setAttendanceCategoryEntityList(attendanceCategoryEntityList: List<AttendanceCategoryEntity>?) {
        this.attendanceCategoryEntityList?.clear()
        attendanceCategoryEntityList?.let {
            this.attendanceCategoryEntityList?.addAll(it)
        }
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view =
            layoutInflater.inflate(R.layout.attendance_category_drop_down_cell, parent, false)
        view?.txtAttendanceCategory?.text = getItem(position)?.category
        return view
    }

    override fun getItem(position: Int): AttendanceCategoryEntity? =
        attendanceCategoryEntityList?.get(position)

    override fun getItemId(position: Int): Long =
        attendanceCategoryEntityList?.get(position)?.id ?: 0

    override fun getCount(): Int = attendanceCategoryEntityList?.size ?: 0
}