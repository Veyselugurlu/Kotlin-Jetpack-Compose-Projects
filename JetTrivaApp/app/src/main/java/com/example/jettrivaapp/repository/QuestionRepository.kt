package com.example.jettrivaapp.repository

import android.util.Log
import com.example.jettrivaapp.data.DataOrException
import com.example.jettrivaapp.model.QuestionItem
import com.example.jettrivaapp.network.QuestionApi
import javax.inject.Inject

class QuestionRepository @Inject constructor(private val api : QuestionApi) {
    private val dataOrException = DataOrException<ArrayList<QuestionItem>,Boolean,Exception>()

    suspend fun getAllQuestions(): DataOrException<ArrayList<QuestionItem>,Boolean,Exception>{
        try {
            dataOrException.loading = true
            dataOrException.data= api.getAllQuestions()
            if (dataOrException.data.toString().isNotEmpty()) dataOrException.loading = false

        }catch (exception: Exception){
            dataOrException.e= exception
            Log.d("excetion","gelAllQuestion: ${dataOrException.e!!.localizedMessage}")
        }
        return dataOrException
    }
}