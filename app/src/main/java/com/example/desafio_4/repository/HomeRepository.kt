package com.example.desafio_4.repository

import com.example.desafio_4.utils.Constants.Firebase.DATABASE_GAMES
import com.example.desafio_4.utils.Constants.Firebase.DATABASE_USERS
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class HomeRepository {
    private val db by lazy {
        Firebase.firestore.collection(DATABASE_USERS).document(firebaseAuth.currentUser?.uid ?: "")
    }
    private val firebaseAuth by lazy {
        Firebase.auth
    }

    suspend fun getGameCollection(): QuerySnapshot {
        return db.collection(DATABASE_GAMES).get().await()
    }

    fun signOut() {
        firebaseAuth.signOut()
    }
}