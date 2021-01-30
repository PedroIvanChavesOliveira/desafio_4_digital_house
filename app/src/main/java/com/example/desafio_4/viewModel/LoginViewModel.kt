package com.example.desafio_4.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.desafio_4.business.LoginBusiness
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {
    var login: MutableLiveData<FirebaseUser?> = MutableLiveData()
    private val business by lazy {
        LoginBusiness()
    }

    fun signInAuthentication(email: String, password: String) {
        viewModelScope.launch {
            business.signInAuthentication(email, password)?.let {
                login.postValue(it)
            }?: run {
                login.postValue(null)
            }
        }
    }
}