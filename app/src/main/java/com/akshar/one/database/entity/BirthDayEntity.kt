package com.akshar.one.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    tableName = BirthDayEntity.TABLE_NAME
)
data class BirthDayEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID) var id : Long = 0,
    @ColumnInfo(name = USER_ID) var userId : Int?,
    @ColumnInfo(name = FIRST_NAME)var firstName : String?,
    @ColumnInfo(name = LASTNAME)var lastName : String?,
    @ColumnInfo(name = BIRTHDAY)var birthDay : String?,
    @ColumnInfo(name = PROFILE_TYPE)var profileType : String?,
    @ColumnInfo(name = IMAGE_URL) var imageUrl : String?
) {

    companion object{
        const val TABLE_NAME = "birthday"
        const val ID = "id"
        const val USER_ID = "userId"
        const val FIRST_NAME = "firstName"
        const val LASTNAME = "lastName"
        const val BIRTHDAY = "birthday"
        const val PROFILE_TYPE = "profileType"
        const val IMAGE_URL = "imageUrl"

    }
}