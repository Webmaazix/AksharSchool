package com.akshar.one.view.feeandpayments

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.akshar.one.R
import com.akshar.one.app.AksharSchoolApplication
import com.akshar.one.app.AksharSchoolApplication.Companion.context
import com.akshar.one.databinding.ActivityPayFeeBinding
import com.akshar.one.databinding.DialogSelectClassAndSectionBinding
import com.akshar.one.databinding.EmailPhonePopupBinding
import com.akshar.one.manager.SessionManager
import com.akshar.one.model.*
import com.akshar.one.util.AndroidUtil
import com.akshar.one.util.AppUtil
import com.akshar.one.util.AppUtils
import com.akshar.one.view.feeandpayments.adapter.FeeHeadAdapter
import com.akshar.one.view.messagecenter.adapter.ShiftListAdapter
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.feeandpayment.FeeAndPaymentViewModel
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.payumoney.core.PayUmoneyConfig
import com.payumoney.core.PayUmoneyConstants
import com.payumoney.core.PayUmoneySdkInitializer
import com.payumoney.core.entity.TransactionResponse
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager
import com.payumoney.sdkui.ui.utils.ResultModel
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.main_toolbar.view.*
import org.json.JSONObject
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import kotlin.collections.ArrayList


class PayFeeActivity : AppCompatActivity(),View.OnClickListener {

    private var feesDetailModel = FeesDetailModel()
    private var binding : ActivityPayFeeBinding? = null
    private var currActivity : Activity = this
    lateinit var adapter : FeeHeadAdapter
    private var total : Double = 0.0
    var studentId  = 0
    private var mAppPreference: AppPreference? = null
    private var mdialog : Dialog? = null
    private var paymentResponseModel = PaymentGatewayResponse()
    private var studentModel = StudentListModel()
    private var SelectedfeeTermList = ArrayList<FeeTermList>()
    private var dialogSelectClassSectionBinding : EmailPhonePopupBinding? = null
    private var dialog : Dialog? = null

    private var feeAndPaymentViewModel : FeeAndPaymentViewModel? = null

//    private var mPaymentParams: PayUmoneySdkInitializer.PaymentParam? = null


    companion object {
        fun open(currActivity: Activity, feesDetailModel : FeesDetailModel,studentId : Int,studentModel : StudentListModel) {
            val intent = Intent(currActivity, PayFeeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("feesDetailModel",feesDetailModel)
            intent.putExtra("studentModel",studentModel)
            intent.putExtra("studentId",studentId)
            currActivity.startActivity(intent)
            currActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(currActivity,R.layout.activity_pay_fee)
//        mAppPreference = AppPreference()
        feesDetailModel = intent.getSerializableExtra("feesDetailModel") as FeesDetailModel
        studentModel = intent.getSerializableExtra("studentModel") as StudentListModel
        studentId = intent.getIntExtra("studentId",0)

        currActivity.application?.let {
            feeAndPaymentViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(FeeAndPaymentViewModel::class.java)
        }


        setData()
        setListner()
    }

    private fun setAdapter(){
        binding!!.rvFeeHeads.setHasFixedSize(true)
        binding!!.rvFeeHeads.layoutManager = LinearLayoutManager(currActivity,
            LinearLayoutManager.VERTICAL,false)
        adapter = FeeHeadAdapter(
            currActivity,
            feesDetailModel.feeHeadList)

        binding!!.rvFeeHeads.adapter = adapter
    }


    private fun setData(){
        binding!!.toolbar.imgMenu.visibility = View.GONE
        binding!!.toolbar.imgBack.visibility = View.VISIBLE
        binding!!.toolbar.txtToolbarTitle.text =
            currActivity.resources.getString(R.string.fee_amp_payment)

        setAdapter()
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.imgBack ->{
                onBackPressed()
            }
            R.id.tvPayButton ->{
                if(SessionManager.getLoginRole()?.appName.equals("SmartParent",true)){
                    if(total == 0.0){
                        AppUtils.showToast(currActivity,"Please select a valid amount",true)
                    }else{
                        if(studentModel.studentContact.mobile == null || studentModel.studentContact.email == null){
                            showEmailPhonePopup()
                        }else{
                            val model  = PaymentGatewayRequest()
                            model.custName = studentModel.fullName
                            model.custId = studentId.toString()
                            model.gateway = "PayU"
                            model.custMobile = studentModel.studentContact.mobile!!
                            model.amount = total.toString()
                            model.custEmail = studentModel.studentContact.email!!
                            model.product = "FEE_PAYMENT"
                            for(data in SelectedfeeTermList){
                                val paymentSplits = PaymentSplits()
                                paymentSplits.name = studentModel.fullName
                                paymentSplits.studentFeeId = data.studentFeeId.toLong()
                                paymentSplits.feeHeadSetupId = data.feeHead.feeHeadSetupId.toLong()
                                paymentSplits.amount = data.dueAmount

                                model.paymentSplits.add(paymentSplits)
                            }
                            feeAndPaymentViewModel?.let {
                                if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                                    mdialog =  AppUtils.showProgress(currActivity)
                                    it.paymentGatewayRedirect(model) }
                            }

                        }
                        observers()
                    }


                }else{
                     MakePaymentActivity.open(currActivity,feesDetailModel,total,studentId)
                }


            }
        }
    }

