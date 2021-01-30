package com.example.desafio_4.business

import com.example.desafio_4.repository.GameDescriptionRepository
import com.google.firebase.firestore.DocumentSnapshot

class GameDescriptionBusiness {
    private val repository by lazy {
        GameDescriptionRepository()
    }

    suspend fun getDocumentById(id: String): DocumentSnapshot {
        return repository.getDocumentById(id)
    }
}