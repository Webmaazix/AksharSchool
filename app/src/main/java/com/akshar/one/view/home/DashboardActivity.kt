package com.akshar.one.view.home

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.akshar.one.R
import com.akshar.one.adapter.BirthDayAdapter
import com.akshar.one.adapter.TimeTableAdapter
import com.akshar.one.databinding.BirthdayPopupBinding
import com.akshar.one.databinding.DialogChooseFeaheadBinding
import com.akshar.one.databinding.DialogCommonOptionsBinding
import com.akshar.one.databinding.FragmentDashboardBinding
import com.akshar.one.manager.SessionManager
import com.akshar.one.model.*
import com.akshar.one.util.AndroidUtil
import com.akshar.one.util.AppUtil
import com.akshar.one.view.fragment.BaseFragment
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.dashboard.DashboardViewModel
import java.util.*
import kotlin.collections.ArrayList
import android.widget.Toast
import android.R.attr.button
import android.view.*
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.LifecycleOwner
import com.akshar.one.birthday.BirthdayActivity
import com.akshar.one.view.activity.MainActivity
import kotlinx.android.synthetic.main.fragment_dashboard.*
import java.text.DecimalFormat


/**
 * A simple [Fragment] subclass.
 */
class DashboardActivity : BaseFragment(),View.OnClickListener {
    private var containerView: View? = null
    private var dashboardViewModel: DashboardViewModel? = null
    private lateinit var mainActivity: MainActivity
    private var date : String? = null
    private var employeeId : Int = 0;
    private var loginModel : LoginModel? = null
    private var fromDate  = ""
    private var toDate = ""
    private var binding : FragmentDashboardBinding? = null
    private lateinit var currActivity: Activity
    private lateinit var adapter : TimeTableAdapter
    private lateinit var birthDayadapter : BirthDayAdapter
    private var timeTableList = ArrayList<TimeTableModel>()
    private var birthDayList = ArrayList<BirthDayModel>()
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

