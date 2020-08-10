package com.akshar.one.view.examschedule.adapter

import android.app.Activity
import android.app.Application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.databinding.*
import com.akshar.one.view.examschedule.ScheduleExamActivity
import com.akshar.one.model.*
import com.akshar.one.swipelayout.SimpleSwipeListener
import com.akshar.one.swipelayout.SwipeLayout
import com.akshar.one.swipelayout.adapters.RecyclerSwipeAdapter
import com.akshar.one.util.AndroidUtil
import com.akshar.one.util.AppUtil
import com.akshar.one.util.AppUtils
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.examination.ExamViewModel
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import kotlinx.android.synthetic.main.row_notice.view.*
import kotlinx.android.synthetic.main.row_scheduled_exam.view.*

class ExaminationAdapter(private val mContext: Activity, private val list: ArrayList<ScheduleList>?) :
    RecyclerView.Adapter<ExaminationAdapter.ViewHolder>() {

    var classDropDownModel: ClassDropDownModel? = null
     var sectionModel: SectionList? =null
    var testModel : TestListModel? =null
    var examDropDownModel: ExaminationDropDownModel? = null


    private var examViewModel : ExamViewModel? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        examViewModel = ViewModelProvider(
            ViewModelStore(),
            ViewModelFactory(mContext.applicationContext as Application)
        ).get(ExamViewModel::class.java)

        return ViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_scheduled_exam,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val model = list?.get(position)
//        viewHolder.binding.swipe.setShowMode(SwipeLayout.ShowMode.LayDown)
//        viewHolder.binding.swipe.addSwipeListener(object : SimpleSwipeListener() {
//            override fun onOpen(layout: SwipeLayout) {
//                YoYo.with(Techniques.Tada).duration(500).delay(100)
//                    .playOn(layout.findViewById(R.id.imgDelete))
//            }
//        })

        viewHolder.binding.rlDelete.setOnClickListener{
            deleteItem(position)
        }
        viewHolder.binding.rlEdit.setOnClickListener{
            updateItem(model!!)
        }
        var name = ""
        if(model?.subjectName != null) {
            name = model.subjectName
        }else{
            name = "N/A"
        }
        viewHolder.binding.tvExamName.text = name

        var startDate = ""
        var endDate = ""
        if(model?.endTime != null){
          startDate =   AppUtil.formatDate(model.startTime)
        }
        if(model?.endTime != null){
            endDate  = AppUtil.formatDate(model.endTime)
        }

        val time = "$startDate - $endDate"
        viewHolder.binding.tvTime.text = time
        viewHolder.binding.tvDate.text = model?.date
        //  holder.binding.tvClassName.text = model.schedule.get(0).subjectName
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: RowScheduledExamBinding = RowScheduledExamBinding.bind(itemView)

    }

    private fun updateItem(model : ScheduleList){
        ScheduleExamActivity.open(mContext,model,classDropDownModel,sectionModel,examDropDownModel,testModel)
    }

    private fun deleteItem(position: Int){

        examViewModel?.let {
            if (mContext.let { ctx -> AndroidUtil.isInternetAvailable(ctx) }) {
                list?.get(position)?.id?.let { it1 ->
                    it.deleteExamSchedule(
                        it1
                    )
                }

            }
        }

        examViewModel?.getErrorMutableLiveData()?.observe(mContext as LifecycleOwner, Observer {
            it?.let {
                AndroidUtil.showToast(mContext, it.message,true)
            }
        })

        examViewModel?.getSuccessLiveData()?.observe(mContext as LifecycleOwner, Observer {
            list?.removeAt(position)
            notifyDataSetChanged()
            AndroidUtil.showToast(mContext, "Examination Schedule deleted successfully",true)
        })

    }
}
