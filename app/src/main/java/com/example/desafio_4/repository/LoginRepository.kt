package com.example.desafio_4.repository

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class LoginRepository {
    private val firebaseAuth by lazy {
        Firebase.auth
    }
    private val analytics by lazy {
        Firebase.analytics
    }


    suspend fun signInAuthentication(email: String, password: String): FirebaseUser? {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
        return firebaseAuth.currentUser ?: throw FirebaseAuthException("", "")
    }

    fun startAnalytics(): FirebaseAnalytics {
        return analytics
    }
}