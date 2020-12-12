package com.akshar.one.view.attendance2


import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.LinearLayoutManager

import com.akshar.one.R
import com.akshar.one.adapter.ClassDropDownAdapter
import com.akshar.one.calender.data.Day
import com.akshar.one.calender.widget.CollapsibleCalendar
import com.akshar.one.databinding.DialogSelectClassAndSectionBinding
import com.akshar.one.databinding.FragmentAttendanceEntry2Binding
import com.akshar.one.manager.SessionManager
import com.akshar.one.model.*
import com.akshar.one.util.AndroidUtil
import com.akshar.one.util.AppUtil
import com.akshar.one.util.AppUtils
import com.akshar.one.view.activity.MainActivity
import com.akshar.one.view.attendance2.adapter.StudentAttendanceAdapter
import com.akshar.one.view.messagecenter.adapter.ShiftListAdapter
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.attendance.AttendanceEntryViewModel
import com.akshar.one.viewmodels.attendance2.AttendanceViewModel
import com.akshar.one.viewmodels.timetable.TimeTableViewModel
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class AttendanceEntryFragment : Fragment() ,View.OnClickListener{

    private var currActivity : Activity? = null
    private var binding : FragmentAttendanceEntry2Binding? = null
    private var studentList = ArrayList<StudentAttendanceModel>()
    private var attendanceEntryViewModel: AttendanceViewModel? = null
    private var timeTableViewModel : TimeTableViewModel? = null
    private var classRoomId : Int = 0;
    private var loginModel : LoginModel? = null
    private var date : String? = null
    lateinit var adapter : StudentAttendanceAdapter
    lateinit var classDropDownAdapter : ClassDropDownAdapter
    private var classDropdownList = ArrayList<ClassDropDownModel>()
    private var dialogSelectClassSectionBinding : DialogSelectClassAndSectionBinding? = null
    private var dialog : Dialog? = null
    private var shiftAdapter: ShiftListAdapter? = null
    private var shiftList = ArrayList<ShiftList>()
    private var selectedShiftList  : ShiftList? = null
    private var selectedShiftId = ArrayList<Int>()
    var from = ""
    private var isUpdate = false

    companion object {
        @JvmStatic
        fun newInstance(): AttendanceEntryFragment{
            return  AttendanceEntryFragment()
        }

    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        currActivity = activity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_attendance_entry2,container,false)
        loginModel = SessionManager.getLoginModel()
        // employeeId = loginModel.let {it?.appsList?.get(0)?.userUniqueId!!  }
        date = AppUtil.getCurrentDate()
        initViews()
        return  binding?.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.application?.let {
            attendanceEntryViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(AttendanceViewModel::class.java)
        }
        activity?.application?.let {
            timeTableViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(TimeTableViewModel::class.java)
        }

        timeTableViewModel?.let {
            if(context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true){
                it.getClassRoomDropdown()
            }
        }

        attendanceEntryViewModel?.let {
            if(context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true){
                if((currActivity as MainActivity).from.equals("Employee")){
                    showProgressBar()
                    it.getShiftList("Employee",null)
                }

            }
        }


        observers()
    }


    private fun initViews(){
        if( (currActivity as MainActivity).from .equals("Student")){
            binding!!.rlClassSection.visibility = View.VISIBLE
        }else  if( (currActivity as MainActivity).from .equals("Employee")){
            binding!!.rlClassSection.visibility = View.GONE
        }
        setListner()
        setAdapter()

        binding!!.collapsibleCalendarView.setCalendarListener(object : CollapsibleCalendar.CalendarListener {
            override fun onDayChanged() {

            }

            override fun onClickListener() {
                if(binding!!.collapsibleCalendarView.expanded){
                    binding!!.imgExpand.setImageResource(R.drawable.arrow_up)
                    binding!!.collapsibleCalendarView.collapse(400)
                } else{
                    binding!!.imgExpand.setImageResource(R.drawable.down_arrow_icon)
                    binding!!.collapsibleCalendarView.expand(400)
                }
            }

            override fun onDaySelect() {
                val data = binding!!.collapsibleCalendarView.selectedDay
                sendDateToRefreshData(data!!)
            }

            override fun onItemClick(v: View) {

            }

            override fun onDataUpdate() {

            }

            override fun onMonthChange() {

            }

            override fun onWeekChange(position: Int) {

            }

        })

    }

    private fun setListner(){
        binding!!.rlClassSection.setOnClickListener(this)
        binding!!.imgExpand.setOnClickListener(this)
        binding!!.tvSelectShift.setOnClickListener(this)
        binding!!.tvMarkAllAs.setOnClickListener(this)
        binding!!.tvRecordNow.setOnClickListener(this)
        binding!!.tvSaveAttendance.setOnClickListener(this)

        binding!!.scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->

            if (scrollY > scrollX) {
                scrollDataTop()
                //  Toast.makeText(context, "Scrolling up", Toast.LENGTH_SHORT).show()
            } else {
                scrollDataBottom()
                // Toast.makeText(context, "Scrolling down", Toast.LENGTH_SHORT).show()
            }
        })

    }

    private var clickedFirst = true
    override fun onClick(p0: View?) {
        when(p0!!.id){

            R.id.imgExpand ->{
                if(binding!!.collapsibleCalendarView.expanded){
                    binding!!.imgExpand.setImageResource(R.drawable.down_arrow_icon)
                    binding!!.collapsibleCalendarView.collapse(400)
                } else{
                    binding!!.imgExpand.setImageResource(R.drawable.arrow_up)
                    binding!!.collapsibleCalendarView.expand(400)
                }
            }

            R.id.rlClassSection ->{
                openDialog()

            }

            R.id.tvSelectShift -> {
                openShiftDialog()
            }
            R.id.tvMarkAllAs ->{
                showMarkAllDialog()
            }

            R.id.tvSaveAttendance ->{
                if(validationForSaveAttendance()){
                    if(isUpdate){
                        if(clickedFirst){
                            clickedFirst = false
                            binding!!.tvSaveAttendance.text = currActivity!!.resources.getString(R.string.save_attendance)
                        }else{
                            showProgressBar()
                            attendanceEntryViewModel?.let {
                                if(context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true){
                                    showProgressBar()
                                    it.saveStudentsAttendance("Student",studentList)
                                }
                            }
                        }

                    }else{
                       // showProgressBar()
                        attendanceEntryViewModel?.let {
                            if(context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true){
                                showProgressBar()
                                it.saveStudentsAttendance("Student",studentList)
                            }
                        }
                    }


                }
            }
            R.id.tvRecordNow ->{
                isUpdate = false
                binding!!.tvSaveAttendance.text = currActivity!!.resources.getString(R.string.save_attendance)
                binding!!.rlNotRecorded.visibility = View.GONE
                binding!!.tvSaveAttendance.visibility = View.VISIBLE
                binding!!.rlClassTimeTable.visibility = View.VISIBLE
                adapter.notifyDataSetChanged()
            }

        }
    }

    fun validationForSaveAttendance() : Boolean{
        var isValid = true


        if(studentList.size == 0) {
            isValid = false
            if( (currActivity as MainActivity).from .equals("Employee")){
                AppUtils.showToast(currActivity, "Employee list must be there", true)
            }else{
                AppUtils.showToast(currActivity, "Student list must be there", true)
            }

        }
//        }else {
//            var entryFalse = false
//            for(model in studentList){
//                if(model.attendanceInd.equals("")){
//                    entryFalse = true
//                }
//            }
//            if(entryFalse){
//                isValid = false
//                AppUtils.showToast(currActivity,"Please enter attendance for every student",true)
//            }
//        }

        return isValid
    }

    private fun showMarkAllDialog() {
        val markAllDialog = context?.let { Dialog(it) }
        markAllDialog?.setContentView(R.layout.attendance_mark_all_dialog)
        markAllDialog?.setCanceledOnTouchOutside(true)
        Objects.requireNonNull<Window>(markAllDialog!!.getWindow())
            .setBackgroundDrawableResource(android.R.color.transparent)
        markAllDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        markAllDialog.show()
        markAllDialog.findViewById<AppCompatTextView>(R.id.txtPresent)?.setOnClickListener {
            markAllDialog.dismiss()
            binding!!.tvMarkAllAs?.text = context?.getString(R.string.present)

            if(studentList.size > 0){
                for (model in studentList){
                    model.attendanceInd = "P"
                }
                adapter.notifyDataSetChanged()

            }
        }

        markAllDialog?.findViewById<AppCompatTextView>(R.id.txtOnLeave)?.setOnClickListener {
            markAllDialog.dismiss()
            binding!!.tvMarkAllAs?.text = context?.getString(R.string.on_leave)

            if(studentList.size > 0){
                for (model in studentList){
                    model.attendanceInd = "L"
                }
                adapter.notifyDataSetChanged()
            }
        }

        markAllDialog?.findViewById<AppCompatTextView>(R.id.txtAbsent)?.setOnClickListener {
            markAllDialog.dismiss()
            binding!!.tvMarkAllAs?.text = context?.getString(R.string.absent)

            if(studentList.size > 0){
                for (model in studentList){
                    model.attendanceInd = "A"
                }
                adapter.notifyDataSetChanged()
            }
        }
        markAllDialog?.findViewById<AppCompatTextView>(R.id.txtHoliday)?.setOnClickListener {
            markAllDialog.dismiss()
            binding!!.tvMarkAllAs?.text = context?.getString(R.string.holiday)

            if(studentList.size > 0){
                for (model in studentList){
                    model.attendanceInd = "H"
                }
                adapter.notifyDataSetChanged()
            }

        }
        markAllDialog?.findViewById<AppCompatTextView>(R.id.txtWeekOff)?.setOnClickListener {
            markAllDialog.dismiss()
            binding!!.tvMarkAllAs?.text = context?.getString(R.string.week_off)

            if(studentList.size > 0){
                for (model in studentList){
                    model.attendanceInd = "W"
                }

                adapter.notifyDataSetChanged()
            }

        }

        markAllDialog?.findViewById<AppCompatImageView>(R.id.imgClose)?.setOnClickListener {
            markAllDialog.dismiss()
        }
    }



    fun scrollDataBottom(){

    }

    fun scrollDataTop(){

        if(binding!!.collapsibleCalendarView.expanded){
            binding!!.imgExpand.setImageResource(R.drawable.arrow_up)
            binding!!.collapsibleCalendarView.collapse(400)

        }
    }

    fun showProgressBar(){
        dialog =  AppUtils.showProgress(currActivity!!)
    }

    fun hideProgressBar(){
        AppUtils.hideProgress(dialog!!)
    }

    private fun setAdapter(){
        binding!!.rlClassTimeTable.setHasFixedSize(true)
        binding!!.rlClassTimeTable.layoutManager = LinearLayoutManager(currActivity,
            LinearLayoutManager.VERTICAL,false)
        adapter = StudentAttendanceAdapter(currActivity!!, studentList, (currActivity as MainActivity).from )
        binding!!.rlClassTimeTable.adapter = adapter
    }

        private fun observers(){

            attendanceEntryViewModel?.getErrorMutableLiveData()?.observe(this, androidx.lifecycle.Observer {
                it?.let {
                    hideProgressBar()
    //                    //(currActivity as TimeTableActivity).hideProgressBar()
    //                    binding!!.rlNoDataFound.visibility = View.VISIBLE
    //                    binding!!.rlClassTimeTable.visibility = View.GONE
    //                    studentList.clear()
    //                    adapter.notifyDataSetChanged()
                        AndroidUtil.showToast(context!!, it.message,true)

                }
            })

            attendanceEntryViewModel?.getSuccessLiveData()?.observe(this,androidx.lifecycle.Observer {
                it?.let {
                    hideProgressBar()
                    AndroidUtil.showToast(context!!, "Attendance Saved Successfully",false)
                }
            })

            attendanceEntryViewModel?.getShiftListLiveData()?.observe(this,androidx.lifecycle.Observer {
                hideProgressBar()
                shiftList.clear()
                shiftList.addAll(it)
                if(shiftAdapter!= null){
                    shiftAdapter!!.notifyDataSetChanged()
                }
            })

            attendanceEntryViewModel?.getStudentListLiveData()?.observe(this, androidx.lifecycle.Observer {
                hideProgressBar()
                studentList.clear()
                studentList.addAll(it)
                if(studentList.size> 0){
                    binding!!.rlNoDataFound.visibility = View.GONE
                    binding!!.rlClassTimeTable.visibility = View.VISIBLE
                }else{
                    binding!!.rlNoDataFound.visibility = View.GONE
                    binding!!.rlClassTimeTable.visibility = View.VISIBLE
                }
                if(!studentList.isNullOrEmpty()){
                    if(studentList[0].attendanceInd.isNullOrEmpty()){
                        binding!!.rlNotRecorded.visibility = View.VISIBLE
                        binding!!.tvSaveAttendance.visibility = View.GONE
                        binding!!.rlClassTimeTable.visibility = View.GONE

                    }else{
                        binding!!.rlNotRecorded.visibility = View.GONE
                        binding!!.tvSaveAttendance.visibility = View.VISIBLE
                        binding!!.rlClassTimeTable.visibility = View.VISIBLE
                        isUpdate = true
                        binding!!.tvSaveAttendance.text = currActivity!!.resources.getString(R.string.edit_attendance)
                        adapter.notifyDataSetChanged()
                    }

                }

                // dashboardViewModel?.setTimeTableAdapter(it)
            })


            timeTableViewModel?.getClassRoomLiveData()?.observe(this, androidx.lifecycle.Observer {
                classDropdownList.clear()
                classDropdownList.addAll(it)
            })

        }

    fun sendDateToRefreshData(data : Day){
        val month = data.month+1
        date = data.year.toString()+"-"+month.toString()+"-"+data.day
        studentList.clear()
        adapter.notifyDataSetChanged()
        if(validation()){
            attendanceEntryViewModel?.let {
                if(context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true){
                    showProgressBar()
                    if((currActivity as MainActivity).from .equals("Employee")){
                        it.getStudentAttendanceByShidtAndClassId(null,selectedShiftList!!.shiftId,date!!)
                    }else{
                        it.getStudentAttendanceByShidtAndClassId(classRoomId,selectedShiftList!!.shiftId,date!!)
                    }

                }
            }

        }
    }

    private fun validation() : Boolean {
        var isValid = true
        if((currActivity as MainActivity).from .equals("Employee")){
            if(selectedShiftList == null){
                isValid = false
                AndroidUtil.showToast(currActivity,"Please select Shift",true)
            }
        }else{
            if(classRoomId == 0){
                isValid = false
                AndroidUtil.showToast(currActivity,"Please select class & Section",true)
            }else if(selectedShiftList == null){
                isValid = false
                AndroidUtil.showToast(currActivity,"Please select Shift",true)
            }

        }
        return isValid
    }



    private fun openShiftDialog() {
        dialog = Dialog(currActivity as Context)
        dialogSelectClassSectionBinding = DataBindingUtil.inflate(
            LayoutInflater.from(currActivity),
            R.layout.dialog_select_class_and_section, null, false
        )
        dialog!!.setContentView(dialogSelectClassSectionBinding!!.getRoot())
        Objects.requireNonNull<Window>(dialog!!.getWindow())
            .setBackgroundDrawableResource(android.R.color.transparent)
        dialogSelectClassSectionBinding!!.tvTitle.text = resources.getString(R.string.select_shift)
        if(shiftList.size > 0){
            dialogSelectClassSectionBinding!!.rlClassesDropdown.visibility = View.VISIBLE
            dialogSelectClassSectionBinding!!.rlNotFound.visibility = View.GONE
            dialogSelectClassSectionBinding!!.rlClassesDropdown.setHasFixedSize(true)
            dialogSelectClassSectionBinding!!.rlClassesDropdown.layoutManager = LinearLayoutManager(
                currActivity,
                LinearLayoutManager.VERTICAL, false
            )
            shiftAdapter = ShiftListAdapter(currActivity!!, shiftList,this)
            dialogSelectClassSectionBinding!!.rlClassesDropdown.adapter = shiftAdapter
            shiftAdapter!!.notifyDataSetChanged()
        }else{
            dialogSelectClassSectionBinding!!.rlClassesDropdown.visibility = View.GONE
            dialogSelectClassSectionBinding!!.rlNotFound.visibility = View.VISIBLE
        }


        dialogSelectClassSectionBinding!!.imgCancel.setOnClickListener {
//            for(model in shiftList){
//                if(model.isSelected){
//                    selectedShiftList.add(model)
//                    selectedShiftId.add(model.shiftId)
//                }
//            }
//
//            if(!selectedShiftList.isNullOrEmpty()){
//                binding!!.tvSelectShift.text = selectedShiftList[0].shiftName+" ..."
//            }

            dialog!!.dismiss()
        }
        dialog!!.setCancelable(true)
        dialog!!.show()
    }

    fun selectedShift(model : ShiftList){
        selectedShiftList = model
        binding!!.tvSelectShift.text = selectedShiftList!!.shiftName
        dialog!!.dismiss()
        if(validation()){
            attendanceEntryViewModel?.let {
                if(context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true){
                    showProgressBar()
                    if((currActivity as MainActivity).from .equals("Employee")){
                        it.getStudentAttendanceByShidtAndClassId(null,selectedShiftList!!.shiftId,date!!)
                    }else{
                        it.getStudentAttendanceByShidtAndClassId(classRoomId,selectedShiftList!!.shiftId,date!!)
                    }
                }
            }

        }


    }

    private fun openDialog(){
        dialog = Dialog(currActivity as Context)
        dialogSelectClassSectionBinding = DataBindingUtil.inflate(LayoutInflater.from(currActivity),
            R.layout.dialog_select_class_and_section,null,false)
        dialog!!.setContentView(dialogSelectClassSectionBinding!!.getRoot())
        Objects.requireNonNull<Window>(dialog!!.getWindow())
            .setBackgroundDrawableResource(android.R.color.transparent)
        if(classDropdownList.size > 0){
            dialogSelectClassSectionBinding!!.rlNotFound.visibility = View.GONE
            dialogSelectClassSectionBinding!!.rlClassesDropdown.visibility = View.VISIBLE
            dialogSelectClassSectionBinding!!.rlClassesDropdown.setHasFixedSize(true)
            dialogSelectClassSectionBinding!!.rlClassesDropdown.layoutManager = LinearLayoutManager(currActivity,
                LinearLayoutManager.VERTICAL,false)
            ClassDropDownAdapter.selectedChild = -1
            ClassDropDownAdapter.clickParent =-1;
            classDropDownAdapter = ClassDropDownAdapter(currActivity!!,classDropdownList,this,object :
                ClassDropDownAdapter.SectionSelection {
                override fun selectionCallback(parent: Int, child: Int) {
                    classDropDownAdapter.notifyDataSetChanged()
                }

            })

            dialogSelectClassSectionBinding!!.rlClassesDropdown.adapter = classDropDownAdapter
            classDropDownAdapter.notifyDataSetChanged()
        }else{
            dialogSelectClassSectionBinding!!.rlNotFound.visibility = View.VISIBLE
            dialogSelectClassSectionBinding!!.rlClassesDropdown.visibility = View.GONE
        }

        dialogSelectClassSectionBinding!!.imgCancel.setOnClickListener {
            dialog!!.dismiss()
        }

        dialog!!.setCancelable(true)
        dialog!!.show()
    }

    fun sectionClicked(data : ClassDropDownModel, model : SectionList){
        classRoomId = model.classroomId
        val className = data.courseName+" "+model.classroomName
        binding!!.tvClassSectionName.text = className
        val classRoomIdList = ArrayList<Int>()
        classRoomIdList.add(classRoomId)
        selectedShiftList = null
        binding!!.tvSelectShift.text = resources.getText(R.string.select_shift)
        binding!!.tvMarkAllAs.text = resources.getText(R.string.mark_all_as)
        shiftList.clear()
        studentList.clear()
        adapter.notifyDataSetChanged()
        dialog!!.dismiss()
        attendanceEntryViewModel?.let {
            if(context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true){
                if((currActivity as MainActivity).from.equals("Student")){
                    showProgressBar()
                    it.getShiftList("Student",classRoomIdList)
                }

            }
        }
       // observers()


    }

}
