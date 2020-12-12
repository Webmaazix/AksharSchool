package com.akshar.one.viewmodels.dashboard

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.akshar.one.view.home.adapter.TimeTableAdapter
import com.akshar.one.model.*
import com.akshar.one.repository.dashboard.DashboardRepository
import com.akshar.one.util.AppConstant
import com.akshar.one.util.AppUtil
import com.akshar.one.viewmodels.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class DashboardViewModel(application: Application) : BaseViewModel(application) {

    private var loginRepository: DashboardRepository? = null
    private var mutableLiveDataTimeTableModel = MutableLiveData<List<PeriodTimeTable>>()
    private var mutableLiveDataClassTimeTableModel = MutableLiveData<List<PeriodTimeTable>>()
    private var mutableLiveDataBirthDayModel  = MutableLiveData<List<BirthDayModel>>()
    private var mutableLiveDataAllFinanceModel  = MutableLiveData<FinanceModel>()
    private var mutableLiveDataSecurityGroups  = MutableLiveData<ArrayList<String>>()
    private var mutableLiveDataCollection = MutableLiveData<ArrayList<FeePayment>>()
    private var mutableLiveDataExpense = MutableLiveData<ArrayList<FeePayment>>()
    private var mutableLiveDataAttendanceStats = MutableLiveData<ArrayList<AttendanceDashboard>>()
    private var mutableLiveDataPresentAttendance = MutableLiveData<StudentAttendanceModel>()
    private val isLoading = MutableLiveData<Boolean>()
    private var timeTableAdapter : TimeTableAdapter? =null

    init {
        loginRepository = DashboardRepository()
      //  timeTableAdapter = TimeTableAdapter(this)
    }

    fun setTimeTableAdapter(timeTableList: List<TimeTableModel>?) {
      //  timeTableAdapter?.setTimeTableList(timeTableList)

    }

    fun getIsLoading(): MutableLiveData<Boolean> = isLoading

    fun getTimeTableLiveData(): MutableLiveData<List<PeriodTimeTable>> =
        mutableLiveDataTimeTableModel
    fun getAttendanceStatsLiveData(): MutableLiveData<ArrayList<AttendanceDashboard>> =
        mutableLiveDataAttendanceStats
    fun getPresentAttendanceStatsLiveData(): MutableLiveData<StudentAttendanceModel> =
        mutableLiveDataPresentAttendance
    fun getSecurityGroupsLiveData(): MutableLiveData<ArrayList<String>> =
        mutableLiveDataSecurityGroups

    fun getClassTimeTableLiveData(): MutableLiveData<List<PeriodTimeTable>> =
        mutableLiveDataClassTimeTableModel

    fun getTimeTableAdapter(): TimeTableAdapter? = timeTableAdapter

//    fun getTimeTableAt(position: Int): TimeTableModel? =
//        mutableLiveDataTimeTableModel.value?.getOrNull(position)


    fun getBirthdayLiveData(): MutableLiveData<List<BirthDayModel>> =
        mutableLiveDataBirthDayModel

    fun getExpenseLiveData() : MutableLiveData<ArrayList<FeePayment>> =
        mutableLiveDataExpense

    fun getTimeTableOfEmployee(employeeId: Int, date: String) {
        isLoading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val loginModel = loginRepository?.getTimeTableOfEmployee(employeeId, date)
                    mutableLiveDataTimeTableModel.postValue(loginModel!!.get(0).periodwiseTimetableList)
                    isLoading.postValue(false)
                   /* loginModel?.let {
                        mutableLiveDataTimeTableModel.postValue(it)
                    }*/
                } catch (httpException: HttpException) {
                    isLoading.postValue(false)
                    val errorResponse =
                        AppUtil.getErrorResponse(httpException.response()?.errorBody()?.string())
                    errorResponse?.let { getErrorMutableLiveData().postValue(it) }
                } catch (e: Exception) {
                    isLoading.postValue(false)
                    Log.d(AppConstant.TAG, "Login Exception : $e")
                }
            }
        }
    }
    fun getTimeTableOfClass(classRoomId: Int, date: String) {
        isLoading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val loginModel = loginRepository?.getTimeTableOfClass(classRoomId, date)
                    mutableLiveDataClassTimeTableModel.postValue(loginModel!!.get(0).periodwiseTimetableList)
                    isLoading.postValue(false)
                   /* loginModel?.let {
                        mutableLiveDataTimeTableModel.postValue(it)
                    }*/
                } catch (httpException: HttpException) {
                    isLoading.postValue(false)
                    val errorResponse =
                        AppUtil.getErrorResponse(httpException.response()?.errorBody()?.string())
                    errorResponse?.let { getErrorMutableLiveData().postValue(it) }
                } catch (e: Exception) {
                    isLoading.postValue(false)
                    Log.d(AppConstant.TAG, "Login Exception : $e")
                }
            }
        }
    }

    fun getFinanceSummeryLiveData() : MutableLiveData<FinanceModel> =
        mutableLiveDataAllFinanceModel

    fun getAllFinanceSummery(fromDate : String, toDate: String){
        isLoading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    isLoading.postValue(false)
                    val financeModel = loginRepository?.getAllFinance(fromDate,toDate)
                    financeModel?.let {
                        mutableLiveDataAllFinanceModel.postValue(it)
                    }

                }catch (httpException : HttpException){
                    isLoading.postValue(false)
                    val errorResponse = AppUtil.getErrorResponse(httpException.response()?.errorBody()?.string())
                    errorResponse?.let { getErrorMutableLiveData().postValue(it)}
                }catch (e: Exception){
                    isLoading.postValue(false)
                    Log.d(AppConstant.TAG,"FinanceEntity Exception : &e")
                }
            }
        }
    }
    fun getSecurityGroupsList(appName : String){
        isLoading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    isLoading.postValue(false)
                    val financeModel = loginRepository?.getSecurityGroupsList(appName)
                    financeModel?.let {
                        mutableLiveDataSecurityGroups.postValue(it)
                    }

                }catch (httpException : HttpException){
                    isLoading.postValue(false)
                    val errorResponse = AppUtil.getErrorResponse(httpException.response()?.errorBody()?.string())
                    errorResponse?.let { getErrorMutableLiveData().postValue(it)}
                }catch (e: Exception){
                    isLoading.postValue(false)
                    Log.d(AppConstant.TAG,"FinanceEntity Exception : &e")
                }
            }
        }
    }

    fun getCollectionLiveData() : MutableLiveData<ArrayList<FeePayment>> =
        mutableLiveDataCollection


    fun getCollectionData(groupBy : String, fromDate: String,toDate: String){
        isLoading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    isLoading.postValue(false)
                    val collection = loginRepository?.getCollection(groupBy,fromDate,toDate)
                    mutableLiveDataCollection.postValue(collection)

                }catch (httpException : HttpException){
                    isLoading.postValue(false)
                    val errorResponse = AppUtil.getErrorResponse(httpException.response()?.errorBody()?.string())
                    errorResponse?.let { getCollectionErrorMutableLiveData().postValue(it)}
                }catch (e: Exception){
                    isLoading.postValue(false)
                    Log.d(AppConstant.TAG, "FinanceEntity Exception :$e")
                }
            }
        }
    }

    fun getExpences(groupBy: String,fromDate: String,toDate: String){
        isLoading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    isLoading.postValue(false)
                    val expence = loginRepository?.getExpences(groupBy,fromDate,toDate)
                    mutableLiveDataExpense.postValue(expence)
                }catch (httException : HttpException){
                    isLoading.postValue(false)
                    val errorResponse = AppUtil.getErrorResponse(httException.response()?.errorBody()?.string())
                    errorResponse?.let { getExpenseErrorMutableLiveData().postValue(it) }
                }catch (e : Exception){
                    isLoading.postValue(false)
                    Log.d(AppConstant.TAG, "FinanceEntity Exception :$e")

                }
            }
        }

    }

    fun getBirthdays(fromDate: String, toDate: String) {
        isLoading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val birthdayModel = loginRepository?.getBirthdays(fromDate, toDate)
                   mutableLiveDataBirthDayModel.postValue(birthdayModel)
                    isLoading.postValue(false)
//                    birthdayModel?.let {data ->
//                        mutableLiveDataBirthDayModel.postValue(data)
//                    }
                } catch (httpException: HttpException) {
                    isLoading.postValue(false)
                    val errorResponse =
                        AppUtil.getErrorResponse(httpException.response()?.errorBody()?.string())
                    errorResponse?.let { getErrorMutableLiveData().postValue(it) }
                } catch (e: Exception) {
                    isLoading.postValue(false)
                    Log.d(AppConstant.TAG, "Login Exception : $e")
                }
            }
        }
    }
    fun getAttendanceStatsOfStudent(fromDate: String?, toDate: String?,studentProfileId : String) {
        isLoading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    if(fromDate == null){
                        val attendanceStats = loginRepository?.getAttendanceStatsOfyear(studentProfileId)
                        mutableLiveDataAttendanceStats.postValue(attendanceStats)
                        isLoading.postValue(false)
                    }else{
                        val attendanceStats = loginRepository?.getAttendanceStatsOfStudent(fromDate, toDate!!,studentProfileId)
                        mutableLiveDataAttendanceStats.postValue(attendanceStats)
                        isLoading.postValue(false)
                    }
                } catch (httpException: HttpException) {
                    isLoading.postValue(false)
                    val errorResponse =
                        AppUtil.getErrorResponse(httpException.response()?.errorBody()?.string())
                    errorResponse?.let { getErrorMutableLiveData().postValue(it) }
                } catch (e: Exception) {
                    isLoading.postValue(false)
                    Log.d(AppConstant.TAG, "Login Exception : $e")
                }
            }
        }
    }
    fun getPresentAttendance(studentProfileId : String) {
        isLoading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val attendanceStats = loginRepository?.getPresentAttendance(studentProfileId)
                   mutableLiveDataPresentAttendance.postValue(attendanceStats)
                    isLoading.postValue(false)
//                    birthdayModel?.let {data ->
//                        mutableLiveDataBirthDayModel.postValue(data)
//                    }
                } catch (httpException: HttpException) {
                    isLoading.postValue(false)
                    val errorResponse =
                        AppUtil.getErrorResponse(httpException.response()?.errorBody()?.string())
                    errorResponse?.let { getErrorMutableLiveData().postValue(it) }
                } catch (e: Exception) {
                    isLoading.postValue(false)
                    Log.d(AppConstant.TAG, "Login Exception : $e")
                }
            }
        }
    }
}