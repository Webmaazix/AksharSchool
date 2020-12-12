package com.akshar.one.view.messagecenter.adapter

import com.akshar.one.adapter.SectionAdapter


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
import com.akshar.one.model.ClassDropDownModel
import com.akshar.one.model.FeeHeadTermList
import com.akshar.one.model.SectionList

import java.util.ArrayList




class FeeHeadListAdapter(private val mContext: Activity, private val list: ArrayList<FeeHeadTermList>?,
                                    private var fragment : Fragment?) :
    RecyclerView.Adapter<FeeHeadListAdapter.ViewHolder>() {

    companion object{
        var selectedChild = -1
        var clickParent=-1;
    }


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

        val classModel = list?.get(position)
        holder.binding.tvClassName.text = classModel!!.headName
        holder.binding.rlSections.setHasFixedSize(true)
        val value=if(position==clickParent){
            selectedChild
        }else{
            -1
        }
        holder.binding.rlSections.layoutManager = LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false)
        if(fragment!= null){
            val adapter = FeeTermListAdapter(mContext,position,value,classModel.termsList ,fragment,classModel)

            holder.binding.rlSections.adapter = adapter
        }else{
            val adapter = FeeTermListAdapter(mContext,position,value,classModel.termsList ,null,classModel)
            holder.binding.rlSections.adapter = adapter

        }

        holder.binding.rlSections.measure(View.MeasureSpec.makeMeasureSpec(holder.binding.rlSections.width, View.MeasureSpec.EXACTLY), View.MeasureSpec.UNSPECIFIED)
        val height = holder.binding.rlSections.measuredHeight

        holder.binding.imgLine.layoutParams.height = height
        holder.binding.imgLine.requestLayout()
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
