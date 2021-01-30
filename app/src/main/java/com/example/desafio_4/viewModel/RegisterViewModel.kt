package com.example.desafio_4.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.desafio_4.business.RegisterBusiness
import com.example.desafio_4.repository.RegisterRepository
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class RegisterViewModel: ViewModel() {
    val newUser: MutableLiveData<FirebaseUser> = MutableLiveData()
    private val business by lazy {
        RegisterBusiness()
    }

    fun createNewUser(email: String, password: String, user: HashMap<String, String>) {
        viewModelScope.launch {
            try {
                business.createNewUser(email, password, user)?.let {
                    newUser.postValue(it)
                } ?: run {
                    newUser.postValue(null)
                }
            }catch(e: FirebaseException) {
                newUser.postValue(null)
            }
        }
    }
}