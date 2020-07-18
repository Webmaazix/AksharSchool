package com.akshar.one.view.noticeboard


import android.app.Activity
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
import com.akshar.one.databinding.FragmentExpiredNoticeBinding
import com.akshar.one.model.NoticeBoardModel
import com.akshar.one.swipelayout.util.Attributes
import com.akshar.one.util.AndroidUtil
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.noticeboard.NoticeBoardViewModel

/**
 * A simple [Fragment] subclass.
 */
class ExpiredNotice : Fragment() ,View.OnClickListener{

    private var currActivity : Activity? = null
    private var binding : FragmentExpiredNoticeBinding? = null
    private var noticeBoardList = ArrayList<NoticeBoardModel>()
    private var noticeBoardViewModel: NoticeBoardViewModel? = null
    lateinit var adapter : MyNoticeBoardAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        currActivity = activity
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_expired_notice,container,false)
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
                it.getAllNotices(true) }
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
            true
        )
        (adapter as MyNoticeBoardAdapter).mode = Attributes.Mode.Single
        binding!!.rlActiveNotice.adapter = adapter

//        val itemTouchHelperCallback = RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, )
//        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding!!.rlActiveNotice)
    }

    private fun observers(){

        noticeBoardViewModel?.getErrorMutableLiveData()?.observe(this, Observer {
            it?.let {
                AndroidUtil.showToast(context!!, it.message,true)
            }
        })

        noticeBoardViewModel?.getNoticeListLiveData()?.observe(this, Observer {
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

//    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
//        if (viewHolder is MyNoticeBoardAdapter.ViewHolder) {
//            val deletedItem = noticeBoardList.get(viewHolder.adapterPosition)
//            val deletedIndex = viewHolder.adapterPosition
//
//            // remove the item from recycler view
//            adapter.removeItem(viewHolder.adapterPosition)
//
//        }
//    }


}