package com.akshar.one.view.common.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.databinding.SelectCollegeYearCellBinding
import com.akshar.one.model.CourseWithClassRoomModel
import com.akshar.one.viewmodels.common.ClassAndSectionViewModel

class DepartmentCourseDialogAdapter(
    private val classAndSectionViewModel: ClassAndSectionViewModel,
    private val courseList: List<CourseWithClassRoomModel>?
) :
    RecyclerView.Adapter<DepartmentCourseDialogAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val selectCollegeYearCellBinding = DataBindingUtil.inflate<SelectCollegeYearCellBinding>(
            LayoutInflater.from(parent.context),
            R.layout.select_college_year_cell,
            parent,
            false
        )
        return Holder(
            selectCollegeYearCellBinding,
            classAndSectionViewModel
        )
    }

    override fun getItemCount(): Int {
        return courseList?.size ?: 0
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(position, courseList?.get(position))
    }

    class Holder(
        private val selectCollegeYearCellBinding: SelectCollegeYearCellBinding,
        private val classAndSectionViewModel: ClassAndSectionViewModel
    ) : RecyclerView.ViewHolder(selectCollegeYearCellBinding.root) {

        fun bind(position: Int, courseModel: CourseWithClassRoomModel?) {
            updateUI(position, courseModel)
            selectCollegeYearCellBinding.executePendingBindings()
        }

        private fun updateUI(position: Int, courseModel: CourseWithClassRoomModel?) {
            selectCollegeYearCellBinding.txtClgYear.text = courseModel?.courseEntity?.courseName
            selectCollegeYearCellBinding.rvSection.adapter =
                ClassRoomDialogAdapter(
                    classAndSectionViewModel,
                    courseModel
                )

        }

    }
}