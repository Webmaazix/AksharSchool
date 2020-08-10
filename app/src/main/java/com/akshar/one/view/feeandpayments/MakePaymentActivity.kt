package com.akshar.one.view.feeandpayments

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.akshar.one.R
import com.akshar.one.app.AksharSchoolApplication
import com.akshar.one.app.AksharSchoolApplication.Companion.context
import com.akshar.one.databinding.ActivityMakePaymentBinding
import com.akshar.one.databinding.ActivityPayFeeBinding
import com.akshar.one.databinding.DialogSelectClassAndSectionBinding
import com.akshar.one.model.*
import com.akshar.one.util.AndroidUtil
import com.akshar.one.util.AppUtils
import com.akshar.one.view.feeandpayments.adapter.BankAccountListAdapter
import com.akshar.one.view.feeandpayments.adapter.FeeHeadAdapter
import com.akshar.one.view.feeandpayments.adapter.PaymentMethodListAdapter
import com.akshar.one.view.messagecenter.adapter.ClassSectionAdapterForMessage
import com.akshar.one.view.messagecenter.adapter.EmployeeDepartmentAdapter
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.feeandpayment.FeeAndPaymentViewModel
import kotlinx.android.synthetic.main.activity_create_notice.*
import kotlinx.android.synthetic.main.activity_make_payment.*
import kotlinx.android.synthetic.main.main_toolbar.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MakePaymentActivity : AppCompatActivity(), View.OnClickListener{


    private var feesDetailModel = FeesDetailModel()
    private lateinit var binding : ActivityMakePaymentBinding
    private var currActivity : Activity = this
    private var feeAndPaymentViewModel : FeeAndPaymentViewModel? = null
    lateinit var adapter : FeeHeadAdapter
    private var total : Double = 0.0
    private var  myCalendar : Calendar? = null
    private var startDate : DatePickerDialog.OnDateSetListener? =null
    private var dialogSelectClassSectionBinding: DialogSelectClassAndSectionBinding? = null
    private var dialog: Dialog? = null
    var recievedBy = ""
    var remarks = ""
    lateinit var bankAccountAdapter : BankAccountListAdapter
    lateinit var paymentMethodAdapter : PaymentMethodListAdapter
    private var bankAccountList = ArrayList<BankAccount>()
    private var paymentMethodList = ArrayList<String>()
    private var mdialog : Dialog? = null
    private var SelectedbankModel = BankAccount()
    private var selectedPaymentMethod = ""
    var studentId  = 0

    companion object {
        fun open(currActivity: Activity, feesDetailModel : FeesDetailModel,total : Double,studentId : Int) {
            val intent = Intent(currActivity, MakePaymentActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("feesDetailModel",feesDetailModel)
            intent.putExtra("total",total)
            intent.putExtra("studentId",studentId)
            currActivity.startActivity(intent)
            currActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(currActivity,R.layout.activity_make_payment)
        feesDetailModel = intent.getSerializableExtra("feesDetailModel") as FeesDetailModel
        total = intent.getDoubleExtra("total",0.0)
        studentId = intent.getIntExtra("studentId",0)
        initViews()
    }

    private fun initViews(){

        binding.toolbar.imgMenu.visibility = View.GONE
        binding.toolbar.imgBack.visibility = View.VISIBLE
        binding.toolbar.txtToolbarTitle.text =
            currActivity.resources.getString(R.string.make_payment)

        currActivity.application?.let {
            feeAndPaymentViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(FeeAndPaymentViewModel::class.java)
        }

        feeAndPaymentViewModel?.let {
            if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
               // mdialog =  AppUtils.showProgress(currActivity)
                it.getBankAccountList() }
        }

        feeAndPaymentViewModel?.let {
            if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
              //  mdialog =  AppUtils.showProgress(currActivity)
                it.getPaymentMethod("PAYMENT_METHOD") }
        }

        observers()


        val academicYear = getString(R.string.academic_year)+" "+feesDetailModel.academicYear
        binding.tvAcademicYear.text = academicYear

        binding.tvTotalAmount.text = "₹ $total"
        myCalendar = Calendar.getInstance();
        setListner()
    }

    private fun setListner(){
        startDate = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar!!.set(Calendar.YEAR, year)
            myCalendar!!.set(Calendar.MONTH, monthOfYear)
            myCalendar!!.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel()
        }

        binding.etRecievedBy.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                recievedBy = p0!!.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })

        binding.etRemarksMethod.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                remarks  = p0!!.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }


        })

        binding.rlPaymentDate.setOnClickListener(this)
        binding.etPaymentMethod.setOnClickListener(this)
        binding.etBankAccountMethod.setOnClickListener(this)
        binding.tvSave.setOnClickListener(this)
        binding.toolbar.imgBack.setOnClickListener(this)
    }

    private fun observers(){

        feeAndPaymentViewModel?.getErrorMutableLiveData()?.observe(this, androidx.lifecycle.Observer {
            it?.let {
                AppUtils.hideProgress(mdialog!!)
                AndroidUtil.showToast(context!!, it.message,true)
            }
        })

        feeAndPaymentViewModel?.getBankAccountLiveData()?.observe(this, androidx.lifecycle.Observer {
           // AppUtils.hideProgress(mdialog!!)
            bankAccountList.clear()
            bankAccountList.addAll(it)
        })

        feeAndPaymentViewModel?.getPaymentMethodLiveData()?.observe(this, androidx.lifecycle.Observer {
           // AppUtils.hideProgress(mdialog!!)
            paymentMethodList.clear()
            paymentMethodList.addAll(it)
        })
        feeAndPaymentViewModel?.getSuccessLiveData()?.observe(this, androidx.lifecycle.Observer {
            AppUtils.hideProgress(mdialog!!)
            AppUtils.showToast(currActivity,"Payment saved Successfully",false)
        })
    }

    private fun updateLabel() {
        val myFormat = "yyyy-MM-dd" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.etPaymentDate.text = sdf.format(myCalendar!!.getTime())
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.imgBack ->{
                onBackPressed()
            }
            R.id.rlPaymentDate ->{
                DatePickerDialog(
                    this@MakePaymentActivity, startDate,
                    myCalendar!!.get(Calendar.YEAR), myCalendar!!.get(Calendar.MONTH),
                    myCalendar!!.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

            R.id.etPaymentMethod ->{
                openPaymetMethodList()
            }
            R.id.etBankAccountMethod ->{
                openBankAccountList()
            }
            R.id.tvSave ->{
                if(validation())
                sendPaymentRequest()
            }
        }
    }

    private fun openBankAccountList(){
        dialog = Dialog(currActivity as Context)
        dialogSelectClassSectionBinding = DataBindingUtil.inflate(
            LayoutInflater.from(currActivity),
            R.layout.dialog_select_class_and_section, null, false
        )
        dialog!!.setContentView(dialogSelectClassSectionBinding!!.getRoot())
        Objects.requireNonNull<Window>(dialog!!.getWindow())
            .setBackgroundDrawableResource(android.R.color.transparent)
        dialogSelectClassSectionBinding!!.rlClassesDropdown.setHasFixedSize(true)
        dialogSelectClassSectionBinding!!.rlClassesDropdown.layoutManager = LinearLayoutManager(
            currActivity,
            LinearLayoutManager.VERTICAL, false
        )
        bankAccountAdapter = BankAccountListAdapter(currActivity, bankAccountList)
        dialogSelectClassSectionBinding!!.rlClassesDropdown.adapter = bankAccountAdapter
        bankAccountAdapter.notifyDataSetChanged()

        dialogSelectClassSectionBinding!!.imgCancel.setOnClickListener {
            dialog!!.dismiss()
        }



        dialog!!.setCancelable(true)
        dialog!!.show()
    }
    private fun openPaymetMethodList(){
        dialog = Dialog(currActivity as Context)
        dialogSelectClassSectionBinding = DataBindingUtil.inflate(
            LayoutInflater.from(currActivity),
            R.layout.dialog_select_class_and_section, null, false
        )
        dialog!!.setContentView(dialogSelectClassSectionBinding!!.getRoot())
        Objects.requireNonNull<Window>(dialog!!.getWindow())
            .setBackgroundDrawableResource(android.R.color.transparent)
        dialogSelectClassSectionBinding!!.rlClassesDropdown.setHasFixedSize(true)
        dialogSelectClassSectionBinding!!.rlClassesDropdown.layoutManager = LinearLayoutManager(
            currActivity,
            LinearLayoutManager.VERTICAL, false
        )
        paymentMethodAdapter = PaymentMethodListAdapter(currActivity, paymentMethodList)
        dialogSelectClassSectionBinding!!.rlClassesDropdown.adapter = paymentMethodAdapter
        paymentMethodAdapter.notifyDataSetChanged()

        dialogSelectClassSectionBinding!!.imgCancel.setOnClickListener {
            dialog!!.dismiss()
        }



        dialog!!.setCancelable(true)
        dialog!!.show()
    }

    fun sendPaymentMethod(model : String){
        selectedPaymentMethod = model
        binding.etPaymentMethod.text = model


        if(selectedPaymentMethod.equals("Cash")){
            binding.rlCheckNumber.visibility = View.GONE
            binding.rlTransactionStatus.visibility = View.GONE
        }else if(selectedPaymentMethod.equals("Cheque")){
            binding.rlCheckNumber.visibility = View.VISIBLE
            binding.rlTransactionStatus.visibility = View.VISIBLE
            binding.tvCheckNumberLabel.text = resources.getString(R.string.check_number)
        }else if(selectedPaymentMethod.equals("Online Transfer")){
            binding.rlCheckNumber.visibility = View.VISIBLE
            binding.rlTransactionStatus.visibility = View.VISIBLE
            binding.tvCheckNumberLabel.text = resources.getString(R.string.transaction_id)
        }else if(selectedPaymentMethod.equals("PayU")){
            binding.rlCheckNumber.visibility = View.VISIBLE
            binding.rlTransactionStatus.visibility = View.VISIBLE
            binding.tvCheckNumberLabel.text = resources.getString(R.string.transaction_id)
        }

        dialog!!.dismiss()
    }

    fun sendBankName(model : BankAccount){
        SelectedbankModel = model
        binding.etBankAccountMethod.text = model.bankName
        dialog!!.dismiss()


    }

    private fun validation() : Boolean{
        var isValid = true

        if(binding.etPaymentDate.text.toString().equals("")){
            isValid = false
            AppUtils.showToast(currActivity,"Payment date is required",true)
        }else if(binding.etRecievedBy.text.toString().equals("")){
            isValid = false
            AppUtils.showToast(currActivity,"Kindly add receiver details",true)
        }else if(selectedPaymentMethod.equals("")){
            isValid = false
            AppUtils.showToast(currActivity,"Payment Method is required",true)
        }else if(binding.etRemarksMethod.text.toString().equals("")){
            isValid = false
            AppUtils.showToast(currActivity,"Kindly add remarks",true)
        }else if(binding.etBankAccountMethod.text.toString().equals("")){
            isValid = false
            AppUtils.showToast(currActivity,"please add bank account",true)
        }else if(selectedPaymentMethod.equals("Cheque") ||
            selectedPaymentMethod.equals("Online Transfer")||selectedPaymentMethod.equals("PayU")){
            if(binding.etTransactionStatus.equals("")){
                isValid = false
                AppUtils.showToast(currActivity,"please add transaction status",true)
            }

        }

        return isValid
    }

    fun sendPaymentRequest(){
        val model = PaymentRequest()
        model.invoiceNumber = null
        model.controlNumber = null
        model.paymentMethod = selectedPaymentMethod
        if(selectedPaymentMethod.equals("Cash")){
            model.checkNbr = null
        }else if(selectedPaymentMethod.equals("Cheque")){
            model.checkNbr = binding.etCheckNumber.text.toString()
            model.paymentReferenceNbr = null
        }else if(selectedPaymentMethod.equals("Online Transfer")){
            model.checkNbr = null
            model.paymentReferenceNbr = binding.etCheckNumber.text.toString()
        }else if(selectedPaymentMethod.equals("PayU")){
            model.checkNbr = null
            model.paymentReferenceNbr = binding.etCheckNumber.text.toString()
        }
        model.paymentDate = binding.etPaymentDate.text.toString()
        model.paymentDescription = binding.etRemarksMethod.text.toString()
        val bankAccount = BankAccountRequest()
        bankAccount.bankAccountId = SelectedbankModel.bankAccountId
        model.bankAccount = bankAccount

        val student = StudentRequest()
        student.studentProfileId = studentId
        model.student = student
        val paymentListt = ArrayList<PaymentListRequest>()
        for (data in feesDetailModel.feeHeadList){
            for (terms in data.feeTermList){
                val paymentList = PaymentListRequest()
                paymentList.studentFeeId = terms.studentFeeId
                paymentList.paymentAmount = terms.dueAmount
                paymentListt.add(paymentList)
            }
        }

        model.paymentsList = paymentListt

        feeAndPaymentViewModel?.let {
            if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                mdialog =  AppUtils.showProgress(currActivity)
                it.addFeePayment(model) }
        }
    }
}
