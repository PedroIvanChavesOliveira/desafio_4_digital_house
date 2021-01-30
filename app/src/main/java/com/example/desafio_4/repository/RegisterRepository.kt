package com.example.desafio_4.repository

import com.example.desafio_4.utils.Constants
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class RegisterRepository {
    private val firebaseAuth by lazy {
        Firebase.auth
    }
    private val db by lazy {
        Firebase.firestore.collection(Constants.Firebase.DATABASE_USERS)
            .document(firebaseAuth.currentUser?.uid ?: "")
    }

    suspend fun createNewUser(email: String, password: String, user: HashMap<String, String>): FirebaseUser? {
        firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        db.set(user, SetOptions.merge()).await()
        return firebaseAuth.currentUser ?: throw FirebaseAuthException("", "")
    }
}