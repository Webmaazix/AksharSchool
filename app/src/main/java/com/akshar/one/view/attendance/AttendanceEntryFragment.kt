package com.akshar.one.view.attendance

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
import com.akshar.one.database.entity.AttendanceCategoryEntity
import com.akshar.one.database.entity.ClassRoomEntity
import com.akshar.one.database.entity.CourseEntity
import com.akshar.one.databinding.FragmentAttendanceEntryBinding
import com.akshar.one.extension.isInForeground
import com.akshar.one.util.AndroidUtil
import com.akshar.one.util.AppUtil
import com.akshar.one.view.activity.MainActivity
import com.akshar.one.view.fragment.BaseFragment
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.attendance.AttendanceEntryViewModel
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import kotlinx.android.synthetic.main.fragment_attendance_entry.*
import java.util.*

class AttendanceEntryFragment : BaseFragment(), View.OnClickListener, OnClassRoomSelectedListener,
    AttendanceCategoryListener {

    private var classRoomEntity: ClassRoomEntity? = null
    private var attendanceCategoryEntity: AttendanceCategoryEntity? = null

    private var fragmentAttendanceEntryBinding: FragmentAttendanceEntryBinding? = null
    private var attendanceEntryViewModel: AttendanceEntryViewModel? = null
    private var mainActivity: MainActivity? = null

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

        mainActivity?.setToolbarTitle("Attendance")

        observers()
    }

    private fun fetchAttendanceStudents() {
        if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
            classRoomEntity?.classroomId?.let { classroomId ->
                attendanceCategoryEntity?.category?.let { category ->
                    attendanceEntryViewModel?.getStudentAttendanceByClassRoomId(
                        classroomId,
                        category,
                        AppUtil.getServerDateFormat(Calendar.getInstance().time)
                    )
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

        initHorizontalCalendar()
        fragmentAttendanceEntryBinding?.btnSave?.setOnClickListener(this)
        fragmentAttendanceEntryBinding?.rlMarkAll?.setOnClickListener(this)
        fragmentAttendanceEntryBinding?.rLClassAndSection?.setOnClickListener(this)
        fragmentAttendanceEntryBinding?.rLCategory?.setOnClickListener(this)

    }

    private fun initHorizontalCalendar() {
        /* starts before 1 month from now */
        val startDate: Calendar = Calendar.getInstance()
        startDate.add(Calendar.MONTH, -1)

        /* ends after 1 month from now */
        val endDate: Calendar = Calendar.getInstance()
        endDate.add(Calendar.MONTH, 1)

        val horizontalCalendar: HorizontalCalendar =
            HorizontalCalendar.Builder(fragmentAttendanceEntryBinding?.root, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(7)   // Number of Dates cells shown on screen (default to 5).
                .configure()    // starts configuration.
                .formatTopText(AppUtil.CALENDAR_DAY_FORMAT)
                .formatMiddleText(AppUtil.CALENDAR_DATE_FORMAT)
                .showTopText(true)              // show or hide TopText (default to true).
                .showBottomText(false)           // show or hide BottomText (default to true).
                .textColor(
                    R.color.black,
                    R.color.black
                )    // default to (Color.LTGRAY, Color.WHITE).
                // set selected date cell background.
                .end()          // ends configuration.
                .build()

        horizontalCalendar.calendarListener = object : HorizontalCalendarListener() {
            override fun onDateSelected(date: Calendar?, position: Int) {

            }
        }
    }

    private fun showProgressIndicator(isLoading: Boolean?) {
        linProgressIndicator.visibility = if (isLoading == true) View.VISIBLE else View.GONE
    }

    companion object {
        @JvmStatic
        fun newInstance(
        ): AttendanceEntryFragment = AttendanceEntryFragment()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnSave -> {
                if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                    classRoomEntity?.classroomId?.let {
                        attendanceEntryViewModel?.saveStudentAttendance(
                            it
                        )
                    }
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
                    Toast.makeText(context, "Please select Class and Section", Toast.LENGTH_LONG)
                        .show()
                } else {
                    attendanceEntryViewModel?.getAttendanceCategories(classRoomEntity, this)
                }
            }
        }
    }

    override fun onClassRoomSelectedListener(
        courseEntity: CourseEntity,
        classroomEntity: ClassRoomEntity
    ) {
        this.classRoomEntity = classroomEntity
        this.attendanceCategoryEntity = null
        fragmentAttendanceEntryBinding?.txtPeriod?.text = context?.getString(R.string.select_period)
        attendanceEntryViewModel?.setStudentAttendanceListInAdapter(null)
        fragmentAttendanceEntryBinding?.txtClassAndSection?.text =
            "${courseEntity.courseName} - ${classroomEntity.classroomName}"
    }

    override fun updateAttendanceCategory(attendanceCategoryEntityList: List<AttendanceCategoryEntity>?) {
        attendanceCategoryEntityList?.let {
            mainActivity?.let { activity ->
                if (activity.isInForeground() && !activity.isFinishing && !activity.isDestroyed) {
                    val categorySectionDialog =
                        CategorySectionDialog.newInstance(attendanceCategoryEntityList, this)
                    categorySectionDialog.show(
                        activity.supportFragmentManager,
                        CategorySectionDialog::class.java.simpleName
                    )
                }
            }
        }
    }

    override fun onAttendanceCategorySelected(attendanceCategoryEntity: AttendanceCategoryEntity) {
        this.attendanceCategoryEntity = attendanceCategoryEntity
        fragmentAttendanceEntryBinding?.txtPeriod?.text = attendanceCategoryEntity.category
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

}