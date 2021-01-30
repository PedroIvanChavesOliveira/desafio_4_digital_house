package com.example.desafio_4.repository

import com.example.desafio_4.utils.Constants
import com.example.desafio_4.utils.Constants.Firebase.DATABASE_GAMES
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class GameDescriptionRepository {
    private val db by lazy {
        Firebase.firestore.collection(Constants.Firebase.DATABASE_USERS).document(firebaseAuth.currentUser?.uid ?: "")
            .collection(DATABASE_GAMES)
    }
    private val firebaseAuth by lazy {
        Firebase.auth
    }

    suspend fun getDocumentById(id: String): DocumentSnapshot {
        return db.document(id).get().await()
    }
}