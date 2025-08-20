package com.example.jetareader.di

import com.example.jetareader.network.BooksApi
import com.example.jetareader.repo.BookRepository
import com.example.jetareader.repo.FireRepository
import com.example.jetareader.util.Constants
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideBookRepository(api: BooksApi) = BookRepository(api)

    @Singleton
    @Provides
    fun provideFireRepository() = FireRepository(queryBook =
    FirebaseFirestore.getInstance().collection("books"))

    @Provides
    @Singleton
    fun provideBookQuery(): Query {
        return FirebaseFirestore.getInstance()
            .collection("books")
    }

    @Singleton
    @Provides
    fun provideBookApi(): BooksApi{
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BooksApi::class.java)
    }
}