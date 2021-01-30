package com.example.desafio_4.business

import android.net.Uri
import com.example.desafio_4.model.Game
import com.example.desafio_4.repository.AddAndEditRepository
import com.example.desafio_4.utils.Constants
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class AddAndEditBusiness {
    private val repository by lazy {
        AddAndEditRepository()
    }

    suspend fun getCollectionSize(): Int {
        return repository.getCollectionSize()
    }

    suspend fun getGameById(id: Int): List<DocumentSnapshot>? {
        repository.getGameById(id)?.let {
            return it
        } ?: run {
            return null
        }
    }

    suspend fun getUriFromStorage(id: Int): Uri {
        return repository.getUriFromStorage(id)
    }

    suspend fun setGameValue(game: Game, id: Int) {
        repository.setGameValue(game, id)
    }
}