package com.akshar.one.view.marksentry

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.akshar.one.R
import com.akshar.one.databinding.ActivityStudentSubjectGraphBinding
import com.akshar.one.model.SubjectMarksGraphDTO
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.ColorTemplate.*
import kotlinx.android.synthetic.main.main_toolbar.view.*
import java.util.*
import kotlin.collections.ArrayList


class StudentSubjectGraphActivity : AppCompatActivity() {

    private var binding : ActivityStudentSubjectGraphBinding? = null
    private var currActivity : Activity = this
    private var list = ArrayList<SubjectMarksGraphDTO>()

    companion object{
        fun open(currActivity : Activity,list : ArrayList<SubjectMarksGraphDTO>){
            val intent = Intent(currActivity, StudentSubjectGraphActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("list",list)
            currActivity.startActivity(intent)
            currActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(currActivity,R.layout.activity_student_subject_graph)
        list = intent.getSerializableExtra("list") as ArrayList<SubjectMarksGraphDTO>

        binding!!.toolbar.imgMenu.visibility = View.GONE
        binding!!.toolbar.imgBack.visibility = View.VISIBLE
        binding!!.toolbar.txtToolbarTitle.text = getString(R.string.student_marks)
       // setContentView(R.layout.activity_student_subject_graph)

        val list = ArrayList<BarData>()

        // 20 items

            list.add(generateData( 1))

        val cda = ChartDataAdapter(applicationContext, list)
        binding!!.listView1.setAdapter(cda)

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
            for (model in list){
                xAxisLabel.add(model.subjectName.substring(0,2))
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

        for (i in 0 until list.size) {
            entries.add(BarEntry(i.toFloat(), list[i].subjectPercentage.toFloat()))
        }

        val d = BarDataSet(entries, "")

        val colorsList = ArrayList<Int>()
        colorsList.add(
            ContextCompat.getColor(currActivity, R.color.light_pink))
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
}
