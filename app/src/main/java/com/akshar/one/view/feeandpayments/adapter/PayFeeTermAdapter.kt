package com.akshar.one.view.feeandpayments.adapter

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.SplashActivity
import com.akshar.one.databinding.RowFeeDetailBinding
import com.akshar.one.databinding.RowFeeHeadDetailBinding
import com.akshar.one.databinding.RowFeeTermsForPayBinding
import com.akshar.one.databinding.RowTermListBinding
import com.akshar.one.manager.SessionManager
import com.akshar.one.model.FeeHeadList
import com.akshar.one.model.FeeTermList
import com.akshar.one.model.FeesDetailModel
import com.akshar.one.util.AppUtil
import com.akshar.one.util.AppUtils
import com.akshar.one.view.activity.MainActivity
import com.akshar.one.view.feeandpayments.PayFeeActivity
import com.akshar.one.view.welcome.WelcomeActivity


import java.util.ArrayList
import java.util.logging.Handler

class PayFeeTermAdapter(private val mContext: Activity, private val list: ArrayList<FeeTermList>?) :
    RecyclerView.Adapter<PayFeeTermAdapter.ViewHolder>() {

    lateinit var adapter : FeeTermAdapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_fee_terms_for_pay,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var amount = ""
        val model = list?.get(position)

        holder.binding.tvTermName.text = model?.feeTerm?.feeTerm
        holder.binding.etAmount.setText(model?.dueAmount.toString())

        holder.binding.etAmount.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                android.os.Handler().postDelayed({
                    if( p0!!.toString() == "" ||  p0.toString() == "0" || p0.toString().equals(" ")){
                        AppUtils.showToast(mContext,"please enter a valid amount",true)
                    }else{
                        model!!.dueAmount = p0.toString().toDouble()
                    }

                }, 100)


            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })

       holder.binding.cbPay.setOnCheckedChangeListener { buttonView, isChecked ->
           if(isChecked){
//               if(amount != "" ){
//                   (mContext as PayFeeActivity).addAmount(amount.toDouble())
//               }else{
                   (mContext as PayFeeActivity).addAmount(model?.dueAmount!!)
//               }

           }else{
//               if(amount!= ""){
//                   (mContext as PayFeeActivity).removeAmount(amount.toDouble())
//               }
               (mContext as PayFeeActivity).removeAmount(model?.dueAmount!!)
           }
       }


    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: RowFeeTermsForPayBinding = RowFeeTermsForPayBinding.bind(itemView)

    }
}
