package com.example.jetareader.repo

import android.util.Log
import com.example.jetareader.data.Resource
import com.example.jetareader.model.Item
import com.example.jetareader.network.BooksApi
import javax.inject.Inject

class BookRepository @Inject constructor(private val api: BooksApi) {

    suspend fun getBooks(searchQuery: String): Resource<List<Item>> {
        Log.d("API_CALL", "Query: $searchQuery")
        return try {
            val response = api.getAllBooks(searchQuery)
            Log.d("API_CALL", "Response: $response")
            val itemList = response.items ?: emptyList()
            Resource.Success(data = itemList)
        } catch (exception: Exception) {
            Resource.Error(message = exception.message ?: "An error occurred")
        }
    }

    suspend fun getBookInfo(bookId: String): Resource<Item> {
        return try {
            val response = api.getBookInfo(bookId)
            Resource.Success(data = response)
        } catch (exception: Exception) {
            Resource.Error(message = "An error occurred: ${exception.message}")
        }
    }
}
