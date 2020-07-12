package com.akshar.one.viewmodels.dashboard

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.akshar.one.adapter.TimeTableAdapter
import com.akshar.one.database.entity.CourseEntity
import com.akshar.one.model.*
import com.akshar.one.repository.dashboard.DashboardRepository
import com.akshar.one.util.AppConstant
import com.akshar.one.util.AppUtil
import com.akshar.one.view.attendance.adapters.CourseAdapter
import com.akshar.one.viewmodels.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class DashboardViewModel(application: Application) : BaseViewModel(application) {

    private var loginRepository: DashboardRepository? = null
    private var mutableLiveDataTimeTableModel = MutableLiveData<List<TimeTableModel>>()
    private var mutableLiveDataClassTimeTableModel = MutableLiveData<List<TimeTableModel>>()
    private var mutableLiveDataBirthDayModel  = MutableLiveData<List<BirthDayModel>>()
    private var mutableLiveDataAllFinanceModel  = MutableLiveData<FinanceModel>()
    private var mutableLiveDataCollection = MutableLiveData<FeePayment>()
    private var mutableLiveDataExpense = MutableLiveData<ExpenseSummary>()
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

    fun getTimeTableLiveData(): MutableLiveData<List<TimeTableModel>> =
        mutableLiveDataTimeTableModel

    fun getClassTimeTableLiveData(): MutableLiveData<List<TimeTableModel>> =
        mutableLiveDataClassTimeTableModel

    fun getTimeTableAdapter(): TimeTableAdapter? = timeTableAdapter

    fun getTimeTableAt(position: Int): TimeTableModel? =
        mutableLiveDataTimeTableModel.value?.getOrNull(position)


    fun getBirthdayLiveData(): MutableLiveData<List<BirthDayModel>> =
        mutableLiveDataBirthDayModel

    fun getExpenseLiveData() : MutableLiveData<ExpenseSummary> =
        mutableLiveDataExpense

    fun getTimeTableOfEmployee(employeeId: Int, date: String) {
        isLoading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val loginModel = loginRepository?.getTimeTableOfEmployee(employeeId, date)
                    mutableLiveDataTimeTableModel.postValue(loginModel!!)
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
                    mutableLiveDataClassTimeTableModel.postValue(loginModel!!)
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

    fun getCollectionLiveData() : MutableLiveData<FeePayment> =
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
                    errorResponse?.let { getErrorMutableLiveData().postValue(it)}
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
                    errorResponse?.let { getErrorMutableLiveData().postValue(it) }
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
}