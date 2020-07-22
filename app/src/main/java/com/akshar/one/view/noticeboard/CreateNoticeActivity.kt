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
import java.util.*
import java.text.SimpleDateFormat
import android.widget.CompoundButton
import com.akshar.one.adapter.ClassDropDownAdapter
import com.akshar.one.databinding.DialogSelectClassAndSectionBinding
import com.akshar.one.manager.SessionManager
import com.akshar.one.model.ClassDropDownModel
import com.akshar.one.model.SectionList
import com.akshar.one.viewmodels.timetable.TimeTableViewModel


class CreateNoticeActivity : AppCompatActivity(), View.OnClickListener {

    private var currActivity : Activity = this
    private var noticeBoardModel = NoticeBoardModel()
    private var binding : ActivityCreateNoticeBinding? = null
    private var noticeBoardViewModel: NoticeBoardViewModel? = null
    private var  myCalendar : Calendar? = null
    private var endDate : DatePickerDialog.OnDateSetListener? =null
    private var startDate : DatePickerDialog.OnDateSetListener? =null
    private var visibility = "All"
    private var dialogSelectClassSectionBinding: DialogSelectClassAndSectionBinding? = null
    private var dialog: Dialog? = null
    lateinit var classDropDownAdapter: ClassDropDownAdapter
    private var classDropdownList = ArrayList<ClassDropDownModel>()
    private var timeTableViewModel: TimeTableViewModel? = null
    private lateinit var classDropDownModel: ClassDropDownModel
    private lateinit var sectionModel: SectionList
    private var classRoomId: Int = 0;
    private var noticeModel : NoticeBoardModel? = null


