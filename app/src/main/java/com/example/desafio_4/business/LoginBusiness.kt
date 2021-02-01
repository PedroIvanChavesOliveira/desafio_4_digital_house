package com.example.desafio_4.business

import com.example.desafio_4.repository.LoginRepository
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class LoginBusiness {
    private val repository by lazy {
        LoginRepository()
    }

    suspend fun signInAuthentication(email: String, password: String): FirebaseUser? {
        repository.signInAuthentication(email, password)?.let {
            return it
        }?: run {
            return null
        }
    }

    fun startAnalytics(): FirebaseAnalytics{
        return repository.startAnalytics()
    }

//    fun analyticsEvent(logName: String) {
//        repository.analyticsEvent(logName)
//    }
//
//    fun analyticsLogInEvent() {
//        repository.analyticsLogInEvent()
//    }
}