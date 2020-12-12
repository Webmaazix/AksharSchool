package com.akshar.one.view.feeandpayments

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CancellationSignal
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.print.*
import android.print.pdf.PrintedPdfDocument
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.akshar.one.R
import com.akshar.one.app.AksharSchoolApplication.Companion.context
import com.akshar.one.databinding.ActivityPayementHistoryDetailBinding
import com.akshar.one.model.InvoiceModel
import com.akshar.one.model.PaymentHistoryModel
import com.akshar.one.model.StudentListModel
import com.akshar.one.util.AndroidUtil
import com.akshar.one.util.AppUtil
import com.akshar.one.util.AppUtils
import com.akshar.one.view.feeandpayments.adapter.PaymentHistoryAdapter
import com.akshar.one.view.feeandpayments.adapter.PaymentHistoryDetailAdapter
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.feeandpayment.FeeAndPaymentViewModel
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.android.synthetic.main.main_toolbar.view.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class PayementHistoryDetailActivity : AppCompatActivity(),View.OnClickListener {

    private var paymentHistoryModel = PaymentHistoryModel()
    private var binding : ActivityPayementHistoryDetailBinding? = null
    private var currActivity : Activity = this
    lateinit var adapter : PaymentHistoryDetailAdapter
    internal var rootView: View? = null
    internal var file: File?= null
    private var mdialog : Dialog? = null
    private var invoiceModel = InvoiceModel()
    private var totalPrice = 0.0;
    private var feeAndPaymentViewModel : FeeAndPaymentViewModel? = null


    companion object {
        fun open(currActivity: Activity, paymentHistoryModel : PaymentHistoryModel) {
            val intent = Intent(currActivity, PayementHistoryDetailActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("paymentHistoryModel",paymentHistoryModel)
            currActivity.startActivity(intent)
            currActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(currActivity,R.layout.activity_payement_history_detail)
        paymentHistoryModel = intent.getSerializableExtra("paymentHistoryModel") as PaymentHistoryModel
        initViews()
        setData()
        setListner()
    }

    private fun initViews(){
        currActivity.application?.let {
            feeAndPaymentViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(FeeAndPaymentViewModel::class.java)
        }

        feeAndPaymentViewModel?.let {
            if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                mdialog =  AppUtils.showProgress(currActivity)
                it.getInVoice(paymentHistoryModel.invoiceNumber)}
        }

        observer()
    }

    private fun observer(){
        feeAndPaymentViewModel?.getErrorMutableLiveData()?.observe(this, androidx.lifecycle.Observer {
            it?.let {
                AppUtils.hideProgress(mdialog!!)
                AndroidUtil.showToast(currActivity, it.message,true)
            }
        })

        feeAndPaymentViewModel?.getInvoiceLiveData()?.observe(this,  androidx.lifecycle.Observer {
            AppUtils.hideProgress(mdialog!!)
            invoiceModel = it
        })
    }

    private fun setAdapter(){
        binding!!.rvTerms.setHasFixedSize(true)
        binding!!.rvTerms.layoutManager = LinearLayoutManager(currActivity,
            LinearLayoutManager.VERTICAL,false)
        adapter = PaymentHistoryDetailAdapter(
            currActivity,
            paymentHistoryModel.paymentsList)

        binding!!.rvTerms.adapter = adapter
    }


    private fun setData(){

        binding!!.toolbar.imgMenu.visibility = View.GONE
        binding!!.toolbar.imgBack.visibility = View.VISIBLE
        binding!!.toolbar.txtToolbarTitle.text =
            currActivity.resources.getString(R.string.payment_history_detail)

        binding!!.tvInvoiceNumber.text = paymentHistoryModel.invoiceNumber.toString()

        val paymentDate = AppUtil.formatDate(paymentHistoryModel.paymentDate)

        binding!!.tvDate.text = paymentDate
        binding!!.tvStudentName.text = paymentHistoryModel.student.fullName
        binding!!.tvPaymentMethod.text = paymentHistoryModel.paymentMethod
        binding!!.tvRefrenceNumber.text = paymentHistoryModel.paymentReferenceNbr
        for(model in paymentHistoryModel.paymentsList){
            if(totalPrice ==0.0){
                totalPrice = model.paymentAmount
            }else{
                totalPrice = totalPrice+model.paymentAmount
            }

        }
        binding!!.tvTotalAmount.setText(totalPrice.toString())
        setAdapter()
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.imgBack ->{
                onBackPressed()
            }
            R.id.tvDownloadInvoice ->{
                WebViewActivityForPayment.open(currActivity,invoiceModel)
            }
        }
    }

    private fun setListner(){
        binding!!.toolbar.imgBack.setOnClickListener(this)
        binding!!.tvDownloadInvoice.setOnClickListener(this)
    }

}
