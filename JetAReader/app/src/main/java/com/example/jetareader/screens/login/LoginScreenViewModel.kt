package com.example.jetareader.screens.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetareader.model.MUser
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class LoginScreenViewModel : ViewModel() {
   // val loadingState =  MutableStateFlow(LoadingState.IDLE)
    private val auth: FirebaseAuth = Firebase.auth

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    fun signInWithEmailAndPassword(email: String, password: String,home: () -> Unit)
    = viewModelScope.launch{
        try {
            auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener { task->
                if (task.isSuccessful){

                    val displayName = task.result.user?.email?.split('@')?.get(0)

                    createUser(displayName)

                    Log.d("FB", "signInWithEmailAndPassword: yaya şaşa : ${task.result.toString()}")
                    home()
                }else{
                    Log.d("","signInWithEmailAndPassword: ${task.result.toString()}")
                }

            }
        }catch (ex: Exception){
            Log.d("FB", "signInWithEmailAndPassword:  ${ex.localizedMessage}")
        }
    }

    private fun createUser(displayName: String?) {
        val userId = auth.currentUser?.uid    // mevcut kullaniciyi cekme

        val user = MUser(
            userId = userId.toString(),
            displayName = displayName.toString(),
            avatarUrl ="",
            quote = "Life İs Great",
            profession = "Android Developer",
            id = null).toMap()    // mapleme islemini yaptik


        FirebaseFirestore.getInstance()     // firebase'e yollama
            .collection("users")
            .add(user)
    }

    fun createUserWithEmailAndPassword(
        email : String,
        password: String,
        home: () ->Unit){
        if (_loading.value == false){
            _loading.value = true
            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        home()
                    }
                    else{
                    Log.d("result", "createUserWithEmailAndPassword: ${task.result.toString()}")

                    }
                    _loading.value = true
                }
        }
    }
}


