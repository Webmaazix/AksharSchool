package com.akshar.one.view.feeandpayments

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.akshar.one.R
import com.akshar.one.databinding.ActivityFeeDetailBinding
import com.akshar.one.manager.SessionManager
import com.akshar.one.model.FeesDetailModel
import com.akshar.one.model.StudentListModel
import com.akshar.one.view.feeandpayments.adapter.FeeHeadAdapter
import kotlinx.android.synthetic.main.main_toolbar.view.*


class FeeDetailActivity : AppCompatActivity(),View.OnClickListener{

    private var feeDetailModel = FeesDetailModel()
    private var binding : ActivityFeeDetailBinding? = null
    private var currActivity : Activity = this
    lateinit var adapter : FeeHeadAdapter
    var studentId  = 0
    private var studentListModel = StudentListModel()
    companion object {
        fun open(currActivity: Activity, feeDetailModel : FeesDetailModel,studentId : Int,studentModel : StudentListModel) {
            val intent = Intent(currActivity, FeeDetailActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("feeDetailModel",feeDetailModel)
            intent.putExtra("studentListModel",studentModel)
            intent.putExtra("studentId",studentId)
            currActivity.startActivity(intent)
            currActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(currActivity,R.layout.activity_fee_detail)
        feeDetailModel = intent.getSerializableExtra("feeDetailModel") as FeesDetailModel
        studentListModel = intent.getSerializableExtra("studentListModel") as StudentListModel
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
            feeDetailModel.feeHeadList)

        binding!!.rvFeeHeads.adapter = adapter
    }

    private fun setData(){
        binding!!.toolbar.imgMenu.visibility = View.GONE
        binding!!.toolbar.imgBack.visibility = View.VISIBLE
        binding!!.toolbar.txtToolbarTitle.text =
            currActivity.resources.getString(R.string.fee_details)
        var finalAmount = 0.0
        var dueAmount = 0.0
        var overDueAmount = 0.0
        for(data in  feeDetailModel.feeHeadList){
            for(price in data.feeTermList){
                if(finalAmount == 0.0){
                    finalAmount = price.finalAmount
                }else{
                    finalAmount += price.finalAmount
                }

                if(dueAmount == 0.0){
                    dueAmount = price.dueAmount
                }else{
                    dueAmount += price.dueAmount
                }

            }
        }
        binding!!.tvFinalAmount.text = "₹ $finalAmount"
        binding!!.tvDueAmount.text = "₹ $dueAmount"

        setAdapter()
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.imgBack ->{
                onBackPressed()
            }
            R.id.tvPay ->{
                    PayFeeActivity.open(currActivity, feeDetailModel, studentId, studentListModel)

            }
        }
    }

    private fun setListner(){
        binding!!.toolbar.imgBack.setOnClickListener(this)
        binding!!.tvPay.setOnClickListener(this)
    }
}
