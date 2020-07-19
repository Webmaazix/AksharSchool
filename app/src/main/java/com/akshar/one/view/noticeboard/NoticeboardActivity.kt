package com.akshar.one.view.noticeboard

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import com.akshar.one.DummyContent
import com.akshar.one.R
import com.akshar.one.adapter.AdapterCommonViewPager
import com.akshar.one.databinding.ActivityNoticeboardBinding
import com.akshar.one.model.TabsModel
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.main_toolbar.view.*
import kotlinx.android.synthetic.main.tablayout.view.*

class NoticeboardActivity : AppCompatActivity(),View.OnClickListener {

    private var currActivity : Activity = this
    private var binding : ActivityNoticeboardBinding? = null
    var customTabs = ArrayList<TabsModel>()
    lateinit var activeNoticeFragment: ActiveNoticeFragment
    lateinit var expiredNotice: ExpiredNotice

    companion object{
        fun open(currActivity : Activity){
            val intent = Intent(currActivity, NoticeboardActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            currActivity.startActivity(intent)
            currActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(currActivity,R.layout.activity_noticeboard)
        customTabs = DummyContent.noticeBoard()
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
                CreateNoticeActivity.open(currActivity,null)
            }

        }
    }

    private fun setUpViewPager(){
        val adapter = AdapterCommonViewPager(currActivity,supportFragmentManager)
        activeNoticeFragment = ActiveNoticeFragment()
        expiredNotice = ExpiredNotice()
        adapter.addFragment(activeNoticeFragment,"")
        adapter.addFragment(expiredNotice,"")
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
