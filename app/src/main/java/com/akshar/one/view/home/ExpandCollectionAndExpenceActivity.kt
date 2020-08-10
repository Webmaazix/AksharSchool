package com.akshar.one.view.home

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.akshar.one.R
import com.akshar.one.model.NoticeBoardModel
import com.akshar.one.util.AndroidUtil
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.noticeboard.NoticeBoardViewModel
import kotlinx.android.synthetic.main.activity_create_notice.*
import kotlinx.android.synthetic.main.main_toolbar.view.*
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Handler
import java.util.*
import java.text.SimpleDateFormat
import android.widget.CompoundButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.akshar.one.adapter.ClassDropDownAdapter
import com.akshar.one.databinding.*
import com.akshar.one.manager.SessionManager
import com.akshar.one.model.ClassDropDownModel
import com.akshar.one.model.FeePayment
import com.akshar.one.model.SectionList
import com.akshar.one.util.AppUtil
import com.akshar.one.view.home.adapter.CollectionAdapter
import com.akshar.one.view.home.adapter.ExpencesAdapter
import com.akshar.one.viewmodels.timetable.TimeTableViewModel
import java.text.DecimalFormat
import kotlin.collections.ArrayList


class ExpandCollectionAndExpenceActivity : AppCompatActivity(), View.OnClickListener {

    private var currActivity : Activity = this
    private var noticeBoardModel = NoticeBoardModel()
    private var binding : ActivityExpandCollectionAndExpenceBinding? = null
    private var model = ArrayList<FeePayment>()
    private lateinit var expencesAdapter : ExpencesAdapter
    private var collectionList = ArrayList<FeePayment>()
    private var expenceList = ArrayList<FeePayment>()
    var collectionAmountFloatArray = ArrayList<Float>()
    var collectionColorArray = ArrayList<Int>()
    var colorsArray = Array<Int>(12){0}


    companion object{
        fun open(currActivity : Activity,model : ArrayList<FeePayment>){
            val intent = Intent(currActivity, ExpandCollectionAndExpenceActivity::class.java)
            intent.putExtra("model",model)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            currActivity.startActivity(intent)
            currActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(currActivity,R.layout.activity_expand_collection_and_expence)
        model = intent.getSerializableExtra("model") as ArrayList<FeePayment>
        colorsArray = arrayOf(R.color.light_blue,R.color.green_normal,
            R.color.light_yellow,R.color.dark_yellow,
            R.color.orange,R.color.science_blue,R.color.maroon,
            R.color.hindi_orange,R.color.english_pink,R.color.bio_purple,
            R.color.physics_pink,
            R.color.socialstudy_purple,R.color.civics_green,
            R.color.chemistry_blue,R.color.economics_purple)
        setCollectionAdapter()
        setData()

        initViews()
    }

    private fun setData(){

            binding!!.toolbar.txtToolbarTitle.text = getString(R.string.notice_detail)
            binding!!.toolbar.imgMenu.visibility = View.GONE
            binding!!.toolbar.imgBack.visibility = View.VISIBLE
          val formatter = DecimalFormat("#,###,###")
                expenceList.clear()
                collectionAmountFloatArray.clear()
                collectionColorArray.clear()
                expenceList.addAll(model)
                expencesAdapter.notifyDataSetChanged()
                var totalAmount = 0.0


                for(i in 0 until model.size){
                    collectionAmountFloatArray.add(model[i].value.toFloat())
                    collectionColorArray.add(colorsArray[i])

                    if(totalAmount == 0.0){
                        totalAmount = model[i].value.toDouble()
                    }else {
                        totalAmount = totalAmount+model[i].value.toDouble()
                    }
                }

                Handler().postDelayed({

                    val totalString = formatter.format(totalAmount)
                    val tottalExpence = "₹ $totalString"
                    binding?.tvTotalFinanceAmountCollection!!.text = tottalExpence

                    binding?.pieChartCollection!!.setDataPoints(collectionAmountFloatArray.toFloatArray())
                    binding?.pieChartCollection!!.setCenterColor(R.color.white)
                    binding?.pieChartCollection!!.setSliceColor(collectionColorArray.toIntArray())
                },3000)
            }

    private fun initViews(){
        setListner()

    }

    private fun setListner(){
        binding!!.toolbar.imgBack.setOnClickListener(this)


    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.imgBack ->{
                onBackPressed()
            }

        }
    }

    private fun setCollectionAdapter(){
        binding!!.rvCollection.setHasFixedSize(true)
        binding!!.rvCollection.layoutManager = LinearLayoutManager(currActivity,
            LinearLayoutManager.VERTICAL,false)
        expencesAdapter = ExpencesAdapter(currActivity,collectionList)
        binding!!.rvCollection.adapter = expencesAdapter

    }


}
