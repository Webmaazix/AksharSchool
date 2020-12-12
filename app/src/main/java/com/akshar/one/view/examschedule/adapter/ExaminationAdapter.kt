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
import com.akshar.one.util.AndroidUtil
import com.akshar.one.util.AppUtil
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.examination.ExamViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ExaminationAdapter(private val mContext: Activity, private val list: ArrayList<ScheduleList>?,var securityList : ArrayList<String>) :
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

        if(securityList.contains("M_EXAM_SCHEDULE_EDIT") && securityList.contains("M_EXAM_SCHEDULE_DELETE")){
            viewHolder.binding.rlBackground.visibility = View.VISIBLE
        }else if(securityList.contains("M_EXAM_SCHEDULE_DELETE")){
            viewHolder.binding.rlEditNotice.visibility = View.GONE
            viewHolder.binding.rlDelete.visibility = View.VISIBLE

        }else if(securityList.contains("M_EXAM_SCHEDULE_EDIT")){
            viewHolder.binding.rlEditNotice.visibility = View.VISIBLE
            viewHolder.binding.rlDelete.visibility = View.GONE
        }else{
            viewHolder.binding.rlBackground.visibility = View.GONE
        }

        viewHolder.binding.rlDelete.setOnClickListener{
            deleteItem(position)
        }
        viewHolder.binding.rlEditNotice.setOnClickListener{
            updateItem(model!!)
        }
        var name = ""
        if(model?.subjectName != null) {
            name = model.subjectName
        }else{
            name = "N/A"
        }
        viewHolder.binding.tvSubjectName.text = name
        viewHolder.binding.tvClassName.text = model?.duration.toString()+" "+"mins."

        var startDate = ""
        var endDate = ""
        if(model?.startTime != null){
          startDate =   AppUtil.parseTimeExam(model.startTime)
        }
        if(model?.endTime != null){
            endDate  = AppUtil.parseTimeExam(model.endTime)
        }

        val time = "$startDate"
        viewHolder.binding.tvStartTime.text = time
        viewHolder.binding.tvDate.text = AppUtil.formatBirthdayDate(model!!.date)
        //  holder.binding.tvClassName.text = model.schedule.get(0).subjectName

        if (model.subjectName != null) {
            if (model.subjectName.contains("Telugu", true)) {
                viewHolder.binding.llNotification.backgroundTintList = mContext.resources.getColorStateList(R.color.timetable_telugu)
                viewHolder.binding.imgNotification.setImageResource(R.drawable.timetable_telugu)
            } else if (model.subjectName.contains("Hindi", true)) {
                viewHolder.binding.llNotification.backgroundTintList = mContext.resources.getColorStateList(R.color.timetable_hindi)
                viewHolder.binding.imgNotification.setImageResource(R.drawable.timetable_hindi)
            } else if (model.subjectName.contains("English", true)) {
                viewHolder.binding.llNotification.backgroundTintList = mContext.resources.getColorStateList(R.color.timetable_english)
                viewHolder.binding.imgNotification.setImageResource(R.drawable.timetable_english)
            } else if (model.subjectName.contains("Mathematics", true)) {
                viewHolder.binding.llNotification.backgroundTintList = mContext.resources.getColorStateList(R.color.timetable_math)
                viewHolder.binding.imgNotification.setImageResource(R.drawable.timetable_math)
            } else if (model.subjectName.contains("Science", true)) {
                viewHolder.binding.llNotification.backgroundTintList = mContext.resources.getColorStateList(R.color.timetable_science)
                viewHolder.binding.imgNotification.setImageResource(R.drawable.timetable_science)
            } else if (model.subjectName.contains("Environmental Science", true)) {
                viewHolder.binding.llNotification.backgroundTintList = mContext.resources.getColorStateList(R.color.timetable_environmental)
                viewHolder.binding.imgNotification.setImageResource(R.drawable.timetable_environmental_study)
            } else if (model.subjectName.contains("Biology", true)) {
                viewHolder.binding.llNotification.backgroundTintList = mContext.resources.getColorStateList(R.color.timetable_bio)
                viewHolder.binding.imgNotification.setImageResource(R.drawable.timetable_bio)
            } else if (model.subjectName.contains("Physics", true)) {
                viewHolder.binding.llNotification.backgroundTintList = mContext.resources.getColorStateList(R.color.timetable_physics)
                viewHolder.binding.imgNotification.setImageResource(R.drawable.timetable_physics)
            } else if (model.subjectName.contains("Social Studies", true)) {
                viewHolder.binding.llNotification.backgroundTintList = mContext.resources.getColorStateList(R.color.timetable_socialstudy)
                viewHolder.binding.imgNotification.setImageResource(R.drawable.timetable_social_study)
            } else if (model.subjectName.contains("Civics", true)) {
                viewHolder.binding.llNotification.backgroundTintList = mContext.resources.getColorStateList(R.color.timetable_civics)
                viewHolder.binding.imgNotification.setImageResource(R.drawable.timetable_civics)
            } else if (model.subjectName.contains("Commerce", true)) {
                viewHolder.binding.llNotification.backgroundTintList = mContext.resources.getColorStateList(R.color.timetable_commerce)
                viewHolder.binding.imgNotification.setImageResource(R.drawable.timetable_commerce)
            } else if (model.subjectName.contains("Economics", true)) {
                viewHolder.binding.llNotification.backgroundTintList = mContext.resources.getColorStateList(R.color.timetable_economics)
                viewHolder.binding.imgNotification.setImageResource(R.drawable.timetable_economics)
            } else if (model.subjectName.contains("Chemistry", true)) {
                viewHolder.binding.llNotification.backgroundTintList = mContext.resources.getColorStateList(R.color.timetable_chemistry)
                viewHolder.binding.imgNotification.setImageResource(R.drawable.timetable_chemistry)
            } else {
                viewHolder.binding.llNotification.backgroundTintList = mContext.resources.getColorStateList(R.color.timetable_other)
                viewHolder.binding.imgNotification.setImageResource(R.drawable.timetableother)
            }

        }

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
