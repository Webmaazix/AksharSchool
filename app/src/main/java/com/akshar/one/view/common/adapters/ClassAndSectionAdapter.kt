package com.akshar.one.view.common.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.databinding.SelectClassOrDegreeCellBinding
import com.akshar.one.model.CourseWithClassRoomModel
import com.akshar.one.model.DegreeWithDeptModel
import com.akshar.one.viewmodels.common.ClassAndSectionViewModel

class ClassAndSectionAdapter(private val classAndSectionViewModel: ClassAndSectionViewModel) :
    RecyclerView.Adapter<ClassAndSectionAdapter.Holder>() {

    private var isCollege: Boolean = false
//    private var degreeList: List<DegreeModel>? = null

    //    private var courseList: List<CourseModel>? = null
    private var courseWithClassRoomList: List<CourseWithClassRoomModel>? = null
    private var degreeWithDeptList: List<DegreeWithDeptModel>? = null

    fun setIsCollege(isCollege: Boolean) {
        this.isCollege = isCollege
    }

//    fun setDegreeList(degreeList: List<DegreeModel>?) {
//        degreeList?.let {
//            this.degreeList = it
//            notifyDataSetChanged()
//        }
//    }

//    fun setCourseList(courseList: List<CourseModel>?) {
//        courseList?.let {
//            this.courseList = it
//            notifyDataSetChanged()
//        }
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val selectClassOrDegreeCellBinding =
            DataBindingUtil.inflate<SelectClassOrDegreeCellBinding>(
                LayoutInflater.from(parent.context),
                R.layout.select_class_or_degree_cell,
                parent,
                false
            )
        return Holder(
            selectClassOrDegreeCellBinding,
            classAndSectionViewModel,
            isCollege
        )
    }

    override fun getItemCount(): Int {
        return if (isCollege) degreeWithDeptList?.size ?: 0 else courseWithClassRoomList?.size ?: 0
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(position)
    }

    fun setCourseWithClassroomList(courseWithClassRoomList: List<CourseWithClassRoomModel>?) {
        courseWithClassRoomList?.let {
            this.courseWithClassRoomList = courseWithClassRoomList
            notifyDataSetChanged()
        }
    }

    fun setDegreeWithDeptList(degreeWithDeptList: List<DegreeWithDeptModel>?) {
        degreeWithDeptList?.let {
            this.degreeWithDeptList = degreeWithDeptList
            notifyDataSetChanged()
        }
    }

    class Holder(
        private val selectClassOrDegreeCellBinding: SelectClassOrDegreeCellBinding,
        private val classAndSectionViewModel: ClassAndSectionViewModel,
        private val isCollege: Boolean
    ) : RecyclerView.ViewHolder(selectClassOrDegreeCellBinding.root) {

        fun bind(position: Int) {
            updateUI(position)
            selectClassOrDegreeCellBinding.executePendingBindings()
        }

        private fun updateUI(position: Int) {
            if (isCollege) {
                val degreeModel = classAndSectionViewModel.getDegreeWithDeptModel(position)
                selectClassOrDegreeCellBinding.txtTitle.text = degreeModel?.degreeEntity?.degreeName
                selectClassOrDegreeCellBinding.rvClassDegree.adapter =
                    DepartmentDialogAdapter(
                        classAndSectionViewModel,
                        degreeModel?.deptList
                    )
            } else {
                val courseModel = classAndSectionViewModel.getCourseWithClassRoomModel(position)
                selectClassOrDegreeCellBinding.txtTitle.text = courseModel?.courseEntity?.courseName
                selectClassOrDegreeCellBinding.rvClassDegree.adapter =
                    ClassRoomDialogAdapter(
                        classAndSectionViewModel,
                        courseModel
                    )
            }
        }

    }
}