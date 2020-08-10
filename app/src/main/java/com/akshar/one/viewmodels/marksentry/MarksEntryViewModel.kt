package com.akshar.one.viewmodels.marksentry

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.akshar.one.model.*
import com.akshar.one.repository.marksentry.MarksEntryRepository
import com.akshar.one.repository.student.StudentRepository
import com.akshar.one.util.AppUtil
import com.akshar.one.view.marksentry.StudentMarksEntry
import com.akshar.one.viewmodels.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException
import java.lang.Exception

class MarksEntryViewModel(application: Application) : BaseViewModel(application){
    private var marksEnterRepository : MarksEntryRepository? = null
    private val isLoading = MutableLiveData<Boolean>()
    private val isSuccess = MutableLiveData<Boolean>()
    private var mutuableLiveDataSubjectList = MutableLiveData<List<SubjectListModel>>()
    private var mutuableLiveDataSkillList = MutableLiveData<List<SkillListModel>>()
    private var mutuableLiveDataStudentList = MutableLiveData<List<StundentsMarksList>>()
    private var mutuableLiveDataStudentListpart2 = MutableLiveData<List<StundentsMarksList>>()

    init {
        marksEnterRepository = MarksEntryRepository()
    }

    fun getIsLoading(): MutableLiveData<Boolean> = isLoading

    fun getSubjectListLiveData() : MutableLiveData<List<SubjectListModel>> = mutuableLiveDataSubjectList
    fun getStudentListLiveData() : MutableLiveData<List<StundentsMarksList>> = mutuableLiveDataStudentList
    fun getSkillListLiveData() : MutableLiveData<List<SkillListModel>> = mutuableLiveDataSkillList
    fun getStudentMarksEntryLiveData() : MutableLiveData<List<StundentsMarksList>> = mutuableLiveDataStudentListpart2
    fun getSuccessLiveData() : MutableLiveData<Boolean> = isSuccess



    fun getSubjectList(classRoomId : Int){
        isLoading.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    isLoading.postValue(false)
                    val studentList = marksEnterRepository?.getSubjectListByClassId(classRoomId)
                    studentList.let {
                        mutuableLiveDataSubjectList.postValue(studentList)
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

    fun getSkillList(classRoomId : Int,subjectId: Int,area: String ){
        isLoading.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    isLoading.postValue(false)
                    val skillList = marksEnterRepository?.getSkillList(classRoomId,subjectId,area)
                    skillList.let {
                        mutuableLiveDataSkillList.postValue(skillList)
                    }

                }catch (httpException : HttpException){
                    isLoading.postValue(false)
                    val errorResponse  = AppUtil.getErrorResponse(httpException.response()?.errorBody()?.string())
                    errorResponse.let { getSkillErrorMutableLiveData().postValue(it) }

                }catch (e : Exception){
                    isLoading.postValue(false)
                    e.printStackTrace()
                }
            }
        }
    }
    fun addOrUpdateStudentMarks(studentMarksList : ArrayList<StundentsMarksList>){
        isLoading.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    isLoading.postValue(false)
                    val list = marksEnterRepository?.addOrUpdateStudentMarks(studentMarksList)
                    if(list != null){
                        mutuableLiveDataStudentListpart2.postValue(list)
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
    fun getStudentMarks(classRoomId : Int , examId : Int, testId : Int, subjectId : Int?, skillId : Int?){
        isLoading.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    isLoading.postValue(false)
                    if(testId != 0){
                        if(subjectId == null){
                            var skill = 0
                            if((skillId != null)){
                                skill = skillId
                            }
                            val studentList = marksEnterRepository?.getStudentMarksListTest(classRoomId,
                                examId,0,testId,skill)
                            studentList.let {
                                mutuableLiveDataStudentList.postValue(studentList)
                            }
                        }else{
                            var skill = 0
                            if((skillId != null)){
                                skill = skillId
                            }
                            val studentList = marksEnterRepository?.getStudentMarksList(classRoomId,
                                examId,subjectId,skill)
                            studentList.let {
                                mutuableLiveDataStudentList.postValue(studentList)
                            }
                        }


                    }else{
                        if(subjectId == null){
                            val studentList = marksEnterRepository?.getStudentMarksListTest(classRoomId,
                                examId,testId,0,testId)
                            studentList.let {
                                mutuableLiveDataStudentList.postValue(studentList)
                            }
                        }else{
                            val studentList = marksEnterRepository?.getStudentMarksListTest(classRoomId,
                                examId,testId,subjectId,testId)
                            studentList.let {
                                mutuableLiveDataStudentList.postValue(studentList)
                            }
                        }

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