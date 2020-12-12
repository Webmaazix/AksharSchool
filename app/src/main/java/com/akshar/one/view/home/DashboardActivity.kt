package com.akshar.one.view.home

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.se.omapi.Session
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.akshar.one.R
import com.akshar.one.view.home.adapter.BirthDayAdapter
import com.akshar.one.view.home.adapter.TimeTableAdapter
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
import kotlin.collections.ArrayList
import android.view.*
import androidx.appcompat.widget.PopupMenu
import com.akshar.one.util.AppUtils
import com.akshar.one.view.birthday.BirthdayActivity
import com.akshar.one.view.activity.MainActivity
import com.akshar.one.view.home.adapter.CollectionAdapter
import com.akshar.one.view.home.adapter.ExpencesAdapter
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
    private var timeTableList = ArrayList<PeriodTimeTable>()
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
    private lateinit var collectionAdapter : CollectionAdapter
    private lateinit var expencesAdapter : ExpencesAdapter
    private var collectionList = ArrayList<FeePayment>()
    private var expenceList = ArrayList<FeePayment>()
     var collectionAmountFloatArray = ArrayList<Float>()
    var collectionColorArray = ArrayList<Int>()
    var colorsArray = Array<Int>(12){0}

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
        colorsArray = arrayOf(R.color.light_blue,R.color.green_normal,
            R.color.light_yellow,R.color.dark_yellow,
            R.color.orange,R.color.science_blue,R.color.maroon,
            R.color.hindi_orange,R.color.english_pink,R.color.bio_purple,
            R.color.physics_pink,
            R.color.socialstudy_purple,R.color.civics_green,
            R.color.chemistry_blue,R.color.economics_purple)

        return binding?.root
    }

    private fun setAdapter(){
        binding!!.rvClassRooms.setHasFixedSize(true)
        binding!!.rvClassRooms.layoutManager = LinearLayoutManager(currActivity,LinearLayoutManager.HORIZONTAL,false)
        adapter = TimeTableAdapter(currActivity, timeTableList)
        binding!!.rvClassRooms.adapter = adapter


        binding!!.rvBirthdays.setHasFixedSize(true)
        binding!!.rvBirthdays.layoutManager = LinearLayoutManager(currActivity,LinearLayoutManager.VERTICAL,false)
        birthDayadapter =
            BirthDayAdapter(currActivity, birthDayList)
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
                showProgressBar()
                it.getTimeTableOfEmployee(employeeId,date!!) }
        }

        dashboardViewModel?.let {
            if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
             //   showProgressBar()
                it.getBirthdays(date!!,date!!) }
        }

        dashboardViewModel?.let {
            if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                it.getAllFinanceSummery(currentYearJan!!,date!!) }
        }

        setCollectionAdapter()
        observers()
    }

    private fun observers() {
        dashboardViewModel?.getErrorMutableLiveData()?.observe(this, Observer {
            hideProgressBar()
            it?.let {
                AndroidUtil.showToast(context!!, it.message,true)
            }
        })

        dashboardViewModel?.getCollectionErrorMutableLiveData()?.observe(this, Observer {
            it?.let {
                if(it.status == 404){
                   binding!!.pieChartCollection.visibility = View.GONE
                   binding!!.rlPaidCollection.visibility = View.GONE
//                   binding!!.rlDueCollection.visibility = View.GONE
                   binding!!.imgEmptyCollection.visibility = View.VISIBLE
                   binding!!.tvErrorMessage.visibility = View.VISIBLE
                   binding!!.tvErrorMessage.text = it.message
                   binding!!.tvTotalFinanceAmountCollection.text = "₹ 0"
                }else{
                    //  AndroidUtil.showToast(context!!, it.message,true)
                }

            }
        })

        dashboardViewModel?.getExpenseErrorMutableLiveData()?.observe(this, Observer {
            it?.let {
                if(it.status == 404){
                    binding!!.pieChartExpence.visibility = View.GONE
                    binding!!.rlPaidExpence.visibility = View.GONE
//                    binding!!.rlDueExpence.visibility = View.GONE
                    binding!!.imgEmptyExpense.visibility = View.VISIBLE
                    binding!!.tvTotalFinanceAmountExpence.text = "₹ 0"
                    binding!!.tvErrorMessageExpence.visibility = View.VISIBLE
                    binding!!.tvErrorMessageExpence.text = it.message
                }else{
                  //  AndroidUtil.showToast(context!!, it.message,true)
                }

            }
        })

        dashboardViewModel?.getTimeTableLiveData()?.observe(this, Observer {
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

        dashboardViewModel?.getBirthdayLiveData()?.observe(this, Observer {
          //  hideProgressBar()
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

//        dashboardViewModel?.getIsLoading()?.observe(this, Observer {
//            showProgressIndicator(it)
//        })

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
        binding!!.tvSeeAllCollection.setOnClickListener(this)
        binding!!.tvSeeAllExpence.setOnClickListener(this)
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
            R.id.tvSeeAllCollection ->{
                ExpandCollectionAndExpenceActivity.open(currActivity,collectionList)
            }
            R.id.tvSeeAllExpence ->{
                ExpandCollectionAndExpenceActivity.open(currActivity,collectionList)
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
                R.color.green_normal,
                R.color.light_yellow,
                R.color.maroon))
        }

        if(it.expenseSummary!= null){
            expenceList.clear()
            collectionAmountFloatArray.clear()
            collectionColorArray.clear()
            expenceList.addAll(it.expenseSummary)
            expencesAdapter.notifyDataSetChanged()
            var totalAmount = 0.0

            if(expenceList.size > 4){
                binding!!.tvSeeAllExpence.visibility =View.VISIBLE
            }else{
                binding!!.tvSeeAllExpence.visibility =View.GONE
            }

            for(i in 0 until it.expenseSummary.size){
                collectionAmountFloatArray.add(it.expenseSummary[i].value.toFloat())
                collectionColorArray.add(colorsArray[i])

                if(totalAmount == 0.0){
                    totalAmount = it.expenseSummary[i].value.toDouble()
                }else {
                    totalAmount = totalAmount+it.expenseSummary[i].value.toDouble()
                }
            }

            val totalString = formatter.format(totalAmount)
            val tottalExpence = "₹ $totalString"
            binding?.tvTotalFinanceAmountExpence!!.text = tottalExpence

            Handler().postDelayed({
                binding?.pieChartExpence!!.setDataPoints(collectionAmountFloatArray.toFloatArray())
                binding?.pieChartExpence!!.setCenterColor(R.color.white)
                binding?.pieChartExpence!!.setSliceColor(collectionColorArray.toIntArray())
            },100)
        }
        if(it.feePayment != null){

            collectionList.clear()
            collectionAmountFloatArray.clear()
            collectionColorArray.clear()
            collectionList.addAll(it.feePayment)
            collectionAdapter.notifyDataSetChanged()
            var totalAmount = 0.0

            if(collectionList.size > 4){
                binding!!.tvSeeAllCollection.visibility = View.VISIBLE
            }else{
                binding!!.tvSeeAllCollection.visibility = View.GONE
            }

            for(i in 0 until it.feePayment.size){
                collectionAmountFloatArray.add(it.feePayment[i].value.toFloat())
                collectionColorArray.add(colorsArray[i])

                if(totalAmount == 0.0){
                    totalAmount = it.feePayment[i].value.toDouble()
                }else {
                    totalAmount = totalAmount+it.feePayment[i].value.toDouble()
                }
            }
            val totalString = formatter.format(totalAmount)
            val tottalExpence = "₹ $totalString"
            binding?.tvTotalFinanceAmountCollection!!.text = tottalExpence


            Handler().postDelayed({


                   binding?.pieChartCollection!!.setDataPoints(collectionAmountFloatArray.toFloatArray())
                    binding?.pieChartCollection!!.setCenterColor(R.color.white)
                    binding?.pieChartCollection!!.setSliceColor(collectionColorArray.toIntArray())

            },100)

        }

    }

    private fun setCollectionAdapter(){
        binding!!.rvCollection.setHasFixedSize(true)
        binding!!.rvCollection.layoutManager = LinearLayoutManager(currActivity,LinearLayoutManager.VERTICAL,false)
        collectionAdapter = CollectionAdapter(currActivity,collectionList)
        binding!!.rvCollection.adapter = collectionAdapter

        binding!!.rvExpence.setHasFixedSize(true)
        binding!!.rvExpence.layoutManager = LinearLayoutManager(currActivity,LinearLayoutManager.VERTICAL,false)
        expencesAdapter = ExpencesAdapter(currActivity,expenceList)
        binding!!.rvExpence.adapter = expencesAdapter
    }

    private fun setCollectionData(feePayment: ArrayList<FeePayment>?){
        binding!!.pieChartCollection.visibility = View.VISIBLE
        binding!!.rlPaidCollection.visibility = View.VISIBLE
        binding!!.rvCollection.visibility = View.VISIBLE
        binding!!.imgEmptyCollection.visibility = View.GONE
        binding!!.tvErrorMessage.visibility = View.GONE
        val formatter = DecimalFormat("#,###,###")
        collectionList.clear()
        collectionAmountFloatArray.clear()
        collectionColorArray.clear()
        collectionList.addAll(feePayment!!)
        collectionAdapter.notifyDataSetChanged()
        var totalAmount = 0.0

        if(collectionList.size > 4){
            binding!!.tvSeeAllCollection.visibility = View.VISIBLE
        }else {
            binding!!.tvSeeAllCollection.visibility = View.GONE
        }

        for(i in 0 until feePayment.size){
            collectionAmountFloatArray.add(feePayment[i].value.toFloat())
            collectionColorArray.add(colorsArray[i])

            if(totalAmount == 0.0){
                totalAmount = feePayment[i].value.toDouble()
            }else {
                totalAmount = totalAmount+feePayment[i].value.toDouble()
            }
        }

        val totalString = formatter.format(totalAmount)
        val tottalExpence = "₹ $totalString"
        binding?.tvTotalFinanceAmountCollection!!.text = tottalExpence

        Handler().postDelayed({

            binding?.pieChartCollection!!.setDataPoints(collectionAmountFloatArray.toFloatArray())
            binding?.pieChartCollection!!.setCenterColor(R.color.white)
            binding?.pieChartCollection!!.setSliceColor(collectionColorArray.toIntArray())

        },100)

    }

    private fun setExpenceData(expenseSummary: ArrayList<FeePayment>){
        binding!!.pieChartExpence.visibility = View.VISIBLE
        binding!!.rlPaidExpence.visibility = View.VISIBLE
        binding!!.imgEmptyExpense.visibility = View.GONE
        binding!!.tvErrorMessageExpence.visibility = View.GONE
        val formatter = DecimalFormat("#,###,###")
        var totalAmount = 0.0

        expenceList.clear()
        collectionAmountFloatArray.clear()
        collectionColorArray.clear()
        expenceList.addAll(expenseSummary)
        expencesAdapter.notifyDataSetChanged()
        if(expenceList.size > 4){
            binding!!.tvSeeAllExpence.visibility = View.VISIBLE
        }else {
            binding!!.tvSeeAllExpence.visibility = View.GONE
        }

        for(i in 0 until expenseSummary.size){
            collectionAmountFloatArray.add(expenseSummary[i].value.toFloat())
            collectionColorArray.add(colorsArray[i])

            if(totalAmount == 0.0){
                totalAmount = expenseSummary[i].value.toDouble()
            }else {
                totalAmount = totalAmount+expenseSummary[i].value.toDouble()
            }
        }

        val totalString = formatter.format(totalAmount)
        val tottalExpence = "₹ $totalString"
        binding?.tvTotalFinanceAmountExpence!!.text = tottalExpence

        Handler().postDelayed({



            binding?.pieChartExpence!!.setDataPoints(collectionAmountFloatArray.toFloatArray())
            binding?.pieChartExpence!!.setCenterColor(R.color.white)
            binding?.pieChartExpence!!.setSliceColor(collectionColorArray.toIntArray())
        },100)
    }

    private fun  openCategoryPopup(from : Int){
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
            popup.menuInflater.inflate(R.menu.expense_category_popup, popup.menu)

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener {
                if(it.title.equals(currActivity.resources.getString(R.string.category))){
                    dashboardViewModel?.let {
                        if(context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true){
                            expenseGroupBy = "FEE_HEAD"
                            binding?.tvCategoryExpence!!.text = currActivity.resources.getString(R.string.category)
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
        popup.getMenuInflater().inflate(R.menu.birthday_popup, popup.menu)

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
        }else if(from == 1){
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
                    yesterdayDate = AppUtil.getYesterdayDateString()
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
                            expenseToDate = date!!+6
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

    fun showProgressBar(){
        dialog =  AppUtils.showProgress(currActivity)
    }

    fun hideProgressBar(){
        AppUtils.hideProgress(dialog)
    }
}
