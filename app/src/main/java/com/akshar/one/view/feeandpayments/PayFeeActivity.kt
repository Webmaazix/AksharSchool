package com.akshar.one.view.feeandpayments

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.akshar.one.R
import com.akshar.one.databinding.ActivityPayFeeBinding
import com.akshar.one.model.FeesDetailModel
import com.akshar.one.view.feeandpayments.adapter.FeeHeadAdapter
import kotlinx.android.synthetic.main.main_toolbar.view.*

class PayFeeActivity : AppCompatActivity(),View.OnClickListener {

    private var feesDetailModel = FeesDetailModel()
    private var binding : ActivityPayFeeBinding? = null
    private var currActivity : Activity = this
    lateinit var adapter : FeeHeadAdapter
    private var total : Double = 0.0
    var studentId  = 0


    companion object {
        fun open(currActivity: Activity, feesDetailModel : FeesDetailModel,studentId : Int) {
            val intent = Intent(currActivity, PayFeeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("feesDetailModel",feesDetailModel)
            intent.putExtra("studentId",studentId)
            currActivity.startActivity(intent)
            currActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(currActivity,R.layout.activity_pay_fee)
        feesDetailModel = intent.getSerializableExtra("feesDetailModel") as FeesDetailModel
        studentId = intent.getIntExtra("studentId",0)
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
                MakePaymentActivity.open(currActivity,feesDetailModel,total,studentId)
            }
        }
    }

    private fun setListner(){
        binding!!.toolbar.imgBack.setOnClickListener(this)
        binding!!.tvPayButton.setOnClickListener(this)
    }

    fun addAmount(amount : Double){
        if(total == 0.0){
            total = amount
        }else{
            total += amount
        }

        binding!!.tvTotalAmount.text = "Total :$total"
    }

    fun removeAmount(amount : Double){
        if(total == 0.0){

        }else{
            total -= amount
        }
        binding!!.tvTotalAmount.text = "Total :$total"
    }
}
