package com.akshar.one.view.noticeboard

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.akshar.one.R
import com.akshar.one.databinding.ActivityCreateNoticeBinding
import com.akshar.one.model.NoticeBoardModel
import com.akshar.one.util.AndroidUtil
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.noticeboard.NoticeBoardViewModel
import kotlinx.android.synthetic.main.activity_create_notice.*
import kotlinx.android.synthetic.main.main_toolbar.view.*
import android.app.DatePickerDialog
import android.app.Dialog
import android.net.Uri
import android.provider.Settings
import java.util.*
import java.text.SimpleDateFormat
import android.widget.CompoundButton
import androidx.lifecycle.LifecycleOwner
import com.akshar.one.adapter.ClassDropDownAdapter
import com.akshar.one.databinding.ActivityNoticeDetailBinding
import com.akshar.one.databinding.ActivityNoticeboardBinding
import com.akshar.one.databinding.DialogSelectClassAndSectionBinding
import com.akshar.one.manager.SessionManager
import com.akshar.one.model.ClassDropDownModel
import com.akshar.one.model.SectionList
import com.akshar.one.util.AppUtil
import com.akshar.one.viewmodels.timetable.TimeTableViewModel


class NoticeDetailActivity : AppCompatActivity(), View.OnClickListener {

    private var currActivity : Activity = this
    private var noticeBoardModel = NoticeBoardModel()
    private var binding : ActivityNoticeDetailBinding? = null
    private var noticeModel : NoticeBoardModel? = null
    private var noticeBoardViewModel: NoticeBoardViewModel? = null


    companion object{
        fun open(currActivity : Activity,model : NoticeBoardModel?){
            val intent = Intent(currActivity, NoticeDetailActivity::class.java)
            intent.putExtra("model",model)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            currActivity.startActivity(intent)
            currActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(currActivity,R.layout.activity_notice_detail)
        noticeModel = intent.getSerializableExtra("model") as NoticeBoardModel?
        setData()
        initViews()
    }

    private fun setData(){
        if(noticeModel!= null){
            binding!!.toolbar.txtToolbarTitle.text = getString(R.string.notice_detail)
            binding!!.toolbar.imgMenu.visibility = View.GONE
            binding!!.toolbar.imgBack.visibility = View.VISIBLE
            binding!!.tvTopicName.setText(noticeModel?.title)
            binding!!.tvDesc.setText(noticeModel?.description)
            val startDate = AppUtil.formatDate(noticeModel?.startDate!!)
            val endtDate = AppUtil.formatDate(noticeModel?.endDate!!)
            val date = "$startDate $endtDate"
            binding!!.tvDate.text = date

        }
    }

    private fun initViews(){
        setListner()

    }

    private fun setListner(){
        binding!!.toolbar.imgBack.setOnClickListener(this)
        binding!!.rlEdit.setOnClickListener(this)
        binding!!.rlDelete.setOnClickListener(this)

    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.imgBack ->{
                onBackPressed()
            }
            R.id.rlEdit ->{
                updateItem(noticeModel!!)
            }
            R.id.rlDelete ->{
                removeItem()
            }

        }
    }

    fun removeItem() {
        deleteNotice(noticeBoardModel.id)

    }

    private fun updateItem(model : NoticeBoardModel){
        CreateNoticeActivity.open(currActivity,model)
    }

    private fun deleteNotice(id : Int){
        noticeBoardViewModel?.let {
            if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx) }) {
                it.deleteNotice(id)
            }
        }

        noticeBoardViewModel?.getErrorMutableLiveData()?.observe(currActivity as LifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                AndroidUtil.showToast(currActivity, it.message,true)
            }
        })

        noticeBoardViewModel?.getSuccessLiveData()?.observe(currActivity as LifecycleOwner, androidx.lifecycle.Observer {

            AndroidUtil.showToast(currActivity, "Notice deleted successfully",false)
            onBackPressed()

        })
    }

}
