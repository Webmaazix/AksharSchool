package com.akshar.one.view.attendance.student

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.akshar.one.R
import com.akshar.one.calender.widget.CollapsibleCalendar
import com.akshar.one.database.entity.ClassRoomEntity
import com.akshar.one.database.entity.CourseEntity
import com.akshar.one.database.entity.ShiftEntity
import com.akshar.one.databinding.FragmentAttendanceEntryBinding
import com.akshar.one.extension.isInForeground
import com.akshar.one.util.AndroidUtil
import com.akshar.one.util.AppUtil
import com.akshar.one.view.activity.MainActivity
import com.akshar.one.view.attendance.AttendanceCategoryListener
import com.akshar.one.view.attendance.CategorySectionDialog
import com.akshar.one.view.common.ClassAndSectionDialog
import com.akshar.one.view.common.OnClassRoomSelectedListener
import com.akshar.one.view.fragment.BaseFragment
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.attendance.AttendanceEntryViewModel
import kotlinx.android.synthetic.main.fragment_attendance_entry.*

class AttendanceEntryFragment : BaseFragment(), View.OnClickListener,
    OnClassRoomSelectedListener,
    AttendanceCategoryListener, CollapsibleCalendar.CalendarListener {

    private var classRoomEntity: ClassRoomEntity? = null
    private var shiftEntity: ShiftEntity? = null

    private var fragmentAttendanceEntryBinding: FragmentAttendanceEntryBinding? = null
    private var attendanceEntryViewModel: AttendanceEntryViewModel? = null
    private var mainActivity: MainActivity? = null

    private var currentDate: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentAttendanceEntryBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_attendance_entry,
            container,
            false
        )
        return fragmentAttendanceEntryBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.application?.let {
            attendanceEntryViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(AttendanceEntryViewModel::class.java)
        }
        fragmentAttendanceEntryBinding?.attendanceEntryViewModel = attendanceEntryViewModel

        mainActivity?.setToolbarBackground(false)
        mainActivity?.setToolbarTitle(context?.getString(R.string.student_attendance_entry))

        observers()
    }

    private fun fetchAttendanceStudents() {
        if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
            classRoomEntity?.classroomId?.let { classroomId ->
                shiftEntity?.shiftId?.let { shiftId ->
                    currentDate?.let { date ->
                        attendanceEntryViewModel?.getStudentAttendanceByClassRoomId(
                            classroomId,
                            shiftId,
                            date
                        )
                    }
                }
            }
        } else {
            Toast.makeText(
                context,
                getString(R.string.no_internet_available),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun observers() {

        attendanceEntryViewModel?.getIsLoading()?.observe(this, Observer {
            showProgressIndicator(it)
        })

        attendanceEntryViewModel?.getStudentAttendanceListMutableLiveData()
            ?.observe(this, Observer {

                attendanceEntryViewModel?.setStudentAttendanceListInAdapter(it)

            })
    }

    private fun initViews() {

        currentDate = fragmentAttendanceEntryBinding?.collapsibleCalendarView?.selectedDay?.let {
            AppUtil.getServerDateFormat(it)
        }
        fragmentAttendanceEntryBinding?.btnSave?.setOnClickListener(this)
        fragmentAttendanceEntryBinding?.rlMarkAll?.setOnClickListener(this)
        fragmentAttendanceEntryBinding?.rLClassAndSection?.setOnClickListener(this)
        fragmentAttendanceEntryBinding?.rLCategory?.setOnClickListener(this)
        fragmentAttendanceEntryBinding?.collapsibleCalendarView?.setCalendarListener(this)
        fragmentAttendanceEntryBinding?.imgExpand?.setOnClickListener(this)
    }

    private fun showProgressIndicator(isLoading: Boolean?) {
        linProgressIndicator.visibility = if (isLoading == true) View.VISIBLE else View.GONE
    }

    companion object {
        @JvmStatic
        fun newInstance(
        ): AttendanceEntryFragment =
            AttendanceEntryFragment()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnSave -> {
                if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                    attendanceEntryViewModel?.saveStudentAttendance()
                } else {
                    Toast.makeText(
                        context,
                        getString(R.string.no_internet_available),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            R.id.rlMarkAll -> {
                showMarkAllDialog()
            }
            R.id.rLClassAndSection -> {
                mainActivity?.let { activity ->
                    if (activity.isInForeground() && !activity.isFinishing && !activity.isDestroyed) {
                        val classAndSectionDialog =
                            ClassAndSectionDialog.newInstance(onClassRoomSelectedListener = this)
                        classAndSectionDialog.show(
                            activity.supportFragmentManager,
                            ClassAndSectionDialog::class.java.simpleName
                        )
                    }
                }

            }
            R.id.rLCategory -> {

                if (classRoomEntity == null) {
                    Toast.makeText(
                        context,
                        getString(R.string.please_select_class_and_section),
                        Toast.LENGTH_LONG
                    )
                        .show()
                } else {
                    attendanceEntryViewModel?.getAttendanceCategories(classRoomEntity, this)
                }
            }
            R.id.imgExpand -> {
                onClickListener()
            }
        }
    }

    override fun onClassRoomSelectedListener(
        courseEntity: CourseEntity,
        classroomEntity: ClassRoomEntity
    ) {
        this.classRoomEntity = classroomEntity
        this.shiftEntity = null
        fragmentAttendanceEntryBinding?.txtPeriod?.text = context?.getString(R.string.select_period)
        attendanceEntryViewModel?.setStudentAttendanceListInAdapter(null)
        fragmentAttendanceEntryBinding?.txtClassAndSection?.text =
            "${courseEntity.courseName} - ${classroomEntity.classroomName}"
    }

    override fun updateAttendanceCategory(shiftEntityList: List<ShiftEntity>?) {
        shiftEntityList?.let {
            mainActivity?.let { activity ->
                if (activity.isInForeground() && !activity.isFinishing && !activity.isDestroyed) {
                    val categorySectionDialog =
                        CategorySectionDialog.newInstance(
                            shiftEntityList,
                            this
                        )
                    categorySectionDialog.show(
                        activity.supportFragmentManager,
                        CategorySectionDialog::class.java.simpleName
                    )
                }
            }
        }
    }

    override fun onAttendanceCategorySelected(shiftEntity: ShiftEntity) {
        this.shiftEntity = shiftEntity
        fragmentAttendanceEntryBinding?.txtPeriod?.text = shiftEntity.name
        fetchAttendanceStudents()
    }

    private fun showMarkAllDialog() {
        val markAllDialog = context?.let { Dialog(it) }
        markAllDialog?.setContentView(R.layout.attendance_mark_all_dialog)
        markAllDialog?.setCanceledOnTouchOutside(true)
        markAllDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        markAllDialog?.show()
        markAllDialog?.findViewById<AppCompatTextView>(R.id.txtPresent)?.setOnClickListener {
            markAllDialog.dismiss()
            attendanceEntryViewModel?.markAll(AttendanceEntryViewModel.PRESENT)
            fragmentAttendanceEntryBinding?.txtMarkAll?.text = context?.getString(R.string.present)
        }
        markAllDialog?.findViewById<AppCompatTextView>(R.id.txtHoliday)?.setOnClickListener {
            markAllDialog.dismiss()
            attendanceEntryViewModel?.markAll(AttendanceEntryViewModel.HOLIDAY)
            fragmentAttendanceEntryBinding?.txtMarkAll?.text = context?.getString(R.string.holiday)
        }
        markAllDialog?.findViewById<AppCompatTextView>(R.id.txtWeekOff)?.setOnClickListener {
            markAllDialog.dismiss()
            attendanceEntryViewModel?.markAll(AttendanceEntryViewModel.WEEK_OFF)
            fragmentAttendanceEntryBinding?.txtMarkAll?.text = context?.getString(R.string.week_off)
        }

        markAllDialog?.findViewById<AppCompatImageView>(R.id.imgClose)?.setOnClickListener {
            markAllDialog.dismiss()
        }
    }

    override fun onDaySelect() {
        currentDate = fragmentAttendanceEntryBinding?.collapsibleCalendarView?.selectedDay?.let {
            AppUtil.getServerDateFormat(it)
        }
        fetchAttendanceStudents()
    }

    override fun onItemClick(v: View) {
    }

    override fun onDataUpdate() {
    }

    override fun onMonthChange() {
    }

    override fun onWeekChange(position: Int) {
    }

    override fun onClickListener() {
        if (fragmentAttendanceEntryBinding?.collapsibleCalendarView?.expanded == true) {
            fragmentAttendanceEntryBinding?.imgExpand?.setImageResource(R.drawable.arrow_up)
            fragmentAttendanceEntryBinding?.collapsibleCalendarView?.collapse(400)
        } else {
            fragmentAttendanceEntryBinding?.imgExpand?.setImageResource(R.drawable.down_arrow_icon)
            fragmentAttendanceEntryBinding?.collapsibleCalendarView?.expand(400)
        }
    }

    override fun onDayChanged() {
    }

}