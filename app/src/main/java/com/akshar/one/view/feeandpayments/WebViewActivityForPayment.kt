package com.akshar.one.view.feeandpayments

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import com.akshar.one.R
import com.akshar.one.databinding.ActivityPayementHistoryDetailBinding
import com.akshar.one.databinding.ActivityWebViewForPaymentBinding
import com.akshar.one.model.InvoiceModel
import com.akshar.one.model.PaymentHistoryModel
import com.akshar.one.view.feeandpayments.adapter.PaymentHistoryDetailAdapter
import com.akshar.one.viewmodels.feeandpayment.FeeAndPaymentViewModel
import kotlinx.android.synthetic.main.main_toolbar.view.*
import java.io.File

class WebViewActivityForPayment : AppCompatActivity(), View.OnClickListener {


    private var binding : ActivityWebViewForPaymentBinding? = null
    private var currActivity : Activity = this
    private var invoiceModel = InvoiceModel()



    companion object {
        fun open(currActivity: Activity, invoiceModel : InvoiceModel) {
            val intent = Intent(currActivity, WebViewActivityForPayment::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("invoiceModel",invoiceModel)
            currActivity.startActivity(intent)
            currActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(currActivity,R.layout.activity_web_view_for_payment)
        invoiceModel = intent.getSerializableExtra("invoiceModel") as InvoiceModel
        initViews()
    }

    private fun initViews(){
        binding!!.toolbar.imgMenu.visibility = View.GONE
        binding!!.toolbar.imgBack.visibility = View.VISIBLE
        binding!!.toolbar.txtToolbarTitle.text =
            currActivity.resources.getString(R.string.invoice)

        val webSettings = binding!!.webView.getSettings()
        webSettings.setJavaScriptEnabled(true)

//        webview.webViewClient = object : WebViewClient() {
//            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
//                // do your handling codes here, which url is the requested url
//                // probably you need to open that url rather than redirect:
//                view.loadUrl(url)
//                return false // then it is not handled by default action
//            }
//        }
        val webViewClient = MyBrowser()
        binding!!.webView.setWebViewClient(webViewClient)
        binding!!.webView.loadUrl("https://docs.google.com/gview?embedded=true&url=${invoiceModel.url}")
        setListner()

    }
    private fun setListner(){
        binding!!.toolbar.imgBack.setOnClickListener(this)

    }


    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.imgBack ->{
                onBackPressed()
            }
        }
    }
}

internal class MyBrowser : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        view.loadUrl(url)
        return true
    }
}
