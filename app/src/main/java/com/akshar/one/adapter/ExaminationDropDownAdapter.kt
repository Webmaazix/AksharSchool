package com.akshar.one.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.databinding.ClassRowBinding
import com.akshar.one.view.examschedule.ScheduleExamActivity
import com.akshar.one.view.examschedule.ScheduledExamList
import com.akshar.one.model.ExaminationDropDownModel

import java.util.ArrayList




class ExaminationDropDownAdapter(private val mContext: Activity, private val list: ArrayList<ExaminationDropDownModel>?,
                                 private var openTest : Boolean,
                                 private var fragment : Fragment?
                           ,private var callback : SectionSelection) :
    RecyclerView.Adapter<ExaminationDropDownAdapter.ViewHolder>() {

    var selectedChild = -1
    var clickParent=-1;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            LayoutInflater.from(mContext as Context).inflate(
                R.layout.class_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val examModel = list?.get(position)
        holder.binding.tvClassName.text = examModel!!.examName
        holder.binding.rlSections.setHasFixedSize(true)
        val value=if(position==clickParent){
            selectedChild
        }else{
            -1
        }

        if(openTest){
            holder.binding.rlSections.visibility = View.VISIBLE
            holder.binding.rlEmptyView.visibility = View.GONE
        }else{
            holder.binding.rlSections.visibility = View.GONE
            holder.binding.rlEmptyView.visibility = View.VISIBLE
        }

        holder.binding.rlSections.layoutManager = LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false)
        if(fragment!= null){
            if(openTest){
                val adapter = TestAdapter(mContext,position,value,examModel.testsList,openTest,
                    fragment,examModel,object :SectionSelection{
                        override fun selectionCallback(parent: Int, child: Int) {
                            selectedChild=child;
                            clickParent=parent;
                            callback.selectionCallback(parent,child)
                        }
                    })

                holder.binding.rlSections.adapter = adapter
            }

        }else{
            if(openTest){
                val adapter = TestAdapter(mContext,position,value,examModel.testsList ,openTest,null,examModel,object :SectionSelection{
                    override fun selectionCallback(parent: Int, child: Int) {
                        selectedChild=child;
                        clickParent=parent;
                        callback.selectionCallback(parent,child)
                    }
                })
                holder.binding.rlSections.adapter = adapter
            }


        }

        holder.binding.rlSections.measure(View.MeasureSpec.makeMeasureSpec(holder.binding.rlSections.width, View.MeasureSpec.EXACTLY), View.MeasureSpec.UNSPECIFIED)
        val height = holder.binding.rlSections.measuredHeight

        holder.binding.imgLine.layoutParams.height = height
        holder.binding.imgLine.requestLayout()


        holder.binding.tvClassName.setOnClickListener{
            if(!openTest){
                if(mContext is ScheduleExamActivity){
                    (mContext).examSelectd(examModel)
                }

            }else if(mContext is ScheduledExamList){
                (mContext).examSelectd(examModel)
            }
        }


    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: ClassRowBinding = ClassRowBinding.bind(itemView)

    }

    interface SectionSelection{
        public fun selectionCallback(parent:Int,child:Int)
    }
}
