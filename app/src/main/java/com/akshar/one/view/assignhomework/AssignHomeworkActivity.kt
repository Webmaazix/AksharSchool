package com.akshar.one.view.assignhomework

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.akshar.one.DummyContent
import com.akshar.one.R
import com.akshar.one.adapter.AdapterCommonViewPager
import com.akshar.one.databinding.ActivityAssignHomeworkBinding
import com.akshar.one.databinding.ActivityNoticeboardBinding
import com.akshar.one.databinding.FragmentActiveNoticeBinding
import com.akshar.one.model.NoticeBoardModel
import com.akshar.one.model.TabsModel
import com.akshar.one.swipelayout.util.Attributes
import com.akshar.one.util.AndroidUtil
import com.akshar.one.view.noticeboard.ActiveNoticeFragment
import com.akshar.one.view.noticeboard.CreateNoticeActivity
import com.akshar.one.view.noticeboard.ExpiredNotice
import com.akshar.one.view.noticeboard.NoticeboardActivity
import com.akshar.one.view.noticeboard.adapter.MyNoticeBoardAdapter
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.noticeboard.NoticeBoardViewModel
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.main_toolbar.view.*
import kotlinx.android.synthetic.main.tablayout.view.*

class AssignHomeworkActivity : AppCompatActivity(),View.OnClickListener {

    private var currActivity : Activity = this
    private var binding : ActivityAssignHomeworkBinding? = null
    var customTabs = ArrayList<TabsModel>()
    lateinit var sentHomeworkFragment: SentHomeworkFragment
    lateinit var draftHomeworkFragment: DraftHomeworkFragment

    companion object{
        fun open(currActivity : Activity){
            val intent = Intent(currActivity, AssignHomeworkActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            currActivity.startActivity(intent)
            currActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(currActivity,R.layout.activity_assign_homework)
        customTabs = DummyContent.assignHomework()
        setUpViewPager()
        initViews()
    }

    private fun initViews(){
        binding!!.toolbar.imgMenu.visibility = View.GONE
        binding!!.toolbar.imgBack.visibility = View.VISIBLE
        binding!!.toolbar.txtToolbarTitle.text = currActivity.resources.getString(R.string.notice_board)
        setListner()
    }

    private fun setListner(){
        binding!!.toolbar.imgBack.setOnClickListener(this)
        binding!!.imgCreateNotice.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.imgBack ->{
                onBackPressed()
            }
            R.id.imgCreateNotice ->{
                CreateHomeworkActivity.open(currActivity)
            }

        }
    }

    private fun setUpViewPager(){
        val adapter = AdapterCommonViewPager(currActivity,supportFragmentManager)
        sentHomeworkFragment = SentHomeworkFragment()
        draftHomeworkFragment = DraftHomeworkFragment()
        adapter.addFragment(sentHomeworkFragment,"")
        adapter.addFragment(draftHomeworkFragment,"")
        binding!!.vpPager.adapter = adapter

        binding!!.tbLayout.setupWithViewPager( binding!!.vpPager)

        for(i in 0 until binding!!.tbLayout.tabCount){
            binding!!.tbLayout.getTabAt(i)!!.customView = getCustomView(i)
        }

        binding!!.vpPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding!!.tbLayout))

        binding!!.tbLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding!!.vpPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    private fun getCustomView(position : Int) : View {
        val view = LayoutInflater.from(currActivity).inflate(R.layout.tablayout, null)
        view.title.text = customTabs[position].name
        return view
    }

}
