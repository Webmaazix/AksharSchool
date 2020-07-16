package com.akshar.one.view.common

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.akshar.one.R
import com.akshar.one.databinding.SelectClassAndSectionDialogBinding
import com.akshar.one.view.activity.MainActivity
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.common.ClassAndSectionViewModel

class ClassAndSectionDialog : DialogFragment() {

    private var mainActivity: MainActivity? = null
    private var selectClassAndSectionDialogBinding: SelectClassAndSectionDialogBinding? = null
    private var classAndSectionViewModel: ClassAndSectionViewModel? = null
    private var onClassRoomSelectedListener: OnClassRoomSelectedListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        selectClassAndSectionDialogBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.select_class_and_section_dialog,
            container,
            false
        )
        return selectClassAndSectionDialogBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainActivity?.application?.let {
            classAndSectionViewModel =
                ViewModelProvider(ViewModelStore(), ViewModelFactory(it)).get(
                    ClassAndSectionViewModel::class.java
                )
            selectClassAndSectionDialogBinding?.classAndSectionViewModel = classAndSectionViewModel

            observers()
            fetchCourse()
//            fetchDegree()
        }
    }

    private fun fetchDegree() {
        classAndSectionViewModel?.getDegreeWithDeptFromDB()
    }

    private fun fetchCourse() {
        classAndSectionViewModel?.getCoursesWithClassRoomFromDB()
    }

    private fun initView() {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        selectClassAndSectionDialogBinding?.imgClose?.setOnClickListener { dismiss() }
    }

    private fun observers() {

//        classAndSectionViewModel?.getDegreeListMutableLiveData()
//            ?.observe(this, Observer {
//                classAndSectionViewModel?.getClassAndSectionAdapter()?.setIsCollege(true)
//                classAndSectionViewModel?.setDegreeWithDeptInAdapter(it)
//            })

        classAndSectionViewModel?.getCourseWithClassRoomMutableLiveDataList()
            ?.observe(this, Observer {
                classAndSectionViewModel?.setCourseWithClassroomListInAdapter(it)
            })

        classAndSectionViewModel?.getSelectedClassRoomEntityLiveData()?.observe(this, Observer {
            it?.let {
                classAndSectionViewModel?.setSelectedClassRoomEntityLiveData(null)
                dismiss()
                classAndSectionViewModel?.getSelectedCourseEntityLiveData()?.value?.let { courseEntity ->
                    classAndSectionViewModel?.setSelectedCourseEntityLiveData(null)
                    onClassRoomSelectedListener?.onClassRoomSelectedListener(
                        courseEntity, it)
                }
            }
        })
    }

    companion object {
        fun newInstance(
            onClassRoomSelectedListener: OnClassRoomSelectedListener?
        ): ClassAndSectionDialog {
            val classAndSectionDialog =
                ClassAndSectionDialog()
            classAndSectionDialog.onClassRoomSelectedListener = onClassRoomSelectedListener
            return classAndSectionDialog
        }
    }
}