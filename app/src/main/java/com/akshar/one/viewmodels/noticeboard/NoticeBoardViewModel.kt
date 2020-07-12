package com.akshar.one.viewmodels.noticeboard

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.akshar.one.model.ImageModel
import com.akshar.one.model.NoticeBoardModel
import com.akshar.one.model.StudentListModel
import com.akshar.one.repository.noticeboard.NoticeBoardRepository
import com.akshar.one.repository.student.StudentRepository
import com.akshar.one.util.AppUtil
import com.akshar.one.viewmodels.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException
import java.lang.Exception

class NoticeBoardViewModel(application: Application) : BaseViewModel(application){
    private var studentRepository : NoticeBoardRepository? = null
    private val isLoading = MutableLiveData<Boolean>()
    private val isSuccess = MutableLiveData<Boolean>()
    private var mutuableLiveDataNoticeList = MutableLiveData<List<NoticeBoardModel>>()
    private var mutableLiveDataCreateStudentProfile = MutableLiveData<StudentListModel>()
    private var mutableLiveDataImage = MutableLiveData<ImageModel>()

    init {
        studentRepository = NoticeBoardRepository()
    }

    fun getNoticeListLiveData() : MutableLiveData<List<NoticeBoardModel>> = mutuableLiveDataNoticeList
    fun getSuccessLiveData() : MutableLiveData<Boolean> = isSuccess

    fun getImageLiveData() : MutableLiveData<ImageModel> = mutableLiveDataImage


    fun getAllNotices(showExpired : Boolean){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    isLoading.postValue(false)
                    val noticeList = studentRepository?.getAllNotices(showExpired)
                    noticeList.let {
                        mutuableLiveDataNoticeList.postValue(noticeList)
                    }

                }catch (httpException : HttpException){
                    isLoading.postValue(false)
                    val errorResponse  = AppUtil.getErrorResponse(httpException.response()?.errorBody()?.string())
                    errorResponse.let { getErrorMutableLiveData().postValue(it) }

                }catch (e : Exception){
                    isLoading.postValue(false)
                    e.printStackTrace()
                }
            }
        }
    }

}