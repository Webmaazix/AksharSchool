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
import com.akshar.one.model.ClassDropDownModel
import com.akshar.one.model.SectionList

import java.util.ArrayList




class ClassDropDownAdapter(private val mContext: Activity, private val list: ArrayList<ClassDropDownModel>?,private var fragment : Fragment?
                           ,private var callback : SectionSelection) :
    RecyclerView.Adapter<ClassDropDownAdapter.ViewHolder>() {

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
        holder.binding.tvClassName.text = classModel!!.courseName
        holder.binding.rlSections.setHasFixedSize(true)
        val value=if(position==clickParent){
            selectedChild
        }else{
            -1
        }
        holder.binding.rlSections.layoutManager = LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false)
        if(fragment!= null){
            val adapter = SectionAdapter(mContext,position,value,classModel.classroomsList as ArrayList<SectionList>,fragment,classModel,object :SectionSelection{
                override fun selectionCallback(parent: Int, child: Int) {
                    selectedChild=child;
                    clickParent=parent;
                    callback.selectionCallback(parent,child)
                }
            })

            holder.binding.rlSections.adapter = adapter
        }else{
            val adapter = SectionAdapter(mContext,position,value,classModel.classroomsList as ArrayList<SectionList>,null,classModel,object :SectionSelection{
                override fun selectionCallback(parent: Int, child: Int) {
                    selectedChild=child;
                    clickParent=parent;
                    callback.selectionCallback(parent,child)
                }
            })
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
