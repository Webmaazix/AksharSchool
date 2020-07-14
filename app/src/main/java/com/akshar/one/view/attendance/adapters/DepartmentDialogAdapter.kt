package com.akshar.one.view.attendance.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.databinding.SelectDepartmentCellBinding
import com.akshar.one.model.DepartmentModel
import com.akshar.one.model.DeptWithCourseModel
import com.akshar.one.viewmodels.attendance.ClassAndSectionViewModel

class DepartmentDialogAdapter(
    private val classAndSectionViewModel: ClassAndSectionViewModel,
    private val departmentList: List<DeptWithCourseModel>?
) :
    RecyclerView.Adapter<DepartmentDialogAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val selectDepartmentCellBinding = DataBindingUtil.inflate<SelectDepartmentCellBinding>(
            LayoutInflater.from(parent.context),
            R.layout.select_department_cell,
            parent,
            false
        )
        return Holder(selectDepartmentCellBinding, classAndSectionViewModel)
    }

    override fun getItemCount(): Int {
        return departmentList?.size ?: 0
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(position, departmentList?.get(position))
    }

    class Holder(
        private val selectDepartmentCellBinding: SelectDepartmentCellBinding,
        private val classAndSectionViewModel: ClassAndSectionViewModel
    ) : RecyclerView.ViewHolder(selectDepartmentCellBinding.root) {

        fun bind(position: Int, departmentModel: DeptWithCourseModel?) {
            updateUI(position, departmentModel)
            selectDepartmentCellBinding.executePendingBindings()
        }

        private fun updateUI(position: Int, departmentModel: DeptWithCourseModel?) {
            selectDepartmentCellBinding.txtTitle.text = departmentModel?.departmentEntity?.departmentName
            selectDepartmentCellBinding.rvClassDegree.adapter = DepartmentCourseDialogAdapter(
                classAndSectionViewModel,
                departmentModel?.courseList
            )
        }

    }
}