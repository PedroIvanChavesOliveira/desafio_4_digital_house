package com.example.desafio_4.business

import com.example.desafio_4.repository.HomeRepository
import com.google.firebase.firestore.DocumentSnapshot

class HomeBusiness() {
    private val repository by lazy {
        HomeRepository()
    }

    suspend fun getGameCollection(): MutableList<DocumentSnapshot> {
        return repository.getGameCollection().documents
    }

    fun signOut() {
        repository.signOut()
    }
}