package com.akshar.one.model

data class TimeTableModel(val classroom: ClassRoom?,
                          val weekDay: String?,
                          val subjectName: String?,
                          val startTime: String?,
                          val endTime: String?,
                          val teacherName: String?,
                          val sessionNumber: Int)