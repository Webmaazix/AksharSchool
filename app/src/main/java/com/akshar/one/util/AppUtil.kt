package com.akshar.one.util

import com.akshar.one.api.response.ErrorResponse
import com.google.gson.Gson
import org.json.JSONObject
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*
import android.util.Log
import java.text.DateFormat
import java.text.ParseException
import java.util.regex.Pattern


object AppUtil {

    fun fromJson(jsonString: String?, type: Type): Any? {
        return Gson().fromJson(jsonString, type)
    }

    fun getErrorResponse(jsonString: String?): ErrorResponse? {
        return fromJson(jsonString, ErrorResponse::class.java) as ErrorResponse?
    }

    @Suppress("IMPLICIT_CAST_TO_ANY")
    fun jsonFromModel(model: Any): JSONObject {
        if (model is String) {
            return JSONObject(Gson().toJson(JSONObject(model)))
        }
        return JSONObject(Gson().toJson(model))
    }

    fun getCurrentDate() : String?{
        val sdf = SimpleDateFormat("YYYY-MM-dd")
        val currentDate = sdf.format(Date())
        //System.out.println(" C DATE is  "+currentDate)
        return currentDate
    }

    fun getCurrentYear() : Int{
       return Calendar.getInstance().get(Calendar.YEAR);
    }

//    fun getBirthdayDate(str_date : String) : String?{
//        val formatter: DateFormat
//        var date: Date? = null
//        formatter = SimpleDateFormat("dd-MMMM-yyyy")
//        date = formatter.parse(str_date)
//        return date.toString()
//    }
    fun getSevenDaysBack(): String? {
        val cal = GregorianCalendar.getInstance();
        cal.setTime(Date());
        cal.add(Calendar.DAY_OF_YEAR, -7)
        val sdf = SimpleDateFormat("YYYY-MM-dd")
        val formattedDate = sdf.format(cal.getTime())
       return formattedDate
    }
    fun getSevenDaysAfter(): String? {
        val cal = GregorianCalendar.getInstance();
        cal.setTime(Date());
        cal.add(Calendar.DAY_OF_YEAR, 7)
        val sdf = SimpleDateFormat("YYYY-MM-dd")
        val formattedDate = sdf.format(cal.getTime())
       return formattedDate
    }
    fun get30DaysBack(): String? {
        val cal = GregorianCalendar.getInstance();
        cal.setTime(Date());
        cal.add(Calendar.DAY_OF_YEAR, -30)
        val sdf = SimpleDateFormat("YYYY-MM-dd")
        val formattedDate = sdf.format(cal.getTime())
       return formattedDate
    }


   private  fun yesterday(): Date {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -1)
        return cal.time
    }
   private  fun tomorrow(): Date {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, 1)
        return cal.time
    }

     fun getYesterdayDateString(): String {
        val dateFormat = SimpleDateFormat("YYYY-MM-dd")
        return dateFormat.format(yesterday())
    }
     fun getTomorrowDateString(): String {
        val dateFormat = SimpleDateFormat("YYYY-MM-dd")
        return dateFormat.format(tomorrow())
    }

    fun isValidEmail(email: String): Boolean {
        val regex = ("[a-zA-Z0-9+._%-+]{1,256}" + "@"
                + "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" + "."
                + "(" + "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" + ")+")
        //        String regex = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";
        return Pattern.matches(regex, email)
    }


}