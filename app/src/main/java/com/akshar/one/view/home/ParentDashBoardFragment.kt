package com.akshar.one.view.home


import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.se.omapi.Session
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.LinearLayoutManager

import com.akshar.one.R
import com.akshar.one.databinding.*
import com.akshar.one.extension.visible
import com.akshar.one.manager.SessionManager
import com.akshar.one.model.*
import com.akshar.one.util.AndroidUtil
import com.akshar.one.util.AppUtil
import com.akshar.one.util.AppUtils
import com.akshar.one.view.activity.MainActivity
import com.akshar.one.view.birthday.BirthdayActivity
import com.akshar.one.view.feeandpayments.StudentFeesDetails
import com.akshar.one.view.home.adapter.*
import com.akshar.one.view.login.adapter.StudentAdapter
import com.akshar.one.view.noticeboard.NoticeboardActivity
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.dashboard.DashboardViewModel
import com.akshar.one.viewmodels.feeandpayment.FeeAndPaymentViewModel
import com.akshar.one.viewmodels.noticeboard.NoticeBoardViewModel
import kotlinx.android.synthetic.main.fragment_dashboard.*
import java.text.DecimalFormat

/**
 * A simple [Fragment] subclass.
 */
class ParentDashBoardFragment : Fragment(),View.OnClickListener {

    private var containerView: View? = null
    private var dashboardViewModel: DashboardViewModel? = null
    private lateinit var mainActivity: MainActivity
    private var date : String? = null
    private var studentProfileId : Int = 0;
    private var loginModel : LoginModel? = null
    private var fromDate  = ""
    private var toDate = ""
    private var noticeBoardViewModel: NoticeBoardViewModel? = null
    private var binding : FragmentParentDashBoardBinding? = null
    private lateinit var currActivity: Activity
    private lateinit var adapter : TimeTableAdapter
    private lateinit var noticeBoardAdapter : NoticeBoardAdapter
    private var timeTableList = ArrayList<PeriodTimeTable>()
    private var currentYearJan : String? = null
    private var dialogCommonOptionsBinding: DialogCommonOptionsBinding? = null
    private var dialogFeaHeadBinding: DialogChooseFeaheadBinding? = null
    private var birthdayLayoutBinding : BirthdayPopupBinding? = null
    private lateinit var dialog: Dialog
    private var yesterdayDate : String?= null
    private var groupBy : String = "FEE_HEAD"
    private var expenseGroupBy = "FEE_HEAD"
    private var expenseFromDate = ""
    private var expenseToDate = ""
    private lateinit var collectionAdapter : CollectionAdapter
    private lateinit var expencesAdapter : ExpencesAdapter
    private var collectionList = ArrayList<FeePayment>()
    private var expenceList = ArrayList<FeePayment>()
    var collectionAmountFloatArray = ArrayList<Float>()
    var collectionColorArray = ArrayList<Int>()
    var colorsArray = Array<Int>(12){0}
    private var noticeBoardList = ArrayList<NoticeBoardModel>()
    private var feeDetailList = ArrayList<FeesDetailModel>()
    private var feeAndPaymentViewModel : FeeAndPaymentViewModel? = null
    private var loginRole : AppList? = null

    private lateinit var studentAdapter : StudentAdapter
    private var list = ArrayList<AppList>()
    private var schoolList = ArrayList<AppList>()
    private var studentList = ArrayList<AppList>()
    private var selectedRole : AppList? = null
    private var currentAttendance = false


    companion object {
        @JvmStatic
        fun newInstance() = ParentDashBoardFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_parent_dash_board, container, false)
        initView()
       

