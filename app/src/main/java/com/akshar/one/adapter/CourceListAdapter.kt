package com.akshar.one.adapter

import android.app.Application
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.database.entity.ClassRoomEntity
import com.akshar.one.database.entity.CourseEntity
import com.akshar.one.databinding.AttendanceCourseCellLayoutBinding
import com.akshar.one.databinding.BirthdayCellBinding
import com.akshar.one.databinding.ClassroomBottomSheetDialogFragmentBinding
import com.akshar.one.databinding.HomeworkCellBinding
import com.akshar.one.model.BirthDayModel
import com.akshar.one.model.TimeTableModel
import com.akshar.one.util.AndroidUtil
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.attendance.AttendanceClassRoomViewModel
import com.akshar.one.viewmodels.attendance.AttendanceCourseViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.*
import kotlin.collections.ArrayList

class CourceListAdapter(private val mContext: Context, private val courceModel: ArrayList<CourseEntity>?) :
    RecyclerView.Adapter<CourceListAdapter.ViewHolder>() {
    private var bottomSheetDialog: BottomSheetDialog? = null
    private var classroomBottomSheetDialogFragmentBinding : ClassroomBottomSheetDialogFragmentBinding? = null
    private var attendanceCourseViewModel: AttendanceCourseViewModel? = null
    private var classRoomEntityList = ArrayList<ClassRoomEntity>()
    private lateinit var adapter : ClassSectionAdapter





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.attendance_course_cell_layout,
                parent,
                false
            )
        )

        attendanceCourseViewModel = ViewModelProvider(
                    ViewModelStore(),
                    ViewModelFactory(mContext.applicationContext as Application)
                ).get(AttendanceCourseViewModel::class.java)

        return view
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val courceModel = courceModel?.get(position)
        holder.binding.txtTitle.text = courceModel!!.courseName
        holder.binding.cvMain.setOnClickListener{
            attendanceCourseViewModel?.let {
                if (mContext.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                    it.getClassSection(courceModel)
                }
            }

            showBottomSheetDialog(position)
        }
    }

    override fun getItemCount(): Int {
        return courceModel!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: AttendanceCourseCellLayoutBinding = AttendanceCourseCellLayoutBinding.bind(itemView)

    }


    private fun showBottomSheetDialog(position: Int) {
        bottomSheetDialog = BottomSheetDialog(mContext)
        classroomBottomSheetDialogFragmentBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.classroom_bottom_sheet_dialog_fragment,
            null,
            false
        )
        classroomBottomSheetDialogFragmentBinding!!.recyclerView.setHasFixedSize(true)
        classroomBottomSheetDialogFragmentBinding!!.recyclerView.layoutManager =
            LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false)
        adapter = ClassSectionAdapter(mContext,classRoomEntityList)
        classroomBottomSheetDialogFragmentBinding!!.recyclerView.adapter = adapter


        bottomSheetDialog!!.setContentView(classroomBottomSheetDialogFragmentBinding!!.getRoot())
        Objects.requireNonNull<Window>(bottomSheetDialog!!.getWindow())
            .setBackgroundDrawableResource(android.R.color.transparent)


        attendanceCourseViewModel?.getClassRoomListMutableLiveData()?.observe(mContext as LifecycleOwner,androidx.lifecycle.Observer {
            classRoomEntityList.clear()
            classRoomEntityList.addAll(it)
            adapter.notifyDataSetChanged()
        })

        bottomSheetDialog!!.show()
    }
}
