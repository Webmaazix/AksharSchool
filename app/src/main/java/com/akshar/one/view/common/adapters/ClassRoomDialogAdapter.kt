package com.akshar.one.view.common.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.databinding.SelectSectionCellBinding
import com.akshar.one.model.CourseWithClassRoomModel
import com.akshar.one.viewmodels.common.ClassAndSectionViewModel

class ClassRoomDialogAdapter(
    private val classAndSectionViewModel: ClassAndSectionViewModel,
    private val courseModel: CourseWithClassRoomModel?
) :
    RecyclerView.Adapter<ClassRoomDialogAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val selectSectionCellBinding = DataBindingUtil.inflate<SelectSectionCellBinding>(
            LayoutInflater.from(parent.context),
            R.layout.select_section_cell,
            parent,
            false
        )
        return Holder(
            selectSectionCellBinding,
            classAndSectionViewModel,
            courseModel
        )
    }

    override fun getItemCount(): Int {
        return courseModel?.classRoomList?.size ?: 0
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(position)
    }

    class Holder(
        private val selectSectionCellBinding: SelectSectionCellBinding,
        private val classAndSectionViewModel: ClassAndSectionViewModel,
        private val courseModel: CourseWithClassRoomModel?
    ) : RecyclerView.ViewHolder(selectSectionCellBinding.root) {

        fun bind(position: Int) {
            updateUI(position)
            selectSectionCellBinding.txtSection.setOnClickListener {
             classAndSectionViewModel.onClassRoomSectionClick(courseModel?.courseEntity, courseModel?.classRoomList?.get(position))
            }
            selectSectionCellBinding.executePendingBindings()
        }

        private fun updateUI(position: Int) {
            val classRoomModel = courseModel?.classRoomList?.get(position)
            selectSectionCellBinding.txtSection.text = classRoomModel?.classroomName
        }

    }
}