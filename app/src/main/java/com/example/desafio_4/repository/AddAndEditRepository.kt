package com.example.desafio_4.repository

import android.net.Uri
import com.example.desafio_4.model.Game
import com.example.desafio_4.utils.Constants
import com.example.desafio_4.utils.Constants.AdapterFields.ID
import com.example.desafio_4.utils.Constants.Firebase.DATABASE_GAMES
import com.example.desafio_4.utils.Constants.Firebase.DATABASE_USERS
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

class AddAndEditRepository {
    private val db by lazy {
        Firebase.firestore.collection(DATABASE_USERS).document(firebaseAuth.currentUser?.uid ?: "")
    }
    private val firebaseAuth by lazy {
        Firebase.auth
    }
    private val firebaseStorageRef by lazy {
        Firebase.storage.reference
    }

    suspend fun getCollectionSize(): Int {
        val collection = db.collection(DATABASE_GAMES).orderBy(ID).get().await()
        return collection.size()
    }

    suspend fun getGameById(id: Int): List<DocumentSnapshot>? {
        val query = db.collection(DATABASE_GAMES).whereEqualTo(ID, id).get().await()
        return query.documents
    }

    suspend fun getUriFromStorage(id: Int): Uri {
        return firebaseStorageRef.child("${(firebaseAuth.currentUser?.uid ?: "")}/${id}gamePhoto.jpg").downloadUrl.await()
    }

    suspend fun setGameValue(game: Game, id: Int) {
        db.collection(DATABASE_GAMES).document("$id").set(game, SetOptions.merge()).await()
    }
}