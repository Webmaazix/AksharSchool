package com.akshar.one.view.feeandpayments

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.akshar.one.R
import com.akshar.one.databinding.ActivityPayementHistoryDetailBinding
import com.akshar.one.model.PaymentHistoryModel
import com.akshar.one.model.StudentListModel
import com.akshar.one.util.AppUtil
import com.akshar.one.view.feeandpayments.adapter.PaymentHistoryAdapter
import com.akshar.one.view.feeandpayments.adapter.PaymentHistoryDetailAdapter
import kotlinx.android.synthetic.main.main_toolbar.view.*

class PayementHistoryDetailActivity : AppCompatActivity(),View.OnClickListener {

    private var paymentHistoryModel = PaymentHistoryModel()
    private var binding : ActivityPayementHistoryDetailBinding? = null
    private var currActivity : Activity = this
    lateinit var adapter : PaymentHistoryDetailAdapter

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
        setData()
        setListner()
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
            currActivity.resources.getString(R.string.fee_amp_payment)

        binding!!.tvInvoiceNumber.text = paymentHistoryModel.invoiceNumber.toString()

        val paymentDate = AppUtil.formatDate(paymentHistoryModel.paymentDate)

        binding!!.tvDate.text = paymentDate
        binding!!.tvStudentName.text = paymentHistoryModel.student.fullName
        binding!!.tvPaymentMethod.text = paymentHistoryModel.paymentMethod
        setAdapter()
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.imgBack ->{
                onBackPressed()
            }
        }
    }

    private fun setListner(){
        binding!!.toolbar.imgBack.setOnClickListener(this)
    }
}