        return binding?.root
    }

    private fun setAdapter(){
        binding!!.rvClassRooms.setHasFixedSize(true)
        binding!!.rvClassRooms.layoutManager = LinearLayoutManager(currActivity,
            LinearLayoutManager.HORIZONTAL,false)
        adapter = TimeTableAdapter(currActivity, timeTableList)
        binding!!.rvClassRooms.adapter = adapter


        binding!!.rvNoticeList1.setHasFixedSize(true)
        binding!!.rvNoticeList1.layoutManager = LinearLayoutManager(currActivity,
            LinearLayoutManager.VERTICAL,false)
        noticeBoardAdapter =
            NoticeBoardAdapter(currActivity, noticeBoardList)
        binding!!.rvNoticeList1.adapter = noticeBoardAdapter

        binding!!.rvStudentList.setHasFixedSize(true)
        binding!!.rvStudentList.layoutManager = LinearLayoutManager(currActivity,LinearLayoutManager.HORIZONTAL,false)
        studentAdapter = StudentAdapter(currActivity,studentList,selectedRole)
        binding!!.rvStudentList.adapter = studentAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        list = SessionManager.getLoginModel()!!.appsList as ArrayList<AppList>
        selectedRole = SessionManager.getLoginRole()
        for(model in list){
            if(model.appName.equals("Spectrum")){
                schoolList.add(model)
            }else if(model.appName.equals("SmartParent")){
                studentList.add(model)
            }
        }

        activity?.application?.let {
            dashboardViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(DashboardViewModel::class.java)
        }

        activity?.application?.let {
            noticeBoardViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(NoticeBoardViewModel::class.java)
        }

        activity?.application?.let {
            feeAndPaymentViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(FeeAndPaymentViewModel::class.java)
        }

        feeAndPaymentViewModel?.let {
            if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                it.getFeeDetail(studentProfileId) }
        }

        dashboardViewModel?.let {
            if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                showProgressBar()
                it.getTimeTableOfClass(loginRole!!.classRoomId,date!!) }
        }

        dashboardViewModel?.let {
            if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                it.getAttendanceStatsOfStudent(null,null,studentProfileId.toString()) }
        }

        noticeBoardViewModel?.let {
            if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                it.getAllNotices("ACTIVE") }
        }

        dashboardViewModel?.let {
            if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {

                it.getPresentAttendance(studentProfileId.toString()) }
        }


        observers()
    }

    private fun observers(){

        feeAndPaymentViewModel?.getErrorMutableLiveData()?.observe(this, Observer {
            it?.let {
                AndroidUtil.showToast(context!!, it.message,true)
            }
        })

        feeAndPaymentViewModel?.getFeeDetailLiveData()?.observe(this, Observer {
            feeDetailList.clear()
            feeDetailList.addAll(it)
            setFinanceData(feeDetailList)
        })

        dashboardViewModel?.getErrorMutableLiveData()?.observe(this, Observer {
            hideProgressBar()
            it?.let {
                AndroidUtil.showToast(context!!, it.message,true)
            }
        })

        dashboardViewModel?.getClassTimeTableLiveData()?.observe(this, Observer {
            hideProgressBar()
            timeTableList.clear()
            timeTableList.addAll(it)
            if(timeTableList.size > 0 || timeTableList == null){
                binding!!.rlTimeTableNotFound.visibility = View.GONE
                binding!!.rvClassRooms.visibility = View.VISIBLE
            }else{
                binding!!.rlTimeTableNotFound.visibility = View.VISIBLE
                binding!!.rvClassRooms.visibility = View.GONE
            }
            adapter.notifyDataSetChanged()
            // dashboardViewModel?.setTimeTableAdapter(it)
        })

        dashboardViewModel?.getAttendanceStatsLiveData()?.observe(this, Observer {
            setAttendaceStatsData(it)
        })
        dashboardViewModel?.getPresentAttendanceStatsLiveData()?.observe(this, Observer {
            setPresentAttendance(it)
        })

        noticeBoardViewModel?.getErrorMutableLiveData()?.observe(this, Observer {
            it?.let {
                AndroidUtil.showToast(context!!, it.message,true)
            }
        })

        noticeBoardViewModel?.getNoticeListLiveData()?.observe(this, Observer {
            noticeBoardList.clear()
            noticeBoardList.addAll(it)
            if(noticeBoardList.size> 0){
                if(noticeBoardList.size < 10){
                    val count = "0"+noticeBoardList.size.toString()
                    binding!!.tvBirthDayCount.text =   count
                }else{
                    binding!!.tvBirthDayCount.text = noticeBoardList.size.toString()
                }
                binding!!.rlBirthdayNotFound.visibility = View.GONE
                binding!!.rlSeeAll.visibility = View.VISIBLE
                binding!!.rvNoticeList1.visibility = View.VISIBLE
            }else{
                binding!!.tvBirthDayCount.text = "00"
                binding!!.rlBirthdayNotFound.visibility = View.VISIBLE
                binding!!.rvNoticeList1.visibility = View.GONE
                binding!!.rlSeeAll.visibility = View.GONE
            }
            noticeBoardAdapter.notifyDataSetChanged()
        })

    }

    private fun setFinanceData(list : ArrayList<FeesDetailModel>){
        if(feeDetailList.size> 0) {
            var finalAmount = 0.0
            var dueAmount = 0.0
            var overDueAmount = 0.0
            for (data in feeDetailList[0].feeHeadList) {
                for (price in data.feeTermList) {
                    if (finalAmount == 0.0) {
                        finalAmount = price.finalAmount
                    } else {
                        finalAmount += price.finalAmount
                    }

                    if (dueAmount == 0.0) {
                        dueAmount = price.dueAmount
                    } else {
                        dueAmount += price.dueAmount
                    }

                }
            }

            binding!!.tvPaidAmount.text = "₹ $finalAmount"
            binding!!.tvDueAmount.text = "₹ $dueAmount"
            binding!!.tvOverDueAmount.text = "₹ $overDueAmount"
            binding!!.rlSeeAllFinance.visibility = View.VISIBLE

            binding?.pieChartFinance!!.setDataPoints(floatArrayOf(
                finalAmount.toFloat(),
                dueAmount.toFloat()))
            binding?.pieChartFinance!!.setCenterColor(R.color.white)
            binding?.pieChartFinance!!.setSliceColor(intArrayOf(
                R.color.green_normal,
                R.color.maroon))

        }else{
            binding!!.rlSeeAllFinance.visibility = View.GONE
        }

    }

    private fun initView() {
       // loginModel = SessionManager.getLoginModel()
        loginRole = SessionManager.getLoginRole()
        currActivity = activity!!
        studentProfileId = loginRole!!.userUniqueId

        //val datee = AppUtil.getCurrentDateTime()
        date = AppUtil.getCurrentDate()
        currentYearJan = AppUtil.getCurrentYear().toString()+"-01-01"
        expenseFromDate = currentYearJan!!
        expenseToDate = date!!
        fromDate = currentYearJan!!
        toDate = date!!
        setAdapter()
        colorsArray = arrayOf(R.color.light_blue,R.color.green_normal,
            R.color.light_yellow,R.color.dark_yellow,
            R.color.orange,R.color.science_blue,R.color.maroon,
            R.color.hindi_orange,R.color.english_pink,R.color.bio_purple,
            R.color.physics_pink,
            R.color.socialstudy_purple,R.color.civics_green,
            R.color.chemistry_blue,R.color.economics_purple)
        setStudentData()
        setListeners()
    }
    
    private fun setStudentData(){
        binding!!.tvStudentName.text = loginRole!!.studentName
        binding!!.tvClassSectionName.text = loginRole!!.className

        if(loginRole!!.url!= null && loginRole!!.url!=""){
            binding!!.flLayout.visibility = View.GONE
            binding!!.imgUserProfile.visibility = View.VISIBLE
            AppUtils.loadImageCrop(
                loginRole!!.url,
                binding!!.imgUserProfile,
                R.drawable.circle_default_pic,
                80,
                80
            )
        }else {
            binding!!.flLayout.visibility = View.VISIBLE
            binding!!.imgUserProfile.visibility = View.GONE

                binding!!.tvShortName.setText(
                    loginRole!!.studentName.substring(0, 2).toUpperCase()
                )


        }

    }

    private fun setListeners() {
        binding!!.rlDay.setOnClickListener(this)
        binding!!.rlDay.setOnClickListener(this)
        binding!!.rlSeeAll.setOnClickListener(this)
        binding!!.rlSeeAllFinance.setOnClickListener(this)
      //  binding!!.tvPresentLabel.setOnClickListener(this)
        binding!!.imgArrow.setOnClickListener(this)
        binding!!.imgUpArrow.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.rlDay ->{
                openMenu()
            }
            R.id.imgArrow ->{
                binding!!.imgUpArrow.visibility = View.VISIBLE
                binding!!.imgArrow.visibility = View.GONE

               binding!!.rvStudentList.visibility = View.VISIBLE
            }
            R.id.imgUpArrow ->{
                binding!!.imgUpArrow.visibility = View.GONE
                binding!!.imgArrow.visibility = View.VISIBLE

               binding!!.rvStudentList.visibility = View.GONE
            }

            R.id.rlSeeAllFinance ->{
                StudentFeesDetails.open(currActivity,(currActivity as MainActivity).student)
            }
            R.id.rlSeeAll ->{
                NoticeboardActivity.open(currActivity,(currActivity as MainActivity).securityList)
            }


        }
    }

    private fun setAttendaceStatsData(it : ArrayList<AttendanceDashboard>){

        var presentPercentage  = 0
        var AbsentPercentage  = 0
        var latePercentage  = 0
        for(model in it){
            if(model.key.equals("P")){
                presentPercentage = model.value.toInt()
                binding?.tvPresentPercentage!!.text = model.value+" %"
            }else if(model.key.equals("A")){
                AbsentPercentage = model.value.toInt()
                binding?.tvAbsentPercentage!!.text = model.value+" %"
            }else if(model.key.equals("L")){
                latePercentage = model.value.toInt()
                binding?.tvOnLeavePercentage!!.text = model.value+" %"
            }
        }
        val formatter = DecimalFormat("#,###,###")
        if(it != null){


            binding?.pieChart!!.setDataPoints(floatArrayOf(
                presentPercentage.toFloat(),
                AbsentPercentage.toFloat(),
                latePercentage.toFloat()))
            binding?.pieChart!!.setCenterColor(R.color.white)
            binding?.pieChart!!.setSliceColor(intArrayOf(
                R.color.green_normal,
                R.color.light_yellow,
                R.color.maroon))
        }

    }
    private fun setPresentAttendance(it : StudentAttendanceModel){
        binding!!.tvPresentLabel.setText("Today - "+it.attendanceStatus)

    }

    private fun openMenu(){
        val popup = PopupMenu(currActivity,rlDay)
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.attendance_stats_options, popup.menu)

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener {
            if(it.title.equals(currActivity.resources.getString(R.string.this_week))){
                binding?.tvDay!!.text = currActivity.resources.getString(R.string.this_week)
                val startDateOfWeek = AppUtil.getSevenDaysBack()
                dashboardViewModel?.let {
                    if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                        it.getAttendanceStatsOfStudent(startDateOfWeek!!,date!!,studentProfileId.toString()) }
                }
                dashboardViewModel?.getAttendanceStatsLiveData()?.observe(this, Observer {
                   setAttendaceStatsData(it)
                    // dashboardViewModel?.setTimeTableAdapter(it)
                })

            }else if(it.title.equals(currActivity.resources.getString(R.string.month))){

                binding?.tvDay!!.text = currActivity.resources.getString(R.string.month)
                val startDateMonth = AppUtil.get30DaysBack()
                dashboardViewModel?.let {
                    if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                        it.getAttendanceStatsOfStudent(startDateMonth!!,date!!,studentProfileId.toString()) }
                }

                dashboardViewModel?.getAttendanceStatsLiveData()?.observe(this, Observer {
                    setAttendaceStatsData(it)
                })

            }else if(it.title.equals(currActivity.resources.getString(R.string.year))){

                binding?.tvDay!!.text = currActivity.resources.getString(R.string.year)
                val fromDate = AppUtil.getSevenDaysAfter()
                dashboardViewModel?.let {
                    if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                        it.getAttendanceStatsOfStudent(null,null,studentProfileId.toString()) }
                }

                dashboardViewModel?.getAttendanceStatsLiveData()?.observe(this, Observer {
                  setAttendaceStatsData(it)
                })


            }
            true
        }

        popup.show()//showing popup menu

    }

    private fun showDayDialog(){

    }

    private fun showProgressIndicator(isLoading: Boolean?) {
        // linProgressIndicator.visibility = if (isLoading == true) View.VISIBLE else View.GONE
    }

    fun showProgressBar(){
        dialog =  AppUtils.showProgress(currActivity)
    }

    fun hideProgressBar(){
        AppUtils.hideProgress(dialog)
    }
}