    companion object {
        @JvmStatic
        fun newInstance() = DashboardActivity()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_dashboard, container, false)
        loginModel = SessionManager.getLoginModel()
        currActivity = activity!!
        employeeId = loginModel.let {it?.appsList?.get(0)?.userUniqueId!!  }
        //val datee = AppUtil.getCurrentDateTime()
        date = AppUtil.getCurrentDate()
        currentYearJan = AppUtil.getCurrentYear().toString()+"-01-01"
        expenseFromDate = currentYearJan!!
        expenseToDate = date!!
        fromDate = currentYearJan!!
        toDate = date!!
        setAdapter()
        return binding?.root
    }

    private fun setAdapter(){
        binding!!.rvClassRooms.setHasFixedSize(true)
        binding!!.rvClassRooms.layoutManager = LinearLayoutManager(currActivity,LinearLayoutManager.HORIZONTAL,false)
        adapter = TimeTableAdapter(currActivity,timeTableList)
        binding!!.rvClassRooms.adapter = adapter


        binding!!.rvBirthdays.setHasFixedSize(true)
        binding!!.rvBirthdays.layoutManager = LinearLayoutManager(currActivity,LinearLayoutManager.VERTICAL,false)
        birthDayadapter = BirthDayAdapter(currActivity,birthDayList)
        binding!!.rvBirthdays.adapter = birthDayadapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.application?.let {
            dashboardViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(DashboardViewModel::class.java)
        }

        dashboardViewModel?.let {
            if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                it.getTimeTableOfEmployee(employeeId,date!!) }
        }
        dashboardViewModel?.let {
            if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                it.getBirthdays(date!!,date!!) }
        }
        dashboardViewModel?.let {
            if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                it.getAllFinanceSummery(currentYearJan!!,date!!) }
        }

        observers()
    }

    private fun observers() {
        dashboardViewModel?.getErrorMutableLiveData()?.observe(this, Observer {
            it?.let {
                AndroidUtil.showToast(context!!, it.message,true)
            }
        })

        dashboardViewModel?.getTimeTableLiveData()?.observe(this, Observer {
            timeTableList.clear()
            timeTableList.addAll(it)
            adapter.notifyDataSetChanged()
           // dashboardViewModel?.setTimeTableAdapter(it)
        })
        dashboardViewModel?.getBirthdayLiveData()?.observe(this, Observer {
            birthDayList.clear()
            birthDayList.addAll(it)
            if(birthDayList.size > 0){
                binding!!.rlSeeAll.visibility = View.VISIBLE
                if(birthDayList.size < 10){
                    val count = "0"+birthDayList.size.toString()
                    binding!!.tvBirthDayCount.text =   count
                }else{
                    binding!!.tvBirthDayCount.text = birthDayList.size.toString()
                }

                binding!!.rlBirthdayNotFound.visibility = View.GONE
                binding!!.rvBirthdays.visibility = View.VISIBLE
            }else{
                binding!!.rlSeeAll.visibility = View.GONE
                binding!!.tvBirthDayCount.text = "00"
                binding!!.rlBirthdayNotFound.visibility = View.VISIBLE
                binding!!.rvBirthdays.visibility = View.GONE
            }
            birthDayadapter.notifyDataSetChanged()
           // dashboardViewModel?.setTimeTableAdapter(it)
        })

        dashboardViewModel?.getFinanceSummeryLiveData()?.observe(this, Observer {
            setFinanceData(it)
        })

        dashboardViewModel?.getIsLoading()?.observe(this, Observer {
            showProgressIndicator(it)
        })

    }

    private fun initView() {
        setListeners()
    }

    private fun setListeners() {
        binding!!.rlDay.setOnClickListener(this)
        binding!!.rlCategory.setOnClickListener(this)
        binding!!.rlWeek.setOnClickListener(this)
        binding!!.rlCategoryExpence.setOnClickListener(this)
        binding!!.rlWeekExpence.setOnClickListener(this)
        binding!!.rlDay.setOnClickListener(this)
        binding!!.rlSeeAll.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.rlWeek ->{
                openWeek(0)

            }
            R.id.rlCategory ->{
                openCategoryPopup(0)

            }
            R.id.rlWeekExpence ->{
                openWeek(1)

            }
            R.id.rlCategoryExpence ->{
                openCategoryPopup(1)

            }
            R.id.rlDay ->{
                openMenu()

            }
            R.id.rlSeeAll ->{
                BirthdayActivity.open(currActivity)
            }
        }
    }

    private fun setFinanceData(it : FinanceModel){
        val formatter = DecimalFormat("#,###,###")
        if(it.feeSummary != null){

            val paidString = formatter.format(it.feeSummary.amountPaid)
            val paid ="₹ "+paidString
            val dueString = formatter.format(it.feeSummary.dueAmount)
            val due = "₹ "+dueString
            val overDueString = formatter.format(it.feeSummary.overdueAmount)
            val overdue = "₹ "+overDueString
            val totalString = formatter.format(it.feeSummary.amountAfterConcession)
            val total = "₹ "+totalString
            val pieTotal = it.feeSummary.amountPaid!!+it.feeSummary.dueAmount!!+it.feeSummary.overdueAmount!!
            binding?.tvPaidAmount!!.text = paid
            binding?.tvDueAmount!!.text = due
            binding?.tvOverDueAmount!!.text = overdue
            binding?.tvTotalFinanceAmount!!.text = total

            binding?.pieChart!!.setDataPoints(floatArrayOf(
                it.feeSummary.amountPaid.toFloat(),
                it.feeSummary.dueAmount.toFloat(),
                it.feeSummary.overdueAmount.toFloat()))
            binding?.pieChart!!.setCenterColor(R.color.white)
            binding?.pieChart!!.setSliceColor(intArrayOf(
                R.color.maroon,
                R.color.green_normal,
                R.color.light_yellow))
        }
        if(it.expenseSummary!= null){
            val maintainString = formatter.format(it.expenseSummary.Maintenance)
            val maintenance = "₹ "+maintainString
            val transportString = formatter.format(it.expenseSummary.Transport)
            val transport = "₹ "+transportString
            val total = it.expenseSummary.Maintenance!!+it.expenseSummary.Transport!!
            val totalString = formatter.format(total)
            val tottalExpence = "₹ "+totalString
            binding?.tvPaidAmountExpence!!.text = maintenance
            binding?.tvDueAmountExpence!!.text = transport
            binding?.tvTotalFinanceAmountExpence!!.text = tottalExpence

            binding?.pieChartExpence!!.setDataPoints(floatArrayOf(
                it.expenseSummary.Maintenance.toFloat(),
                it.expenseSummary.Transport.toFloat()))
            binding?.pieChartExpence!!.setCenterColor(R.color.white)
            binding?.pieChartExpence!!.setSliceColor(intArrayOf(
                R.color.maroon,
                R.color.orange))

        }
        if(it.feePayment!= null){
            val admissionString = formatter.format(it.feePayment.Admission_Fee)
            val admissionFee = "₹ "+admissionString
            val tutionString = formatter.format(it.feePayment.TUTION_FEE)
            val tutionFee = "₹ "+tutionString
            val total = it.feePayment.Admission_Fee!!+it.feePayment.TUTION_FEE!!
            val totalString = formatter.format(total)
            val tottalExpence = "₹ "+totalString

            binding?.tvPaidAmountCollection!!.text = admissionFee
            binding?.tvDueAmountCollection!!.text = tutionFee
            binding?.tvTotalFinanceAmountCollection!!.text = tottalExpence

            binding?.pieChartCollection!!.setDataPoints(floatArrayOf(
                it.feePayment.Admission_Fee.toFloat(),
                it.feePayment.TUTION_FEE.toFloat()))
            binding?.pieChartCollection!!.setCenterColor(R.color.white)
            binding?.pieChartCollection!!.setSliceColor(intArrayOf(
                R.color.green_normal,
                R.color.bluee))
        }

    }

    private fun setCollectionData(feePayment: FeePayment){
        val formatter = DecimalFormat("#,###,###")
        if(feePayment!= null){
            var admissionFee = ""
            var tottalExpence = ""
            var tutionFee = ""
            if(feePayment.Admission_Fee!= null){
                val admissionString = formatter.format(feePayment.Admission_Fee)
                admissionFee = "₹ "+admissionString
                binding?.tvPaidAmountCollection!!.text = admissionFee
            }else{
                admissionFee = "₹ 0"
                binding?.tvPaidAmountCollection!!.text = admissionFee
            }

            if(feePayment.TUTION_FEE!= null){
                val tutionString = formatter.format(feePayment.TUTION_FEE)
                 tutionFee = "₹ "+tutionString
                binding?.tvDueAmountCollection!!.text = tutionFee
            }else{
                tutionFee = "₹ 0"
                binding?.tvDueAmountCollection!!.text = tutionFee
            }
            if(feePayment.Admission_Fee!= null && feePayment.TUTION_FEE!= null){
                val total = feePayment.Admission_Fee+feePayment.TUTION_FEE
                val totalString = formatter.format(total)
                 tottalExpence = "₹ $totalString"
                binding?.tvTotalFinanceAmountCollection!!.text = tottalExpence

                binding?.pieChartCollection!!.setDataPoints(floatArrayOf(
                    feePayment.Admission_Fee.toFloat(),
                    feePayment.TUTION_FEE.toFloat()))
                binding?.pieChartCollection!!.setCenterColor(R.color.white)
                binding?.pieChartCollection!!.setSliceColor(intArrayOf(
                    R.color.green_normal,
                    R.color.bluee))

            }else if(feePayment.Admission_Fee == null){
                val tutionString = formatter.format(feePayment.TUTION_FEE)
                val total = feePayment.TUTION_FEE
                tottalExpence = "₹ $tutionString"
                binding?.tvTotalFinanceAmountCollection!!.text = tottalExpence

                binding?.pieChartCollection!!.setDataPoints(floatArrayOf(
                    feePayment.TUTION_FEE!!.toFloat()))
                binding?.pieChartCollection!!.setCenterColor(R.color.white)
                binding?.pieChartCollection!!.setSliceColor(intArrayOf(
                    R.color.bluee))

            }else if(feePayment.TUTION_FEE == null){
                val admissionString = formatter.format(feePayment.Admission_Fee)
                val total = feePayment.Admission_Fee
                tottalExpence = "₹ $admissionString"
                binding?.tvTotalFinanceAmountCollection!!.text = tottalExpence
                binding?.pieChartCollection!!.setDataPoints(floatArrayOf(
                    feePayment.Admission_Fee.toFloat()))
                binding?.pieChartCollection!!.setCenterColor(R.color.white)
                binding?.pieChartCollection!!.setSliceColor(intArrayOf(
                    R.color.green_normal))
            }
        }

    }

    private fun setExpenceData(expenseSummary: ExpenseSummary){
        val formatter = DecimalFormat("#,###,###")
        if(expenseSummary!= null){
            var maintenance = ""
            var transport = ""
            if(expenseSummary.Maintenance!= null){
                val maintainString = formatter.format(expenseSummary.Maintenance)
                 maintenance = "₹ "+maintainString
                binding?.tvPaidAmountExpence!!.text = maintenance
            }else{
                maintenance = "₹ 0"
                binding?.tvPaidAmountExpence!!.text = maintenance
            }

            if(expenseSummary.Transport!= null){
                val transportString = formatter.format(expenseSummary.Transport)
                 transport = "₹ "+transportString
                binding?.tvDueAmountExpence!!.text = transport
            }else{
                transport = "₹ 0"
                binding?.tvDueAmountExpence!!.text = transport
            }

            if(expenseSummary.Maintenance != null && expenseSummary.Transport!= null){
                val total = expenseSummary.Maintenance+expenseSummary.Transport
                val totalString = formatter.format(total)
                val tottalExpence = "₹ "+totalString
                binding?.tvTotalFinanceAmountExpence!!.text = tottalExpence

                binding?.pieChartExpence!!.setDataPoints(floatArrayOf(
                    expenseSummary.Maintenance.toFloat(),
                    expenseSummary.Transport.toFloat()))
                binding?.pieChartExpence!!.setCenterColor(R.color.white)
                binding?.pieChartExpence!!.setSliceColor(intArrayOf(
                    R.color.maroon,
                    R.color.orange))

            }else if(expenseSummary.Maintenance == null){
                val total = expenseSummary.Transport!!
                val totalString = formatter.format(total)
                val tottalExpence = "₹ "+totalString
                binding?.tvTotalFinanceAmountExpence!!.text = tottalExpence

                binding?.pieChartExpence!!.setDataPoints(floatArrayOf(
                    expenseSummary.Transport.toFloat()))
                binding?.pieChartExpence!!.setCenterColor(R.color.white)
                binding?.pieChartExpence!!.setSliceColor(intArrayOf(
                    R.color.orange))
            }else if(expenseSummary.Transport == null){
                val total = expenseSummary.Maintenance
                val totalString = formatter.format(total)
                val tottalExpence = "₹ "+totalString
                binding?.tvTotalFinanceAmountExpence!!.text = tottalExpence

                binding?.pieChartExpence!!.setDataPoints(floatArrayOf(
                    expenseSummary.Maintenance.toFloat()))
                binding?.pieChartExpence!!.setCenterColor(R.color.white)
                binding?.pieChartExpence!!.setSliceColor(intArrayOf(
                    R.color.maroon))

            }
        }
    }

    private fun  openCategoryPopup(from: Int){
        if(from == 0){
            val popup = PopupMenu(currActivity, rlCategory)
            //Inflating the Popup using xml file
            popup.menuInflater.inflate(R.menu.category_popup, popup.menu)

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener {
                if(it.title.equals(currActivity.resources.getString(R.string.by_feahead))){
                    dashboardViewModel?.let {
                        if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                            groupBy = "FEE_HEAD"
                            binding?.tvCategory!!.text = currActivity.resources.getString(R.string.by_feahead)
                            it.getCollectionData(groupBy,fromDate,toDate) }
                    }

                    dashboardViewModel?.getCollectionLiveData()?.observe(this, Observer {
                        setCollectionData(it)
                    })
                }else if(it.title.equals(currActivity.resources.getString(R.string.by_payment_method))){
                    dashboardViewModel?.let {
                        if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                            groupBy = "PAYMENT_METHOD"
                            binding?.tvCategory!!.text = currActivity.resources.getString(R.string.small_payment_method)
                            it.getCollectionData(groupBy,fromDate,toDate) }
                    }

                    dashboardViewModel?.getCollectionLiveData()?.observe(this, Observer {
                        setCollectionData(it)
                    })
                }
                true
            }

            popup.show()
        }else if(from == 1){
            val popup = PopupMenu(currActivity, rlCategoryExpence)
            //Inflating the Popup using xml file
            popup.menuInflater.inflate(R.menu.category_popup, popup.menu)

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener {
                if(it.title.equals(currActivity.resources.getString(R.string.by_feahead))){
                    dashboardViewModel?.let {
                        if(context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true){
                            expenseGroupBy = "FEE_HEAD"
                            binding?.tvCategoryExpence!!.text = currActivity.resources.getString(R.string.by_feahead)
                            it.getExpences(expenseGroupBy,expenseFromDate,expenseToDate)
                        }
                    }

                    dashboardViewModel?.getExpenseLiveData()?.observe(this, Observer {
                        setExpenceData(it)
                    })
                }else if(it.title.equals(currActivity.resources.getString(R.string.by_payment_method))){
                    dashboardViewModel?.let {
                        if(context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true){
                            expenseGroupBy = "PAYMENT_METHOD"
                            binding?.tvCategoryExpence!!.text = currActivity.resources.getString(R.string.small_payment_method)
                            it.getExpences(expenseGroupBy,expenseFromDate,expenseToDate)
                        }
                    }

                    dashboardViewModel?.getExpenseLiveData()?.observe(this, Observer {
                        setExpenceData(it)
                    })
                }
                true
            }

            popup.show()
        }

    }

    private fun openMenu(){
        val popup = PopupMenu(currActivity,rlDay)
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(com.akshar.one.R.menu.birthday_popup, popup.getMenu())

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener {
            if(it.title.equals(currActivity.resources.getString(R.string.today))){
                binding?.tvDay!!.text = currActivity.resources.getString(R.string.today)
                dashboardViewModel?.let {
                    if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                        it.getBirthdays(date!!,date!!) }
                }

                dashboardViewModel?.getBirthdayLiveData()?.observe(this, Observer {
                    birthDayList.clear()
                    birthDayList.addAll(it)
                    if(birthDayList.size > 0){
                        binding!!.rlSeeAll.visibility = View.VISIBLE
                        binding!!.rlBirthdayNotFound.visibility = View.GONE
                        binding!!.rvBirthdays.visibility = View.VISIBLE
                    }else{
                        binding!!.rlSeeAll.visibility = View.GONE
                        binding!!.rlBirthdayNotFound.visibility = View.VISIBLE
                        binding!!.rvBirthdays.visibility = View.GONE
                    }
                    birthDayadapter.notifyDataSetChanged()
                    // dashboardViewModel?.setTimeTableAdapter(it)
                })

            }else if(it.title.equals(currActivity.resources.getString(R.string.tomorrow))){

                binding?.tvDay!!.text = currActivity.resources.getString(R.string.tomorrow)
                val fromDate = AppUtil.getTomorrowDateString()
                dashboardViewModel?.let {
                    if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                        it.getBirthdays(fromDate,date!!) }
                }

                dashboardViewModel?.getBirthdayLiveData()?.observe(this, Observer {
                    birthDayList.clear()
                    birthDayList.addAll(it)
                    if(birthDayList.size > 0){
                        binding!!.rlSeeAll.visibility = View.VISIBLE
                        binding!!.rlBirthdayNotFound.visibility = View.GONE
                        binding!!.rvBirthdays.visibility = View.VISIBLE
                    }else{
                        binding!!.rlSeeAll.visibility = View.GONE
                        binding!!.rlBirthdayNotFound.visibility = View.VISIBLE
                        binding!!.rvBirthdays.visibility = View.GONE
                    }
                    birthDayadapter.notifyDataSetChanged()
                    // dashboardViewModel?.setTimeTableAdapter(it)
                })

            }else if(it.title.equals(currActivity.resources.getString(R.string.week))){

                binding?.tvDay!!.text = currActivity.resources.getString(R.string.week)
                val fromDate = AppUtil.getSevenDaysAfter()
                dashboardViewModel?.let {
                    if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                        it.getBirthdays(fromDate!!,date!!) }
                }

                dashboardViewModel?.getBirthdayLiveData()?.observe(this, Observer {
                    birthDayList.clear()
                    birthDayList.addAll(it)
                    if(birthDayList.size > 0){
                        binding!!.rlSeeAll.visibility = View.VISIBLE
                        binding!!.rlBirthdayNotFound.visibility = View.GONE
                        binding!!.rvBirthdays.visibility = View.VISIBLE
                    }else{
                        binding!!.rlSeeAll.visibility = View.GONE
                        binding!!.rlBirthdayNotFound.visibility = View.VISIBLE
                        binding!!.rvBirthdays.visibility = View.GONE
                    }
                    birthDayadapter.notifyDataSetChanged()
                    // dashboardViewModel?.setTimeTableAdapter(it)
                })


            }
            true
        }

        popup.show()//showing popup menu

}

    private fun openWeek(from: Int){
        if(from == 0){
            val popup = PopupMenu(currActivity, rlWeek)
            //Inflating the Popup using xml file
            popup.menuInflater.inflate(R.menu.collection_month_popup, popup.menu)

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener {
                if(it.title.equals(currActivity.resources.getString(R.string.today))){
                    dashboardViewModel?.let {
                        if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                            binding?.tvWeek!!.text = currActivity.resources.getString(R.string.today)
                            fromDate = date!!
                            toDate = date!!
                            it.getCollectionData(groupBy,fromDate,toDate) }
                    }

                    dashboardViewModel?.getCollectionLiveData()?.observe(this, Observer {
                        setCollectionData(it)
                    })
                }else if(it.title.equals(currActivity.resources.getString(R.string.yesterday))){
                    yesterdayDate = AppUtil.getYesterdayDateString()

                        dashboardViewModel?.let {
                            if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                                binding?.tvWeek!!.text = currActivity.resources.getString(R.string.yesterday)
                                fromDate = yesterdayDate!!
                                toDate = date!!
                                it.getCollectionData(groupBy,fromDate,toDate) }
                        }

                        dashboardViewModel?.getCollectionLiveData()?.observe(this, Observer {
                            setCollectionData(it)
                        })


                }else if(it.title.equals(currActivity.resources.getString(R.string.this_week))){
                    val startDateOfWeek = AppUtil.getSevenDaysBack()

                        dashboardViewModel?.let {
                            if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                                binding?.tvWeek!!.text = currActivity.resources.getString(R.string.week)
                                fromDate = startDateOfWeek!!
                                toDate = date!!
                                it.getCollectionData(groupBy,fromDate,toDate) }
                        }

                        dashboardViewModel?.getCollectionLiveData()?.observe(this, Observer {
                            setCollectionData(it)
                        })

                }else if(it.title.equals(currActivity.resources.getString(R.string.month))){

                    val startDateMonth = AppUtil.get30DaysBack()
                        dashboardViewModel?.let {
                            if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                                binding?.tvWeek!!.text = currActivity.resources.getString(R.string.month)
                                fromDate = startDateMonth!!
                                toDate = date!!
                                it.getCollectionData(groupBy,fromDate,toDate) }
                        }

                        dashboardViewModel?.getCollectionLiveData()?.observe(this, Observer {
                            setCollectionData(it)
                        })

                }else if(it.title.equals(currActivity.resources.getString(R.string.year))){
                        dashboardViewModel?.let {
                            if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                                binding?.tvWeek!!.text = currActivity.resources.getString(R.string.year)
                                fromDate = currentYearJan!!
                                toDate = date!!
                                it.getCollectionData(groupBy,fromDate,toDate) }
                        }

                        dashboardViewModel?.getCollectionLiveData()?.observe(this, Observer {
                            setCollectionData(it)
                        })
                }
                true
            }

            popup.show()
        }else if( from == 1){
            val popup = PopupMenu(currActivity, rlWeekExpence)
            //Inflating the Popup using xml file
            popup.menuInflater.inflate(com.akshar.one.R.menu.collection_month_popup, popup.menu)

            //registering popup with OnMenuItemClickListener
            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener {
                if (it.title.equals(currActivity.resources.getString(R.string.today))) {
                    dashboardViewModel?.let {
                        if(context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true){
                            binding?.tvWeekExpence!!.text = currActivity.resources.getString(R.string.today)
                            expenseFromDate = date!!
                            expenseToDate = date!!
                            it.getExpences(expenseGroupBy,expenseFromDate,expenseToDate)
                        }
                    }

                    dashboardViewModel?.getExpenseLiveData()?.observe(this, Observer {
                        setExpenceData(it)
                    })
                } else if (it.title.equals(currActivity.resources.getString(R.string.yesterday))) {
                    dashboardViewModel?.let {
                        if(context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true){
                            binding?.tvWeekExpence!!.text = currActivity.resources.getString(R.string.yesterday)
                            expenseFromDate = yesterdayDate!!
                            expenseToDate = date!!
                            it.getExpences(expenseGroupBy,expenseFromDate,expenseToDate)
                        }
                    }

                    dashboardViewModel?.getExpenseLiveData()?.observe(this, Observer {
                        setExpenceData(it)
                    })

                } else if (it.title.equals(currActivity.resources.getString(R.string.this_week))) {
                    val startDateOfWeek = AppUtil.getSevenDaysBack()
                    dashboardViewModel?.let {
                        if(context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true){
                            binding?.tvWeekExpence!!.text = currActivity.resources.getString(R.string.week)
                            expenseFromDate = startDateOfWeek!!
                            expenseToDate = date!!
                            it.getExpences(expenseGroupBy,expenseFromDate,expenseToDate)
                        }
                    }

                    dashboardViewModel?.getExpenseLiveData()?.observe(this, Observer {
                        setExpenceData(it)
                    })

                } else if (it.title.equals(currActivity.resources.getString(R.string.month))) {
                    val startDateMonth = AppUtil.get30DaysBack()
                    dashboardViewModel?.let {
                        if(context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true){
                            binding?.tvWeekExpence!!.text = currActivity.resources.getString(R.string.month)
                            expenseFromDate = startDateMonth!!
                            expenseToDate = date!!
                            it.getExpences(expenseGroupBy,expenseFromDate,expenseToDate)
                        }
                    }

                    dashboardViewModel?.getExpenseLiveData()?.observe(this, Observer {
                        setExpenceData(it)
                    })

                } else if (it.title.equals(currActivity.resources.getString(R.string.year))) {
                    dashboardViewModel?.let {
                        if(context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true){
                            binding?.tvWeekExpence!!.text = currActivity.resources.getString(R.string.year)
                            expenseFromDate = currentYearJan!!
                            expenseToDate = date!!
                            it.getExpences(expenseGroupBy,expenseFromDate,expenseToDate)
                        }
                    }

                    dashboardViewModel?.getExpenseLiveData()?.observe(this, Observer {
                        setExpenceData(it)
                    })

                }
                true
            }

            popup.show()
        }

    }


    private fun showDayDialog(){

    }

    private fun showProgressIndicator(isLoading: Boolean?) {
       // linProgressIndicator.visibility = if (isLoading == true) View.VISIBLE else View.GONE
    }
}
