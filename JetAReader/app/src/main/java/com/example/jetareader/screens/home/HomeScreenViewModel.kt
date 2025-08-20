package com.example.jetareader.screens.home

import MBook
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetareader.data.DataOrException
import com.example.jetareader.repo.FireRepository
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val repository: FireRepository) : ViewModel() {

    val data: MutableState<DataOrException<List<MBook>, Boolean, Exception>>
            = mutableStateOf(DataOrException(listOf(),true,Exception("")))

    init {
        getAllBooksFromDatabase()
    }


    fun getAllBooksFromDatabase(){
        viewModelScope.launch {
            data.value.loading = true
            data.value = repository.getAllBooksFromDatabase()
            Log.d("BOOKS", data.value.toString())

            if(!data.value.data.isNullOrEmpty()) data.value.loading = false
        }
    }
    fun updateStartedReading(book: MBook) {
        val updatedBook = mapOf(
            "started_reading" to Timestamp.now()
        )
        FirebaseFirestore.getInstance()
            .collection("books")
            .document(book.id!!)
            .update(updatedBook)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    getAllBooksFromDatabase() // güncel listeyi çek
                }
            }
    }

    fun updateFinishedReading(book: MBook) {
        val updatedBook = mapOf(
            "finished_reading" to Timestamp.now()
        )
        FirebaseFirestore.getInstance()
            .collection("books")
            .document(book.id!!)
            .update(updatedBook)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    getAllBooksFromDatabase() // güncel listeyi çek
                }
            }
    }

}