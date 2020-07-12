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
}

