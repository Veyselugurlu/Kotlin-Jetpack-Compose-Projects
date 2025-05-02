package com.example.jettrivaapp.network

import com.example.jettrivaapp.model.Question
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
interface QuestionApi {
    @GET("world.json")
    suspend fun getAllQuestions() : Question
}