    private fun showEmailPhonePopup(){
        dialog = Dialog(currActivity as Context)
        dialogSelectClassSectionBinding = DataBindingUtil.inflate(
            LayoutInflater.from(currActivity),
            R.layout.email_phone_popup, null, false
        )
        dialog!!.setContentView(dialogSelectClassSectionBinding!!.getRoot())
        Objects.requireNonNull<Window>(dialog!!.getWindow())
            .setBackgroundDrawableResource(android.R.color.transparent)

        if(studentModel.studentContact.mobile == null){
            dialogSelectClassSectionBinding!!.rlPhone.visibility = View.VISIBLE
        }else{
            dialogSelectClassSectionBinding!!.rlPhone.visibility = View.GONE
        }

        if(studentModel.studentContact.email == null){
            dialogSelectClassSectionBinding!!.rlEmail.visibility = View.VISIBLE
        }else{
            dialogSelectClassSectionBinding!!.rlEmail.visibility = View.GONE
        }
        dialogSelectClassSectionBinding!!.imgCancel.setOnClickListener{
            dialog!!.dismiss()
        }
        dialogSelectClassSectionBinding!!.tvCancel.setOnClickListener{
            dialog!!.dismiss()
        }
        dialogSelectClassSectionBinding!!.tvSubmit.setOnClickListener{
            if(validate()){
                dialogSelectClassSectionBinding!!.tvemailLabel.setText(currActivity.getString(R.string.enter_email))
                dialogSelectClassSectionBinding!!.tvemailLabel.setTextColor(currActivity.resources.getColor(R.color.black))
                dialogSelectClassSectionBinding!!.tvMobileNumberLabel.setText(currActivity.getString(R.string.enter_mobile_no))
                dialogSelectClassSectionBinding!!.tvMobileNumberLabel.setTextColor(currActivity.resources.getColor(R.color.black))
                if(studentModel.studentContact.mobile == null && studentModel.studentContact.email == null){
                    studentModel.studentContact.mobile = dialogSelectClassSectionBinding!!.etMobile.text.toString()
                    studentModel.studentContact.email = dialogSelectClassSectionBinding!!.etEmail.text.toString()
                }else if(studentModel.studentContact.mobile == null){
                    studentModel.studentContact.mobile = dialogSelectClassSectionBinding!!.etMobile.text.toString()
                }else if(studentModel.studentContact.email == null){
                    studentModel.studentContact.email = dialogSelectClassSectionBinding!!.etEmail.text.toString()
                }

                val model  = PaymentGatewayRequest()
                model.custName = studentModel.fullName
                model.custId = studentId.toString()
                model.gateway = "PayU"
                model.custMobile = studentModel.studentContact.mobile!!
                model.amount = total.toString()
                model.custEmail = studentModel.studentContact.email!!
                model.product = "FEE_PAYMENT"
                for(data in SelectedfeeTermList){
                    val paymentSplits = PaymentSplits()
                    paymentSplits.name = studentModel.fullName
                    paymentSplits.studentFeeId = data.studentFeeId.toLong()
                    paymentSplits.feeHeadSetupId = data.feeHead.feeHeadSetupId.toLong()
                    paymentSplits.amount = data.dueAmount

                    model.paymentSplits.add(paymentSplits)
                }
                feeAndPaymentViewModel?.let {
                    if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                        mdialog =  AppUtils.showProgress(currActivity)
                        it.paymentGatewayRedirect(model) }
                }

                observers()
                dialog!!.dismiss()

            }

        }

