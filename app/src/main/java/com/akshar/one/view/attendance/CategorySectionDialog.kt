package com.akshar.one.view.attendance

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.akshar.one.R
import com.akshar.one.database.entity.ShiftEntity
import com.akshar.one.view.activity.MainActivity
import com.akshar.one.view.attendance.student.adapter.AttendanceCategoryAdapter
import kotlinx.android.synthetic.main.select_category_dialog.view.*

class CategorySectionDialog : DialogFragment(),
    AttendanceCategoryListener {

    private var mainActivity: MainActivity? = null
    private var attendanceCategoryList: List<ShiftEntity>? = null
    private var attendanceCategoryListener: AttendanceCategoryListener? = null
    private var baseView: View? = null
    private var attendanceCategoryAdapter: AttendanceCategoryAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        baseView = inflater.inflate(R.layout.select_category_dialog, container, false)
        return baseView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun initView() {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        attendanceCategoryAdapter = mainActivity?.let {
            AttendanceCategoryAdapter(
                it,
                attendanceCategoryList,
                this
            )
        }
        baseView?.recyclerView?.adapter = attendanceCategoryAdapter

        baseView?.imgClose?.setOnClickListener { dismiss() }
    }

    companion object {
        fun newInstance(
            attendanceCategoryList: List<ShiftEntity>?,
            attendanceCategoryListener: AttendanceCategoryListener?
        ): CategorySectionDialog {
            val categorySectionDialog = CategorySectionDialog()
            categorySectionDialog.attendanceCategoryList = attendanceCategoryList
            categorySectionDialog.attendanceCategoryListener = attendanceCategoryListener
            return categorySectionDialog
        }
    }

    override fun updateAttendanceCategory(shiftEntityList: List<ShiftEntity>?) {
    }

    override fun onAttendanceCategorySelected(shiftEntity: ShiftEntity) {
        dismiss()
        attendanceCategoryListener?.onAttendanceCategorySelected(shiftEntity)
    }
}