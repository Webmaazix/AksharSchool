package com.akshar.one.view.attendance.employee

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
import com.akshar.one.database.entity.ShiftEntity
import com.akshar.one.databinding.FragmentEmployeeAttendanceEntryBinding
import com.akshar.one.extension.isInForeground
import com.akshar.one.util.AndroidUtil
import com.akshar.one.util.AppUtil
import com.akshar.one.view.activity.MainActivity
import com.akshar.one.view.attendance.AttendanceCategoryListener
import com.akshar.one.view.attendance.CategorySectionDialog
import com.akshar.one.view.fragment.BaseFragment
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.attendance.AttendanceEntryViewModel
import com.akshar.one.viewmodels.attendance.EmployeeAttendanceEntryViewModel
import kotlinx.android.synthetic.main.fragment_attendance_entry.*

class EmployeeAttendanceEntryFragment : BaseFragment(), View.OnClickListener,
    AttendanceCategoryListener, CollapsibleCalendar.CalendarListener {

    private var shiftEntity: ShiftEntity? = null

    private var fragmentEmployeeAttendanceEntryBinding: FragmentEmployeeAttendanceEntryBinding? =
        null
    private var employeeAttendanceEntryViewModel: EmployeeAttendanceEntryViewModel? = null
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
        fragmentEmployeeAttendanceEntryBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_employee_attendance_entry,
            container,
            false
        )
        return fragmentEmployeeAttendanceEntryBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.application?.let {

            employeeAttendanceEntryViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(EmployeeAttendanceEntryViewModel::class.java)
        }
        fragmentEmployeeAttendanceEntryBinding?.employeeAttendanceEntryViewModel =
            employeeAttendanceEntryViewModel

//        mainActivity?.setToolbarBackground(false)
       mainActivity?.setToolbarTitle(getString(R.string.employee_attendance_entry))

        observers()
        fetchEmployeeShifts()
    }

    private fun fetchEmployeeShifts() {
        if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
            shiftEntity?.shiftId?.let { shiftId ->
                currentDate?.let { date ->
                    employeeAttendanceEntryViewModel?.fetchEmployeeShifts(
                        shiftId,
                        date
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

        employeeAttendanceEntryViewModel?.getIsLoading()?.observe(this, Observer {
            showProgressIndicator(it)
        })

        employeeAttendanceEntryViewModel?.getStudentAttendanceListMutableLiveData()
            ?.observe(this, Observer {

                employeeAttendanceEntryViewModel?.setEmployeeAttendanceListInAdapter(it)

            })
    }

    private fun initViews() {

        currentDate =
            fragmentEmployeeAttendanceEntryBinding?.collapsibleCalendarView?.selectedDay?.let {
                AppUtil.getServerDateFormat(it)
            }
        fragmentEmployeeAttendanceEntryBinding?.btnSave?.setOnClickListener(this)
        fragmentEmployeeAttendanceEntryBinding?.rlMarkAll?.setOnClickListener(this)
        fragmentEmployeeAttendanceEntryBinding?.rLCategory?.setOnClickListener(this)
        fragmentEmployeeAttendanceEntryBinding?.collapsibleCalendarView?.setCalendarListener(this)
        fragmentEmployeeAttendanceEntryBinding?.imgExpand?.setOnClickListener(this)
    }

    private fun showProgressIndicator(isLoading: Boolean?) {
        linProgressIndicator.visibility = if (isLoading == true) View.VISIBLE else View.GONE
    }

    companion object {
        @JvmStatic
        fun newInstance(): EmployeeAttendanceEntryFragment = EmployeeAttendanceEntryFragment()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnSave -> {
                if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                    employeeAttendanceEntryViewModel?.saveEmployeeAttendance()
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

            R.id.rLCategory -> {
                employeeAttendanceEntryViewModel?.getShifts(this)
            }
            R.id.imgExpand -> {
                onClickListener()
            }
        }
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
        fragmentEmployeeAttendanceEntryBinding?.txtPeriod?.text = shiftEntity.name
        fetchEmployeeShifts()
    }

    private fun showMarkAllDialog() {
        val markAllDialog = context?.let { Dialog(it) }
        markAllDialog?.setContentView(R.layout.attendance_mark_all_dialog)
        markAllDialog?.setCanceledOnTouchOutside(true)
        markAllDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        markAllDialog?.show()
        markAllDialog?.findViewById<AppCompatTextView>(R.id.txtPresent)?.setOnClickListener {
            markAllDialog.dismiss()
            employeeAttendanceEntryViewModel?.markAll(AttendanceEntryViewModel.PRESENT)
            fragmentEmployeeAttendanceEntryBinding?.txtMarkAll?.text =
                context?.getString(R.string.present)
        }
        markAllDialog?.findViewById<AppCompatTextView>(R.id.txtHoliday)?.setOnClickListener {
            markAllDialog.dismiss()
            employeeAttendanceEntryViewModel?.markAll(AttendanceEntryViewModel.HOLIDAY)
            fragmentEmployeeAttendanceEntryBinding?.txtMarkAll?.text =
                context?.getString(R.string.holiday)
        }
        markAllDialog?.findViewById<AppCompatTextView>(R.id.txtWeekOff)?.setOnClickListener {
            markAllDialog.dismiss()
            employeeAttendanceEntryViewModel?.markAll(AttendanceEntryViewModel.WEEK_OFF)
            fragmentEmployeeAttendanceEntryBinding?.txtMarkAll?.text =
                context?.getString(R.string.week_off)
        }

        markAllDialog?.findViewById<AppCompatImageView>(R.id.imgClose)?.setOnClickListener {
            markAllDialog.dismiss()
        }
    }

    override fun onDaySelect() {
        currentDate =
            fragmentEmployeeAttendanceEntryBinding?.collapsibleCalendarView?.selectedDay?.let {
                AppUtil.getServerDateFormat(it)
            }
        fetchEmployeeShifts()
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
        if (fragmentEmployeeAttendanceEntryBinding?.collapsibleCalendarView?.expanded == true) {
            fragmentEmployeeAttendanceEntryBinding?.imgExpand?.setImageResource(R.drawable.arrow_up)
            fragmentEmployeeAttendanceEntryBinding?.collapsibleCalendarView?.collapse(400)
        } else {
            fragmentEmployeeAttendanceEntryBinding?.imgExpand?.setImageResource(R.drawable.down_arrow_icon)
            fragmentEmployeeAttendanceEntryBinding?.collapsibleCalendarView?.expand(400)
        }
    }

    override fun onDayChanged() {
    }

}