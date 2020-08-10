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
import com.akshar.one.model.*

import java.util.ArrayList




class SkillSubCategoryAdapter(private val mContext: Activity, private val list: ArrayList<SubCategoryList>?,
                              private val category : SkillListModel) :
    RecyclerView.Adapter<SkillSubCategoryAdapter.ViewHolder>() {

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

        val model = list?.get(position)
        holder.binding.tvClassName.text = model!!.subCategoryName
        holder.binding.rlSections.setHasFixedSize(true)
        val value=if(position==clickParent){
            selectedChild
        }else{
            -1
        }
        holder.binding.rlSections.layoutManager = LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false)

        val adapter = SkillLiatAdapter(mContext,model.skillList as ArrayList<SkillSetModel>,model,category)
        holder.binding.rlSections.adapter = adapter



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

}
