package com.akshar.one.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.databinding.SectionRowBinding
import com.akshar.one.view.examschedule.ScheduleExamActivity
import com.akshar.one.view.examschedule.ScheduledExamList
import com.akshar.one.model.ExaminationDropDownModel
import com.akshar.one.model.TestListModel
import com.akshar.one.view.marksentry.inputselection.SelectOtherInputsActivity
import com.akshar.one.view.messagecenter.SendMarksAndFeeReport

import java.util.ArrayList

class TestAdapter(private val mContext: Activity, var parent: Int,
                     var child:Int, private val list: ArrayList<TestListModel>?,private var openTest : Boolean,
                  private var fragment: Fragment?,
                     private var model: ExaminationDropDownModel, val callback: ExaminationDropDownAdapter.SectionSelection
) :
    RecyclerView.Adapter<TestAdapter.ViewHolder>() {





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.section_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val test = list?.get(position)
        holder.binding.tvClassName.text = test!!.testName
        if(child == position){
            holder.binding.imgCheck.visibility = View.VISIBLE
            holder.binding.tvClassName.setTextColor(mContext.resources.getColor(R.color.bluee))
        }else{
            holder.binding.imgCheck.visibility = View.GONE
            holder.binding.tvClassName.setTextColor(mContext.resources.getColor(R.color.black))
        }
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: SectionRowBinding = SectionRowBinding.bind(itemView)

        init {
            itemView.setOnClickListener {
                callback.selectionCallback(parent,adapterPosition)
                if(openTest){
                    if(mContext is ScheduleExamActivity){
                        (mContext).selectTest(list?.get(adapterPosition)!!,model)

                    }else if(mContext is ScheduledExamList){
                        (mContext).selectTest(list?.get(adapterPosition)!!,model)
                    } else if(mContext is SelectOtherInputsActivity){
                        (mContext).selectTest(list?.get(adapterPosition)!!,model)
                    }else if(mContext is SendMarksAndFeeReport){
                        (mContext).selectTest(list?.get(adapterPosition)!!,model)
                    }

                }

            }

        }

    }
}
