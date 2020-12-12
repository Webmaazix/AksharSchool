package com.akshar.one.view.marksentry.adapter

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.databinding.BirthdayCellBinding
import com.akshar.one.databinding.RowStudentMarksEntryBinding
import com.akshar.one.databinding.SubjectCellBinding
import com.akshar.one.model.BirthDayModel
import com.akshar.one.model.StundentsMarksList
import com.akshar.one.model.SubjectListModel
import com.akshar.one.util.AppUtils
import com.akshar.one.view.activity.MainActivity
import com.akshar.one.view.marksentry.StudentMarksEntry
import com.akshar.one.view.marksentry.inputselection.SelectOtherInputsActivity

import java.util.ArrayList

class StudentMarksAdapter(private val mContext: Context, private val list: ArrayList<StundentsMarksList>,private var isUpdate : Boolean) :
    RecyclerView.Adapter<StudentMarksAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_student_marks_entry,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model  = list[position]
        holder.binding.tvStudentName.text = model.studentName

        if(isUpdate){
            holder.binding.tvMarks.text = model.marksScored
            holder.binding.tvMarks.visibility = View.VISIBLE
            holder.binding.etMarksEntry.visibility = View.GONE
        }else{
            holder.binding.tvMarks.visibility = View.GONE
            holder.binding.etMarksEntry.visibility = View.VISIBLE
        }

       // model.marksScored = holder.binding.etMarksEntry.text.toString()
        holder.binding.etMarksEntry.setText(model.marksScored)
        holder.binding.etMarksEntry.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                model.marksScored = p0!!.toString()
//                if(position == list.size-1){
//                    for (data in list){
//                        if(!data.marksScored.isNullOrEmpty()){
//                                (mContext as StudentMarksEntry).saveOrUpdateList(list,isUpdate)
//                        }else{
//                            AppUtils.showToast(mContext as Activity,"Please assign marks to all students",true)
//                        }
//                    }
//
//
//                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })

        var count = "00."
        if(position < 9){
            count = "0"+(position+1)+"."
            holder.binding.tvSerialNumber.text = count
        }else{
            count = (position+1).toString()+"."
            holder.binding.tvSerialNumber.text = count
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: RowStudentMarksEntryBinding = RowStudentMarksEntryBinding.bind(itemView)

    }
}