        dialog!!.setCancelable(true)
        dialog!!.show()
    }

    private fun validate(): Boolean {
        var isValid =  true

        if(studentModel.studentContact.email == null && studentModel.studentContact.mobile == null) {
            if (dialogSelectClassSectionBinding!!.etEmail.text.isNullOrEmpty()) {
                dialogSelectClassSectionBinding!!.tvemailLabel.setText("Please enter  email")
                dialogSelectClassSectionBinding!!.tvemailLabel.setTextColor(currActivity.resources.getColor(R.color.red))
                //AppUtils.showToast(currActivity, "Please enter  email", true)
                isValid = false
                return isValid
            } else if (!AppUtil.isValidEmail(dialogSelectClassSectionBinding!!.etEmail.text.toString())) {
                dialogSelectClassSectionBinding!!.tvemailLabel.setText("Please enter valid email")
                dialogSelectClassSectionBinding!!.tvemailLabel.setTextColor(currActivity.resources.getColor(R.color.red))
              //  AppUtils.showToast(currActivity, "Please enter valid email", true)
                isValid = false
                return isValid
            } else if (dialogSelectClassSectionBinding!!.etMobile.text.isNullOrEmpty()) {
                dialogSelectClassSectionBinding!!.tvMobileNumberLabel.setText("Please enter Mobile number")
                dialogSelectClassSectionBinding!!.tvMobileNumberLabel.setTextColor(currActivity.resources.getColor(R.color.red))
               // AppUtils.showToast(currActivity, "Please enter Mobile number", true)
                isValid = false
                return isValid
            } else if (dialogSelectClassSectionBinding!!.etMobile.length() < 10 ||
                dialogSelectClassSectionBinding!!.etMobile.length() > 10
            ) {
                dialogSelectClassSectionBinding!!.tvMobileNumberLabel.setText("Please enter valid Mobile number")
                dialogSelectClassSectionBinding!!.tvMobileNumberLabel.setTextColor(currActivity.resources.getColor(R.color.red))
              //  AppUtils.showToast(currActivity, "Please enter valid Mobile number", true)
                isValid = false
                return isValid
            }

        }else if(studentModel.studentContact.mobile == null){
            if (dialogSelectClassSectionBinding!!.etMobile.text.isNullOrEmpty()) {
                dialogSelectClassSectionBinding!!.tvMobileNumberLabel.setText("Please enter Mobile number")
                dialogSelectClassSectionBinding!!.tvMobileNumberLabel.setTextColor(currActivity.resources.getColor(R.color.red))
              //  AppUtils.showToast(currActivity,"Please enter Mobile number",true)
                isValid = false
                return isValid
            }else if(dialogSelectClassSectionBinding!!.etMobile.length() < 10 ||
                dialogSelectClassSectionBinding!!.etMobile.length() > 10){
                dialogSelectClassSectionBinding!!.tvMobileNumberLabel.setText("Please enter valid Mobile number")
                dialogSelectClassSectionBinding!!.tvMobileNumberLabel.setTextColor(currActivity.resources.getColor(R.color.red))
                //AppUtils.showToast(currActivity,"Please enter valid Mobile number",true)
                isValid = false
                return isValid
            }
        }else if(studentModel.studentContact.email == null){
            if (dialogSelectClassSectionBinding!!.etEmail.text.isNullOrEmpty()) {
                dialogSelectClassSectionBinding!!.tvemailLabel.setText("Please enter email")
                dialogSelectClassSectionBinding!!.tvemailLabel.setTextColor(currActivity.resources.getColor(R.color.red))
              //  AppUtils.showToast(currActivity,"Please enter  email",true)
                isValid = false
                return isValid
            } else if (!AppUtil.isValidEmail(dialogSelectClassSectionBinding!!.etEmail.text.toString())) {
                dialogSelectClassSectionBinding!!.tvemailLabel.setText("Please enter valid email")
                dialogSelectClassSectionBinding!!.tvemailLabel.setTextColor(currActivity.resources.getColor(R.color.red))
              //  AppUtils.showToast(currActivity,"Please enter valid email",true)
                isValid = false
                return isValid
            }
        }


        return isValid
    }


    private fun observers(){

        feeAndPaymentViewModel?.getErrorMutableLiveData()?.observe(this, Observer {
            it?.let {
                AppUtils.hideProgress(mdialog!!)
                AndroidUtil.showToast(currActivity, it.message,true)
            }
        })

        feeAndPaymentViewModel?.getPaymentGatewayResponseLiveData()?.observe(this, Observer {
            AppUtils.hideProgress(mdialog!!)
            paymentResponseModel = it

            launchPayUMoneyFlow()
        })


    }



    private fun launchPayUMoneyFlow() {

        val payUmoneyConfig = PayUmoneyConfig.getInstance()

        payUmoneyConfig.disableExitConfirmation(false)

        val builder = PayUmoneySdkInitializer.PaymentParam.Builder()

        val amount = paymentResponseModel.TXN_AMOUNT.toDouble()


        val txnId = paymentResponseModel.ORDER_ID
        //String txnId = "TXNID720431525261327973";
        val phone = paymentResponseModel.MOBILE_NO
        val productName = "FEE_PAYMENT"
        val firstName = paymentResponseModel.CUST_NAME
        val email = paymentResponseModel.EMAIL
        val udf1 = ""
        val udf2 = ""
        val udf3 = ""
        val udf4 = ""
        val udf5 = ""
        val udf6 = ""
        val udf7 = ""
        val udf8 = ""
        val udf9 = ""
        val udf10 = ""

        val appEnvironment =AksharSchoolApplication.getAppEnvironment()
        builder.setAmount(amount.toString())
            .setTxnId(txnId)
            .setPhone(phone)
            .setProductName(productName)
            .setFirstName(firstName)
            .setEmail(email)
            .setsUrl(appEnvironment.surl())
            .setfUrl(appEnvironment.furl())
            .setUdf1(udf1)
            .setUdf2(udf2)
            .setUdf3(udf3)
            .setUdf4(udf4)
            .setUdf5(udf5)
            .setUdf6(udf6)
            .setUdf7(udf7)
            .setUdf8(udf8)
            .setUdf9(udf9)
            .setUdf10(udf10)
            .setIsDebug(appEnvironment.debug())
            .setKey(paymentResponseModel.MERCHANT_KEY)
            .setMerchantId(paymentResponseModel.MERCHANT_ID)

        try {
           var mPaymentParams = builder.build()

            /*
            * Hash should always be generated from your server side.
            * */
            //    generateHashFromServer(mPaymentParams);

            /*            */
            /**
             * Do not use below code when going live
             * Below code is provided to generate hash from sdk.
             * It is recommended to generate hash from server side only.
             */
            mPaymentParams = calculateServerSideHashAndInitiatePayment1(mPaymentParams)

            if (AppPreference.selectedTheme !== -1) {
                PayUmoneyFlowManager.startPayUMoneyFlow(
                    mPaymentParams,
                    this,
                    AppPreference.selectedTheme,
                    true
                )
            } else {
                PayUmoneyFlowManager.startPayUMoneyFlow(
                    mPaymentParams,
                    this,
                    R.style.AppTheme_default,
                    true
                )
            }

        } catch (e: Exception) {
            // some exception occurred
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()

        }

    }

    private fun calculateServerSideHashAndInitiatePayment1(paymentParam: PayUmoneySdkInitializer.PaymentParam): PayUmoneySdkInitializer.PaymentParam {

        val stringBuilder = StringBuilder()
        val params = paymentParam.params
        stringBuilder.append(params[PayUmoneyConstants.KEY]!! + "|")
        stringBuilder.append(params[PayUmoneyConstants.TXNID]!! + "|")
        stringBuilder.append(params[PayUmoneyConstants.AMOUNT]!! + "|")
        stringBuilder.append(params[PayUmoneyConstants.PRODUCT_INFO]!! + "|")
        stringBuilder.append(params[PayUmoneyConstants.FIRSTNAME]!! + "|")
        stringBuilder.append(params[PayUmoneyConstants.EMAIL]!! + "|")
        stringBuilder.append(params[PayUmoneyConstants.UDF1]!! + "|")
        stringBuilder.append(params[PayUmoneyConstants.UDF2]!! + "|")
        stringBuilder.append(params[PayUmoneyConstants.UDF3]!! + "|")
        stringBuilder.append(params[PayUmoneyConstants.UDF4]!! + "|")
        stringBuilder.append(params[PayUmoneyConstants.UDF5]!! + "||||||")

        val appEnvironment = AksharSchoolApplication.getAppEnvironment()
        stringBuilder.append(appEnvironment.salt())

        val hash = hashCal(stringBuilder.toString())
        paymentParam.setMerchantHash(hash)

        return paymentParam
    }


    fun hashCal(str: String): String {
        val hashseq = str.toByteArray()
        val hexString = StringBuilder()
        try {
            val algorithm = MessageDigest.getInstance("SHA-512")
            algorithm.reset()
            algorithm.update(hashseq)
            val messageDigest = algorithm.digest()
            for (aMessageDigest in messageDigest) {
                val hex = Integer.toHexString(0xFF and aMessageDigest.toInt())
                if (hex.length == 1) {
                    hexString.append("0")
                }
                hexString.append(hex)
            }
        } catch (ignored: NoSuchAlgorithmException) {
        }

        return hexString.toString()
    }


    protected override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result Code is -1 send from Payumoney activity
        Log.d("MainActivity", "request code $requestCode resultcode $resultCode")
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data != null) {
            val transactionResponse = data.getParcelableExtra<TransactionResponse>(
                PayUmoneyFlowManager
                    .INTENT_EXTRA_TRANSACTION_RESPONSE
            )

            val resultModel = data.getParcelableExtra<ResultModel>(PayUmoneyFlowManager.ARG_RESULT)

            // Check which object is non-null
            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {
                if (transactionResponse.transactionStatus == TransactionResponse.TransactionStatus.SUCCESSFUL) {
                    //Success Transaction

                } else {
                    //Failure Transaction
                }



                // Response from Payumoney
                val payuResponse = transactionResponse.getPayuResponse()

                // Response from SURl and FURL
                val merchantResponse = transactionResponse.transactionDetails

                AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setMessage("Payu's Data : $payuResponse\n\n\n Merchant's Data: $merchantResponse")
                    .setPositiveButton(
                        android.R.string.ok
                    ) { dialog, whichButton -> dialog.dismiss() }.show()

                val json = JsonObject()
                val feeSplits = JsonObject()
                for(feeTermList in SelectedfeeTermList){
                    feeSplits.addProperty(feeTermList.studentFeeId.toString(),feeTermList.dueAmount.toString())
                }
                val payUResponse  = JSONObject(payuResponse)
                val parser = JsonParser()
                val payUjson = parser.parse(payUResponse.toString())
                json.add("feeSplits",feeSplits)
                json.add("payuResponse",payUjson)


                feeAndPaymentViewModel?.let {
                    if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                        it.sendPayUResponseToServer(json) }
                }

            } else if (resultModel != null && resultModel.error != null) {
                AppUtils.showToast(currActivity,"Error response : " + resultModel.error.transactionResponse,true)
              //  Log.d(TAG, "Error response : " + resultModel.error.transactionResponse)
            } else {
              //  AppUtils.showToast(currActivity,"Both objects are null!",true)
               // Log.d(TAG, "Both objects are null!")
            }
        }
    }

    private fun setListner(){
        binding!!.toolbar.imgBack.setOnClickListener(this)
        binding!!.tvPayButton.setOnClickListener(this)
    }

    fun addAmount(amount : Double,model : FeeTermList){
        if(total == 0.0){
            total = amount
        }else{
            total += amount
        }
        SelectedfeeTermList.add(model)
        binding!!.tvTotalAmount.text = "Total :$total"
    }

    fun removeAmount(amount : Double,model : FeeTermList){
        if(total == 0.0){

        }else{
            total -= amount
        }
        SelectedfeeTermList.remove(model)
        binding!!.tvTotalAmount.text = "Total :$total"
    }
}
