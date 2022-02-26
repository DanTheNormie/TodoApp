package com.noice.todo_app.data.repository

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.noice.rickmortyfacts.utils.NetworkCallStatus
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.lang.Exception


object FirebaseAuthRepository{

    private val TAG = "AuthAppRepository"
    private val auth: FirebaseAuth = Firebase.auth

    fun isUserSignedIn():Boolean{
        return auth.currentUser != null
    }

    fun getUser() = auth.currentUser

    fun isUserVerified():Boolean = auth.currentUser!!.isEmailVerified

    suspend fun sendVerificationEmail() = try{
        Result.success(auth.currentUser!!.sendEmailVerification().await())
    }catch (e:Exception){
        Result.failure(e)
    }

    suspend fun login(email: String, password: String)= try{
        Result.success(auth.signInWithEmailAndPassword(email, password).await())
    }catch (e:Exception){
        Result.failure(e)
    }

    suspend fun register(email: String, password: String) = try {
        Result.success(auth.createUserWithEmailAndPassword(email,password).await())
    }catch (e:Exception){
        Result.failure(e)
    }
}