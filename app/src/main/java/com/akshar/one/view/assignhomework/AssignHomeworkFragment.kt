package com.akshar.one.view.assignhomework


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.akshar.one.R

/**
 * A simple [Fragment] subclass.
 */
class AssignHomeworkFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() = AssignHomeworkFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_assign_homework, container, false)
    }


}
