package com.akshar.one.view.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akshar.one.R
import com.akshar.one.databinding.BirthdayCellBinding
import com.akshar.one.databinding.CollectionCellBinding
import com.akshar.one.model.BirthDayModel
import com.akshar.one.model.FeePayment
import com.akshar.one.util.AppUtils
import com.akshar.one.view.activity.MainActivity
import java.text.DecimalFormat

import java.util.ArrayList

class ExpencesAdapter(private val mContext: Context, private val list: ArrayList<FeePayment>) :
    RecyclerView.Adapter<ExpencesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.collection_cell,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val formatter = DecimalFormat("#,###,###")
        val birthDayModel = list.get(position)
        val admissionString = formatter.format(birthDayModel.value.toDouble())
        val admissionFee = "₹ $admissionString"

        val total = birthDayModel.value.toDouble()
        val totalString = formatter.format(total)

        val tottalExpence = "₹ $totalString"
        holder.binding.tvDueCollection!!.text = birthDayModel.key
        holder.binding.tvDueAmountCollection!!.text = admissionFee

        var colorRes = 0
        when (position % 12) {
            0 -> colorRes = R.color.light_blue
            1 -> colorRes = R.color.green_normal
            2 -> colorRes = R.color.light_yellow
            3 -> colorRes = R.color.dark_yellow
            4 -> colorRes = R.color.orange
            5 -> colorRes = R.color.science_blue
            6 -> colorRes = R.color.maroon
            7 -> colorRes = R.color.hindi_orange
            8 -> colorRes = R.color.english_pink
            9 -> colorRes = R.color.bio_purple
            10 -> colorRes = R.color.physics_pink
            11 -> colorRes = R.color.socialstudy_purple
        }
        holder.binding.tvYellowColorCollection!!.backgroundTintList = mContext.resources.getColorStateList(colorRes)



    }

    override fun getItemCount(): Int {
        if(mContext is MainActivity){
            if(list.size > 3){
                return 3
            }else{
                return list.size
            }
        }else{
            return list.size
        }

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: CollectionCellBinding = CollectionCellBinding.bind(itemView)

    }
}
