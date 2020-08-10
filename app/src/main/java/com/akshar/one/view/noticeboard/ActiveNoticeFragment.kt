package com.akshar.one.view.noticeboard


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
import com.akshar.one.model.NoticeBoardModel
import com.akshar.one.util.AndroidUtil
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.noticeboard.NoticeBoardViewModel
import com.akshar.one.swipelayout.util.Attributes
import com.akshar.one.util.AppUtils

/**
 * A simple [Fragment] subclass.
 */
class ActiveNoticeFragment : Fragment() ,View.OnClickListener {


    private var currActivity : Activity? = null
    private var binding : FragmentActiveNoticeBinding? = null
    private var noticeBoardList = ArrayList<NoticeBoardModel>()
    private var noticeBoardViewModel: NoticeBoardViewModel? = null
    lateinit var adapter : MyNoticeBoardAdapter
    private var dialog : Dialog? = null
    private var mdialog : Dialog? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        currActivity = activity
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_active_notice,container,false)
        initViews()
        return  binding?.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.application?.let {
            noticeBoardViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(NoticeBoardViewModel::class.java)
        }

        noticeBoardViewModel?.let {
            if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
               mdialog =  AppUtils.showProgress(currActivity!!)
                it.getAllNotices(false) }
        }

        observers()
    }


    private fun initViews(){
        setAdapter()
    }

    private fun setAdapter(){
        binding!!.rlActiveNotice.setHasFixedSize(true)
        binding!!.rlActiveNotice.layoutManager = LinearLayoutManager(currActivity,
            LinearLayoutManager.VERTICAL,false)
        adapter = MyNoticeBoardAdapter(
            currActivity!!,
            noticeBoardList,
            false,this
        )
//        (adapter).mode = Attributes.Mode.Single
        binding!!.rlActiveNotice.adapter = adapter
    }

    private fun observers(){

        noticeBoardViewModel?.getErrorMutableLiveData()?.observe(this, Observer {
            it?.let {
                AppUtils.hideProgress(mdialog!!)
                AndroidUtil.showToast(context!!, it.message,true)
            }
        })

        noticeBoardViewModel?.getNoticeListLiveData()?.observe(this, Observer {
            AppUtils.hideProgress(mdialog!!)
            noticeBoardList.clear()
            noticeBoardList.addAll(it)
            if(noticeBoardList.size> 0){
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
