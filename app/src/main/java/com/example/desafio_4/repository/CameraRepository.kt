package com.example.desafio_4.repository

import android.net.Uri
import com.example.desafio_4.model.Game
import com.example.desafio_4.utils.Constants
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

class CameraRepository {
    private val firebaseStorageRef by lazy {
        Firebase.storage.reference
    }
    private val firebaseAuth by lazy {
        Firebase.auth
    }
    private val db by lazy {
        Firebase.firestore.collection(Constants.Firebase.DATABASE_USERS).document(firebaseAuth.currentUser?.uid ?: "")
    }

    suspend fun getCollectionSize(): Int {
        val collection = db.collection(Constants.Firebase.DATABASE_GAMES).orderBy(Constants.AdapterFields.ID).get().await()
        return collection.size()
    }

    suspend fun putFileToStorage(uri: Uri, id: Int): Uri {
        firebaseStorageRef.child("${(firebaseAuth.currentUser?.uid ?: "")}/${id}gamePhoto.jpg").putFile(uri).await()
        return firebaseStorageRef.child("${(firebaseAuth.currentUser?.uid ?: "")}/${id}gamePhoto.jpg").downloadUrl.await()
    }

    suspend fun setGameValue(game: Game, id: Int) {
        db.collection(Constants.Firebase.DATABASE_GAMES).document("$id").set(game, SetOptions.merge()).await()
    }

    suspend fun setPhotoValue(photo: HashMap<String, String>, id: Int) {
        db.collection(Constants.Firebase.DATABASE_GAMES).document("$id").set(photo, SetOptions.merge()).await()
    }
}