package com.akshar.one.view.feeandpayments


import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.akshar.one.R
import com.akshar.one.view.noticeboard.adapter.MyNoticeBoardAdapter
import com.akshar.one.databinding.FragmentActiveNoticeBinding
import com.akshar.one.databinding.FragmentFeesDetailsBinding
import com.akshar.one.databinding.FragmentPaymentHistoryBinding
import com.akshar.one.model.FeesDetailModel
import com.akshar.one.model.NoticeBoardModel
import com.akshar.one.model.PaymentHistoryModel
import com.akshar.one.model.StudentListModel
import com.akshar.one.util.AndroidUtil
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.noticeboard.NoticeBoardViewModel
import com.akshar.one.swipelayout.util.Attributes
import com.akshar.one.util.AppUtils
import com.akshar.one.view.feeandpayments.adapter.FeesDetailAdapter
import com.akshar.one.view.feeandpayments.adapter.PaymentHistoryAdapter
import com.akshar.one.viewmodels.feeandpayment.FeeAndPaymentViewModel

/**
 * A simple [Fragment] subclass.
 */
class PaymentHistoryFragment : Fragment() ,View.OnClickListener {


    private var currActivity : Activity? = null
    private var binding : FragmentPaymentHistoryBinding? = null
    private var paymentHistoryList = ArrayList<PaymentHistoryModel>()
    private var feeAndPaymentViewModel : FeeAndPaymentViewModel? = null
    lateinit var adapter : PaymentHistoryAdapter
    private var dialog : Dialog? = null
    private var mdialog : Dialog? = null
    private var studentModel = StudentListModel()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        currActivity = activity
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_payment_history,container,false)

        return  binding?.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



        studentModel = (currActivity as StudentFeesDetails).student
        initViews()
        observers()
    }


    private fun initViews(){
        setAdapter()
        activity?.application?.let {
            feeAndPaymentViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(FeeAndPaymentViewModel::class.java)
        }

        feeAndPaymentViewModel?.let {
            if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                mdialog =  AppUtils.showProgress(currActivity!!)
                it.getPaymentHistory((currActivity as StudentFeesDetails).student.studentProfileId!!) }
        }
    }

    private fun setAdapter(){
        binding!!.rlActiveNotice.setHasFixedSize(true)
        binding!!.rlActiveNotice.layoutManager = LinearLayoutManager(currActivity,
            LinearLayoutManager.VERTICAL,false)
        adapter = PaymentHistoryAdapter(
            currActivity!!,
            paymentHistoryList)

        binding!!.rlActiveNotice.adapter = adapter
    }

    private fun observers(){

        feeAndPaymentViewModel?.getErrorMutableLiveData()?.observe(this, Observer {
            it?.let {
                AppUtils.hideProgress(mdialog!!)
                AndroidUtil.showToast(context!!, it.message,true)
            }
        })

        feeAndPaymentViewModel?.getPaymentHistoryLiveData()?.observe(this, Observer {
            AppUtils.hideProgress(mdialog!!)
            paymentHistoryList.clear()
            paymentHistoryList.addAll(it)
            if(paymentHistoryList.size> 0){
                binding!!.rlNoDataFound.visibility = View.GONE
                binding!!.rlActiveNotice.visibility = View.VISIBLE
            }else{
                binding!!.rlNoDataFound.visibility = View.VISIBLE
                binding!!.rlActiveNotice.visibility = View.GONE
            }

            adapter.notifyDataSetChanged()
            // dashboardViewModel?.setTimeTableAdapter(it)
        })


    }

    override fun onClick(p0: View?) {

    }

    fun showEmptyBox(){
        binding!!.rlNoDataFound.visibility = View.VISIBLE
        binding!!.rlActiveNotice.visibility = View.GONE
    }


}
