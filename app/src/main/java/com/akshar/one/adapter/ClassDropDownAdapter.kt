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




class ClassDropDownAdapter(private val mContext: Activity, private val list: ArrayList<ClassDropDownModel>?,private var fragment : Fragment
                           ,private var callback : SectionSelection) :
    RecyclerView.Adapter<ClassDropDownAdapter.ViewHolder>() {
//    lateinit var adapter : SectionAdapter

    companion object{
        var selectedChild = -1
        var clickParent=-1;
    }

//    init {
//         selectedChild = -1
//         clickParent=-1
//    }

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
        val adapter = SectionAdapter(mContext,position,value,classModel.classroomsList as ArrayList<SectionList>,fragment,classModel,object :SectionSelection{
            override fun selectionCallback(parent: Int, child: Int) {
                selectedChild=child;
                clickParent=parent;
                callback.selectionCallback(parent,child)
            }
        })
        holder.binding.rlSections.adapter = adapter

//        holder.binding.rlSections.addOnItemTouchListener(
//            RecyclerItemClickListener(
//                mContext,
//                holder.binding.rlSections,
//                object : RecyclerItemClickListener.OnItemClickListener {
//                    override fun onLongItemClick(view: View?, position: Int) {
//
//                    }
//
//                    override fun onItemClick(view: View, position: Int) {
//                        selected = position
//                        if(fragment is ClassTimeTableFragment){
//                            (fragment as ClassTimeTableFragment).sectionClicked(classModel ,classModel.classroomsList[position])
//                        }else if(fragment is StudentListFragment){
//                            (fragment as StudentListFragment).sectionClicked(classModel ,classModel.classroomsList[position])
//                        }
//                        adapter.notifyDataSetChanged();
//                    }
//                })
//        )

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