    companion object{
        fun open(currActivity : Activity,model : NoticeBoardModel?){
            val intent = Intent(currActivity, CreateNoticeActivity::class.java)
            intent.putExtra("model",model)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            currActivity.startActivity(intent)
            currActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(currActivity,R.layout.activity_create_notice)
        noticeModel = intent.getSerializableExtra("model") as NoticeBoardModel?
        setData()
        myCalendar = Calendar.getInstance();
        initViews()
    }

    private fun setData(){
        if(noticeModel!= null){
            binding!!.etTitle.setText(noticeModel?.title)
            binding!!.etDesc.setText(noticeModel?.description)
            binding!!.tvStartDate.text = noticeModel?.startDate
            binding!!.tvEndDate.setText(noticeModel?.endDate)

            if(noticeModel?.visibility.equals("All")){
                binding!!.cbParents.isChecked = true
                binding!!.cbEmployees.isChecked = true
            }else if(noticeModel?.visibility.equals("Parent")){
                binding!!.cbParents.isChecked = true
                binding!!.cbEmployees.isChecked = false
            }else if(noticeModel?.visibility.equals("Employee")){
                binding!!.cbParents.isChecked = false
                binding!!.cbEmployees.isChecked = true
            }
        }
    }

    private fun initViews(){
        currActivity.application?.let {
            noticeBoardViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(NoticeBoardViewModel::class.java)
        }

        currActivity.application?.let {
            timeTableViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(TimeTableViewModel::class.java)
        }

        timeTableViewModel?.let {
            if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx) }) {
                it.getClassRoomDropdown()
            }
        }

        observers()
        setListner()
        binding!!.toolbar.txtToolbarTitle.text = getString(R.string.create_notice)
    }

    private fun setListner(){
        binding!!.toolbar.imgBack.setOnClickListener(this)
        binding!!.tvSend.setOnClickListener(this)
        startDate = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                myCalendar!!.set(Calendar.YEAR, year)
                myCalendar!!.set(Calendar.MONTH, monthOfYear)
                myCalendar!!.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateLabel()
            }
        endDate = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                myCalendar!!.set(Calendar.YEAR, year)
                myCalendar!!.set(Calendar.MONTH, monthOfYear)
                myCalendar!!.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateEndDate()
            }
        binding!!.tvStartDate.setOnClickListener(this)
        binding!!.tvEndDate.setOnClickListener(this)

        binding!!.cbParents.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                visibility = "Parent"
            }
        })
        binding!!.cbEmployees.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                visibility = "Employee"
            }
        })
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.imgBack ->{
                onBackPressed()
            }
            R.id.tvSend ->{
                if(validation()){
                    if(noticeModel!= null){
                        updateNotice(noticeModel?.id!!)
                    }else{
                        createNotice()
                    }

                }
            }
            R.id.tvStartDate ->{
                DatePickerDialog(
                    this@CreateNoticeActivity, startDate,
                    myCalendar!!.get(Calendar.YEAR), myCalendar!!.get(Calendar.MONTH),
                    myCalendar!!.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
            R.id.tvEndDate ->{
                DatePickerDialog(
                    this@CreateNoticeActivity, endDate, myCalendar
                        !!.get(Calendar.YEAR), myCalendar!!.get(Calendar.MONTH),
                    myCalendar!!.get(Calendar.DAY_OF_MONTH)
                ).show()

            }

        }
    }

    private fun updateLabel() {
        val myFormat = "yyyy-MM-dd" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)

        tvStartDate.text = sdf.format(myCalendar!!.getTime())
    }
    private fun updateEndDate() {
        val myFormat = "yyyy-MM-dd" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)

        tvEndDate.text = sdf.format(myCalendar!!.getTime())
    }

    private fun validation() : Boolean{
        var isValid = true
        if(etTitle.text.toString().isEmpty()){
            isValid = false
            AndroidUtil.showToast(currActivity,"Title is required",true)
        }else if(etDesc.text.toString().isEmpty()){
            isValid = false
            AndroidUtil.showToast(currActivity,"Description is required",true)
        }else if(tvStartDate.text.toString().isEmpty() || tvStartDate.text.equals(currActivity.getString(R.string.start_date))){
            isValid = false
            AndroidUtil.showToast(currActivity,"Start date is required",true)
        }else if(tvEndDate.text.toString().isEmpty() || tvEndDate.text.equals(currActivity.getString(R.string.end_date))){
            isValid = false
            AndroidUtil.showToast(currActivity,"End date is required",true)
        }
        return  isValid
    }

    private fun updateNotice(id : Int){
        val loginModel = SessionManager.getLoginModel()
        noticeBoardModel.title = etTitle.text.toString()
        noticeBoardModel.description = etDesc.text.toString()
        noticeBoardModel.startDate = tvStartDate.text.toString()
        noticeBoardModel.endDate = tvEndDate.text.toString()
        loginModel.let {
            if(it?.appsList!= null){
                noticeBoardModel.schoolCd = loginModel?.appsList!![0].orgCodes!!

            }else{
                noticeBoardModel.schoolCd = ""
            }
        }
        if(binding!!.cbParents.isChecked && binding!!.cbEmployees.isChecked){
            visibility = "All"
        }
        noticeBoardModel.visibility = visibility//parent teacher all


        noticeBoardViewModel?.let {
            if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                it.updateNotice(id,noticeBoardModel) }
        }
        observers()
    }

    private fun createNotice(){
        val loginModel = SessionManager.getLoginModel()
        noticeBoardModel.title = etTitle.text.toString()
        noticeBoardModel.description = etDesc.text.toString()
        noticeBoardModel.startDate = tvStartDate.text.toString()
        noticeBoardModel.endDate = tvEndDate.text.toString()
        loginModel.let {
            if(it?.appsList!= null){
                noticeBoardModel.schoolCd = loginModel?.appsList!![0].orgCodes!!

            }else{
                noticeBoardModel.schoolCd = ""
            }
        }
        if(binding!!.cbParents.isChecked && binding!!.cbEmployees.isChecked){
            visibility = "All"
        }
        noticeBoardModel.visibility = visibility//parent teacher all


        noticeBoardViewModel?.let {
            if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                it.createNotice(noticeBoardModel) }
        }
        observers()
    }

    private fun observers(){
       noticeBoardViewModel?.getErrorMutableLiveData()?.observe(this,androidx.lifecycle.Observer {
           it?.let {
               AndroidUtil.showToast(currActivity, it.message,true)
           }
       })

        noticeBoardViewModel?.getSuccessLiveData()?.observe(this,androidx.lifecycle.Observer {
            if(noticeModel!= null){
                AndroidUtil.showToast(currActivity,"Notice Updated successfully",false)
            }else{
                AndroidUtil.showToast(currActivity,"Notice created successfully",false)
            }

            NoticeboardActivity.open(currActivity)
        })

    }


}
