package com.akshar.one.model

import androidx.room.Embedded
import androidx.room.Relation
import com.akshar.one.database.entity.ClassRoomEntity
import com.akshar.one.database.entity.CourseEntity

data class CourseWithClassRoomModel(
    @Embedded val courseEntity: CourseEntity,
    @Relation(
        parentColumn = CourseEntity.COURSE_ID,
        entityColumn = ClassRoomEntity.COURSE_ID
    )
    val classRoomList: List<ClassRoomEntity>
)