package com.akshar.one

import android.app.Activity
import android.content.Context
import com.akshar.one.model.TabsModel


object DummyContent {


    fun timeTable(): ArrayList<TabsModel> {
        val timeTableList = ArrayList<TabsModel>()

        val timeTableOne = TabsModel()
        timeTableOne.name = "My TimeTable"

        val timeTableTwo = TabsModel()
        timeTableTwo.name = "Class TimeTable"


        timeTableList.add(timeTableOne)
        timeTableList.add(timeTableTwo)

        return timeTableList
    }
    fun feeModule(): ArrayList<TabsModel> {
        val timeTableList = ArrayList<TabsModel>()

        val timeTableOne = TabsModel()
        timeTableOne.name = "Fee Details"

        val timeTableTwo = TabsModel()
        timeTableTwo.name = "Payment History"


        timeTableList.add(timeTableOne)
        timeTableList.add(timeTableTwo)

        return timeTableList
    }
    fun noticeBoard(): ArrayList<TabsModel> {
        val noticeBoardList = ArrayList<TabsModel>()

        val noticeBoardOne = TabsModel()
        noticeBoardOne.name = "Active Notice"

        val noticeBoardTwo = TabsModel()
        noticeBoardTwo.name = "Expired Notice"


        noticeBoardList.add(noticeBoardOne)
        noticeBoardList.add(noticeBoardTwo)

        return noticeBoardList
    }
    fun assignHomework(): ArrayList<TabsModel> {
        val noticeBoardList = ArrayList<TabsModel>()

        val noticeBoardOne = TabsModel()
        noticeBoardOne.name = "Sent"

        val noticeBoardTwo = TabsModel()
        noticeBoardTwo.name = "Draft"


        noticeBoardList.add(noticeBoardOne)
        noticeBoardList.add(noticeBoardTwo)

        return noticeBoardList
    }
}

