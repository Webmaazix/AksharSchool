package com.akshar.one.view.feeandpayments


import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.akshar.one.R
import com.akshar.one.databinding.FragmentStudentListForFeesBinding
import com.akshar.one.view.activity.MainActivity

/**
 * A simple [Fragment] subclass.
 */
class StudentListForFeesFragment : Fragment(),View.OnClickListener {
    private lateinit var currActivity : Activity
    private var mainActivity: MainActivity? = null
    private var binding : FragmentStudentListForFeesBinding? = null

    companion object {
        @JvmStatic
        fun newInstance() = StudentListForFeesFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_student_list_for_fees, container, false)
        currActivity = activity!!
        setListner()
        return binding?.root
    }

    private fun setListner(){
        binding?.rlMain!!.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.rlMain ->{

            }

        }
    }

    private fun initViews() {
        mainActivity?.setToolbarTitle(getString(R.string.attendance))
    }


}
