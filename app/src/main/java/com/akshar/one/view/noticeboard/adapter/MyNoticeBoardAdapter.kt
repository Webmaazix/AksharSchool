package com.akshar.one.view.noticeboard.adapter

import android.app.Activity
import android.app.Application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.databinding.RowExpirednoticeBinding
import com.akshar.one.databinding.RowNoticeBinding
import com.akshar.one.model.NoticeBoardModel
import com.akshar.one.view.noticeboard.CreateNoticeActivity
import com.akshar.one.swipelayout.SimpleSwipeListener
import com.akshar.one.swipelayout.SwipeLayout
import com.akshar.one.swipelayout.adapters.RecyclerSwipeAdapter
import com.akshar.one.util.AndroidUtil
import com.akshar.one.util.AppUtil
import com.akshar.one.view.noticeboard.ActiveNoticeFragment
import com.akshar.one.view.noticeboard.ExpiredNotice
import com.akshar.one.view.noticeboard.NoticeDetailActivity
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.noticeboard.NoticeBoardViewModel
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import kotlinx.android.synthetic.main.row_notice.view.*
import java.util.ArrayList

class MyNoticeBoardAdapter(private val mContext: Activity, private val noticeList: ArrayList<NoticeBoardModel>?,
                           private var expired : Boolean, private var fragment : Fragment) :
    RecyclerView.Adapter<MyNoticeBoardAdapter.ViewHolder>() {


//    override fun getSwipeLayoutResourceId(position: Int): Int {
//        return R.id.swipe
//
//    }

    private var noticeBoardViewModel: NoticeBoardViewModel? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        noticeBoardViewModel = ViewModelProvider(
            ViewModelStore(),
            ViewModelFactory(mContext.applicationContext as Application)
        ).get(NoticeBoardViewModel::class.java)

        if(expired){
            return ViewHolder(
                LayoutInflater.from(mContext).inflate(
                    R.layout.row_expirednotice,
                    parent,
                    false
                )
            )

        }else{
            return ViewHolder(
                LayoutInflater.from(mContext).inflate(
                    R.layout.row_notice,
                    parent,
                    false
                )
            )

        }
    }


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val noticeModel = noticeList?.get(position)



        if(expired){
//            viewHolder.expiredBinding?.swipe!!.setShowMode(SwipeLayout.ShowMode.LayDown)
//            viewHolder.expiredBinding?.swipe!!.addSwipeListener(object : SimpleSwipeListener() {
//                override fun onOpen(layout: SwipeLayout) {
//                    YoYo.with(Techniques.Tada).duration(500).delay(100)
//                        .playOn(layout.findViewById(R.id.imgDelete))
//                }
//            })

            viewHolder.expiredBinding!!.tvTopicName.text = noticeModel?.title
            viewHolder.expiredBinding!!.tvDesc.text = noticeModel?.description
            val startDate = AppUtil.formatDate(noticeModel?.startDate!!)
            val endtDate = AppUtil.formatDate(noticeModel.endDate)
            val date = "$startDate - $endtDate"
            viewHolder.expiredBinding!!.tvDate.text = date

            viewHolder.expiredBinding!!.rlDelete.setOnClickListener{
                removeItem(position)
            }
//            viewHolder.expiredBinding!!.rlEditNotice.setOnClickListener{
//                updateItem(noticeModel)
//            }
            viewHolder.expiredBinding!!.rlForground.setOnClickListener{
                NoticeDetailActivity.open(mContext,noticeModel)
            }

        }else{

            var colorRes = 0
            when (position % 4) {
                0 -> colorRes = R.color.blue_notice
                1 -> colorRes = R.color.pink_notice
                2 -> colorRes = R.color.purple_notice
                3 -> colorRes = R.color.green_notice
            }
            viewHolder.binding?.rlBottom!!.backgroundTintList = mContext.resources.getColorStateList(colorRes)
//            viewHolder.binding?.swipe!!.setShowMode(SwipeLayout.ShowMode.LayDown)
//            viewHolder.binding?.swipe!!.addSwipeListener(object : SimpleSwipeListener() {
//                override fun onOpen(layout: SwipeLayout) {
//                    YoYo.with(Techniques.Tada).duration(500).delay(100)
//                        .playOn(layout.findViewById(R.id.imgDelete))
//                }
//            })

            viewHolder.binding!!.tvTopicName.text = noticeModel?.title
            viewHolder.binding!!.tvDesc.text = noticeModel?.description
            val startDate = AppUtil.formatDate(noticeModel?.startDate!!)
            val endtDate = AppUtil.formatDate(noticeModel.endDate)
            val date = "$startDate - $endtDate"
            viewHolder.binding!!.tvDate.text = date

            viewHolder.binding!!.rlDelete.setOnClickListener{
                removeItem(position)
            }
            viewHolder.binding!!.rlEditNotice.setOnClickListener{
                updateItem(noticeModel)
            }

            viewHolder.binding!!.rlForground.setOnClickListener{
                NoticeDetailActivity.open(mContext,noticeModel)
            }
        }

    }

    override fun getItemCount(): Int {
        return noticeList!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: RowNoticeBinding? = null
        var expiredBinding: RowExpirednoticeBinding? = null

        init {
            if(expired){
                expiredBinding = RowExpirednoticeBinding.bind(itemView)
            }else{
                binding = RowNoticeBinding.bind(itemView)
            }

        }
    }

    fun removeItem(position: Int) {
        deleteNotice(noticeList?.get(position)!!.id,position)

    }

    private fun updateItem(model : NoticeBoardModel){
        CreateNoticeActivity.open(mContext,model)
    }

    private fun deleteNotice(id : Int,position: Int){
        noticeBoardViewModel?.let {
            if (mContext.let { ctx -> AndroidUtil.isInternetAvailable(ctx) }) {
                it.deleteNotice(id)
                noticeList?.removeAt(position)
                notifyDataSetChanged()

            }
        }

        noticeBoardViewModel?.getErrorMutableLiveData()?.observe(mContext as LifecycleOwner, Observer {
            it?.let {
                AndroidUtil.showToast(mContext, it.message,true)
            }
        })

        noticeBoardViewModel?.getSuccessLiveData()?.observe(mContext as LifecycleOwner, Observer {
            if(noticeList?.size == 0){
                if(fragment is ActiveNoticeFragment){
                    (fragment as ActiveNoticeFragment).showEmptyBox()
                }else if(fragment is ExpiredNotice){
                    (fragment as ExpiredNotice).showEmptyBox()
                }

            }
            AndroidUtil.showToast(mContext, "Notice deleted successfully",false)

        })
    }
}
