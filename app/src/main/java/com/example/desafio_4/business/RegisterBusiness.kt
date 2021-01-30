package com.example.desafio_4.business

import com.example.desafio_4.repository.RegisterRepository
import com.google.firebase.auth.FirebaseUser

class RegisterBusiness {
    private val repository by lazy {
        RegisterRepository()
    }

    suspend fun createNewUser(email: String, password: String, user: HashMap<String, String>): FirebaseUser? {
        repository.createNewUser(email, password, user)?.let {
            return it
        }?: run {
            return null
        }
    }
}