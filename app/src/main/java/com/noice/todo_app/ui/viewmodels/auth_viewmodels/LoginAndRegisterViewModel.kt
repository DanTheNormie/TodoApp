package com.noice.todo_app.ui.viewmodels.auth_viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noice.todo_app.data.repository.FirebaseAuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginAndRegisterViewModel : ViewModel() {

    private val fireAuth = FirebaseAuthRepository

    private val _counter = MutableLiveData<Int>()
    val counter:LiveData<Int> = _counter
    fun startCounter(){
        viewModelScope.launch {
            for (i in 60 downTo 1) {
                delay(1000L)
                _counter.postValue(i)
            }
        }

    }

    suspend fun registerUser(email:String,pass:String) = fireAuth.register(email,pass)

    suspend fun loginUser(email:String,pass:String) = fireAuth.login(email,pass)

    suspend fun sendEmailVerfication() = fireAuth.sendVerificationEmail()

    fun getUser() = fireAuth.getUser()
    fun isUserSignedIn() = fireAuth.isUserSignedIn()
    fun isUserVerified() = fireAuth.isUserVerified()
}