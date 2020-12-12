package com.akshar.one.view.marksentry

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.akshar.one.R
import com.akshar.one.adapter.ExaminationDropDownAdapter
import com.akshar.one.app.AksharSchoolApplication
import com.akshar.one.app.AksharSchoolApplication.Companion.context
import com.akshar.one.databinding.ActivityParentMarksBinding
import com.akshar.one.databinding.DialogSelectClassAndSectionBinding
import com.akshar.one.manager.SessionManager
import com.akshar.one.model.*
import com.akshar.one.util.AndroidUtil
import com.akshar.one.util.AppUtils
import com.akshar.one.view.login.adapter.StudentAdapter
import com.akshar.one.view.marksentry.adapter.ParentMarksCategoryAdapter
import com.akshar.one.view.marksentry.adapter.SubjectMarksParentAdapter
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.examination.ExamViewModel
import com.akshar.one.viewmodels.marksentry.MarksEntryViewModel
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.enums.Anchor
import com.anychart.enums.HoverMode
import com.anychart.enums.Position
import com.anychart.enums.TooltipPositionMode
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.main_toolbar.view.*
import java.util.*
import kotlin.collections.ArrayList

class ParentMarksActivity : AppCompatActivity(),View.OnClickListener {

    private var binding : ActivityParentMarksBinding? = null
    private var currActivity : Activity = this
    private lateinit var testHeadingAdapter : SubjectMarksParentAdapter
    private lateinit var categoryAdapter : ParentMarksCategoryAdapter
    private var testHeadingList = ArrayList<MarksTestListParent>()
    private var scholasticMarksList = ArrayList<ScholasticMarksList>()
    private var subjectList = ArrayList<SubjectListParent>()
    private var marksViewModel: MarksEntryViewModel? = null
    private var currentTest = MarksTestListParent()
    var Tid = 0

    private var examViewModel: ExamViewModel? = null
    lateinit var examDropDownAdapter: ExaminationDropDownAdapter
    private var examDropDownList = ArrayList<ExaminationDropDownModel>()
    private var examId: Int = 0;
    private var studentProfileId: Int = 0;
    private var dialog : Dialog? = null
    private  var examDropDownModel: ExaminationDropDownModel? = null
    private var dialogSelectClassSectionBinding: DialogSelectClassAndSectionBinding? = null
    private var selectedRole : AppList? = null

    private lateinit var studentAdapter : StudentAdapter
    private var list = ArrayList<AppList>()
    private var schoolList = ArrayList<AppList>()
    private var studentList = ArrayList<AppList>()
    private var  marksGraphModel= ArrayList<MarksGraphModel>()



    companion object{
        fun open(currActivity : Activity){
            val intent = Intent(currActivity, ParentMarksActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            currActivity.startActivity(intent)
            currActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(currActivity,R.layout.activity_parent_marks)
        studentProfileId = SessionManager.getLoginRole()!!.userUniqueId
        selectedRole = SessionManager.getLoginRole()
        list = SessionManager.getLoginModel()!!.appsList as ArrayList<AppList>
        selectedRole = SessionManager.getLoginRole()
        for(model in list){
            if(model.appName.equals("Spectrum")){
                schoolList.add(model)
            }else if(model.appName.equals("SmartParent")){
                studentList.add(model)
            }
        }
        setAdapter()
        initViews()
        setListner()
        setStudentData()
    }

    private fun setListner(){
        binding!!.toolbar.imgBack.setOnClickListener(this)
        binding!!.rlExaminationSelection.setOnClickListener(this)
        binding!!.imgArrow.setOnClickListener(this)
        binding!!.imgUpArrow.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.imgBack ->{
               onBackPressed()
              //  StudentSubjectGraphActivity.open(currActivity,marksGraphModel[0].subjectMarksGraphDTO)
            }
            R.id.rlExaminationSelection ->{
                openExamDialog()
            }

            R.id.imgArrow ->{
                binding!!.imgUpArrow.visibility = View.VISIBLE
                binding!!.imgArrow.visibility = View.GONE

                binding!!.rvStudentList.visibility = View.VISIBLE
            }
            R.id.imgUpArrow ->{
                binding!!.imgUpArrow.visibility = View.GONE
                binding!!.imgArrow.visibility = View.VISIBLE

                binding!!.rvStudentList.visibility = View.GONE
            }

        }
    }

    private fun setStudentData(){
        binding!!.tvStudentName.text = selectedRole!!.studentName
        binding!!.tvClassSectionName.text = selectedRole!!.className

        if(selectedRole!!.url!= null && selectedRole!!.url!=""){
            binding!!.flLayout.visibility = View.GONE
            binding!!.imgUserProfile.visibility = View.VISIBLE
            AppUtils.loadImageCrop(
                selectedRole!!.url,
                binding!!.imgUserProfile,
                R.drawable.circle_default_pic,
                80,
                80
            )
        }else {
            binding!!.flLayout.visibility = View.VISIBLE
            binding!!.imgUserProfile.visibility = View.GONE

            binding!!.tvShortName.setText(
                selectedRole!!.studentName.substring(0, 2).toUpperCase()
            )


        }

    }


    private fun setAdapter(){
        binding!!.rvSubjectMarks.setHasFixedSize(true)
        binding!!.rvSubjectMarks.layoutManager = LinearLayoutManager(
            currActivity,
            LinearLayoutManager.VERTICAL, false
        )
        testHeadingAdapter = SubjectMarksParentAdapter(currActivity, subjectList)
        binding!!.rvSubjectMarks.adapter = testHeadingAdapter

        binding!!.rvCategory.setHasFixedSize(true)
        binding!!.rvCategory.layoutManager = LinearLayoutManager(
            currActivity,
            LinearLayoutManager.VERTICAL, false
        )
        categoryAdapter = ParentMarksCategoryAdapter(currActivity, scholasticMarksList)
        binding!!.rvCategory.adapter = categoryAdapter



        binding!!.rvStudentList.setHasFixedSize(true)
        binding!!.rvStudentList.layoutManager = LinearLayoutManager(currActivity,LinearLayoutManager.HORIZONTAL,false)
        studentAdapter = StudentAdapter(currActivity,studentList,selectedRole)
        binding!!.rvStudentList.adapter = studentAdapter

    }

    private fun initViews(){
        binding!!.toolbar.imgMenu.visibility = View.GONE
        binding!!.toolbar.imgBack.visibility = View.VISIBLE
        binding!!.toolbar.txtToolbarTitle.text = getString(R.string.student_marks)
        currActivity.application?.let {
            marksViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(MarksEntryViewModel::class.java)
        }


        currActivity!!.application?.let {
            examViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(ExamViewModel::class.java)
        }


        examViewModel.let {
            if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx!!) }) {
                it!!.getExaminationDropDown(selectedRole!!.classRoomId)
            }
        }

        observer()
    }

    private fun observer() {
        examViewModel?.getExamDropDownErrorData()?.observe(this, androidx.lifecycle.Observer {
            it.let {
                //                AppUtils.hideProgress(dialog!!)
                examDropDownList.clear()
                AndroidUtil.showToast(currActivity, it.message, true)
            }
        })


        examViewModel?.getExamDropDownLiveData()?.observe(this, Observer {
            examDropDownList.clear()
            examDropDownList.addAll(it)
        })

        marksViewModel?.getErrorMutableLiveData()?.observe(this, Observer {
            it.let {
                AndroidUtil.showToast(currActivity, it.message, true)
            }
        })
        marksViewModel?.getStudentMarksGraphLiveData()?.observe(this, Observer {
            marksGraphModel = it
            setGraph(it)
        })

        marksViewModel?.getStudentMarksCategoryLiveData()?.observe(this, Observer {
            if(it.testList != null){
                binding!!.rvCategory.visibility = View.GONE
                binding!!.rlMarks.visibility = View.VISIBLE
                testHeadingList.clear()
                testHeadingList.addAll(it.testList)
                marksViewModel?.let {
                    if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
//                mdialog =  AppUtils.showProgress(currActivity!!)
                        it.getStudentMarksGraph(studentProfileId) }
                }

                setData()
            }else if(it.scholasticMarksList != null){
                binding!!.rvCategory.visibility = View.VISIBLE
                binding!!.rlMarks.visibility = View.GONE
                scholasticMarksList.clear()
                scholasticMarksList.addAll(it.scholasticMarksList)
                categoryAdapter.notifyDataSetChanged()
            }

        })

    }

    private fun setGraph(list : ArrayList<MarksGraphModel>){

        val list = ArrayList<BarData>()

        // 20 items

        list.add(generateData( 1))

        val cda = ChartDataAdapter(applicationContext, list)
        binding!!.listView1.setAdapter(cda)


//        val cartesian = AnyChart.column()
//
//        val data = java.util.ArrayList<DataEntry>()
//
//        for(model in list){
//            data.add(ValueDataEntry(model.examName, model.overAllPercentage))
//
//        }
//
//        val column = cartesian.column(data)
//
////        column.tooltip()
////            .titleFormat("{%X}")
////            .position(Position.CENTER_BOTTOM)
////            .anchor(Anchor.CENTER_BOTTOM)
////            .offsetX(0.0)
////            .offsetY(5.0)
////            .format("\${%Value}{groupsSeparator: }")
//
//        cartesian.animation(true)
////        cartesian.title("Top 10 Cosmetic Products by Revenue")
//
//        cartesian.yScale().minimum(0.0)
//
//      //  cartesian.yAxis(0).labels().format("\${%Value}{groupsSeparator: }")
//
////        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
//        cartesian.interactivity().hoverMode(HoverMode.BY_X)
//
//        cartesian.xAxis(0).title("Product")
//        cartesian.yAxis(0).title("Revenue")
//
//        binding!!.anyChartView.setChart(cartesian)


    }


    private inner class ChartDataAdapter internal constructor(
        context: Context,
        objects: List<BarData>
    ) : ArrayAdapter<BarData>(context, 0, objects) {

        @SuppressLint("InflateParams")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView

            val data = getItem(position)

            val holder: ViewHolder

            if (convertView == null) {

                holder = ViewHolder()

                convertView = LayoutInflater.from(context).inflate(
                    R.layout.list_item_barchart, null
                )
                holder.chart = convertView!!.findViewById<BarChart>(R.id.chart)

                convertView.tag = holder

            } else {
                holder = convertView.tag as ViewHolder
            }

            // apply styling
            if (data != null) {
//                data.setValueTypeface(tfLight)
                data.setValueTextColor(Color.BLACK)
            }
            holder.chart!!.description.isEnabled = false
            holder.chart!!.setDrawGridBackground(false)


            val xAxisLabel = ArrayList<String>()
            for (model in marksGraphModel){
                xAxisLabel.add(model.examName.substring(0,2))
            }

//            xAxisLabel.add("Tue")
//            xAxisLabel.add("Wed")
//            xAxisLabel.add("Thu")
//            xAxisLabel.add("Fri")
//            xAxisLabel.add("Sat")
//            xAxisLabel.add("Sun")

            val xAxis = holder.chart!!.xAxis
            xAxis.valueFormatter = IndexAxisValueFormatter(xAxisLabel)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
//            xAxis.typeface = tfLight
            xAxis.setDrawGridLines(false)




            val leftAxis = holder.chart!!.axisLeft
//            leftAxis.typeface = tfLight
            leftAxis.setLabelCount(5, false)
            leftAxis.spaceTop = 15f

            val rightAxis = holder.chart!!.axisRight
//            rightAxis.typeface = tfLight
            rightAxis.setLabelCount(5, false)
            rightAxis.spaceTop = 15f

            // set data
            holder.chart!!.data = data
            holder.chart!!.setFitBars(true)

            // do not forget to refresh the chart
            //            holder.chart.invalidate();
            holder.chart!!.animateY(700)

            holder.chart!!.setOnChartValueSelectedListener(object : OnChartValueSelectedListener{

                 override public fun onValueSelected(e : Entry, h : Highlight)
                    {
                       val x=e.getX();
                        val y=e.getY();

                        StudentSubjectGraphActivity.open(currActivity,marksGraphModel[position].subjectMarksGraphDTO)
                     }

                override  public fun onNothingSelected()
                {

                }
            });

            return convertView
        }

        private inner class ViewHolder {

            internal var chart: BarChart? = null
        }
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return Line data
     */
    private fun generateData(cnt: Int): BarData {

        val entries = ArrayList<BarEntry>()

        for (i in 0 until marksGraphModel.size) {
            entries.add(BarEntry(i.toFloat(), marksGraphModel[i].overAllPercentage.toFloat()))
        }

        val d = BarDataSet(entries, "")
        val colorsList = ArrayList<Int>()
        colorsList.add(ContextCompat.getColor(currActivity, R.color.light_pink))
        colorsList.add(ContextCompat.getColor(currActivity, R.color.light_blue1))
        colorsList.add(ContextCompat.getColor(currActivity, R.color.light_orange1))
        colorsList.add(ContextCompat.getColor(currActivity, R.color.light_green1))
        d.setColors(colorsList)
        d.barShadowColor = Color.rgb(203, 203, 203)

        val sets = ArrayList<IBarDataSet>()
        sets.add(d)

        val cd = BarData(sets)
        cd.barWidth = 0.9f
        return cd
    }

    private fun setData(){
        if(Tid <  testHeadingList.size){
            currentTest = testHeadingList[Tid]
            binding!!.tvTestName.text = currentTest.testName
            subjectList.clear()
                subjectList.addAll(currentTest.subjectMarksList)
            testHeadingAdapter.notifyDataSetChanged()

            binding!!.imgPrev.setOnClickListener {
                Tid--
                setData()
            }
            binding!!.imgNext.setOnClickListener {
                Tid++
                setData()
            }

        }
    }

    private fun openExamDialog(){
        dialog = Dialog(currActivity as Context)
        dialogSelectClassSectionBinding = DataBindingUtil.inflate(
            LayoutInflater.from(currActivity),
            R.layout.dialog_select_class_and_section, null, false
        )
        dialog!!.setContentView(dialogSelectClassSectionBinding!!.getRoot())
        Objects.requireNonNull<Window>(dialog!!.getWindow())
            .setBackgroundDrawableResource(android.R.color.transparent)
        dialogSelectClassSectionBinding!!.tvTitle.text = resources.getString(R.string.select_exam_amp_test_name)
        dialogSelectClassSectionBinding!!.rlClassesDropdown.setHasFixedSize(true)
        dialogSelectClassSectionBinding!!.rlClassesDropdown.layoutManager = LinearLayoutManager(
            currActivity,
            LinearLayoutManager.VERTICAL, false
        )

        examDropDownAdapter = ExaminationDropDownAdapter(currActivity!!, examDropDownList, false,null, object :
            ExaminationDropDownAdapter.SectionSelection {
            override fun selectionCallback(parent: Int, child: Int) {
                examDropDownAdapter.notifyDataSetChanged()
            }

        })

        if(examDropDownList.size > 0){
            dialogSelectClassSectionBinding!!.rlNotFound.visibility = View.GONE
            dialogSelectClassSectionBinding!!.rlClassesDropdown.visibility = View.VISIBLE
        }else{
            dialogSelectClassSectionBinding!!.rlNotFound.visibility = View.VISIBLE
            dialogSelectClassSectionBinding!!.rlClassesDropdown.visibility = View.GONE
        }

        dialogSelectClassSectionBinding!!.rlClassesDropdown.adapter = examDropDownAdapter
        examDropDownAdapter.notifyDataSetChanged()

        dialogSelectClassSectionBinding!!.imgCancel.setOnClickListener {
            dialog!!.dismiss()
        }

        dialog!!.setCancelable(true)
        dialog!!.show()

    }

    fun examSelectd(data : ExaminationDropDownModel){
        examId = data.examId
        binding!!.tvExamName.text = data.examName
        examDropDownModel  = data
        dialog!!.dismiss()
        marksViewModel?.let {
            if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
//                mdialog =  AppUtils.showProgress(currActivity!!)
                it.getStudentMarksByProfileId(examId, studentProfileId) }
        }

    }


}
