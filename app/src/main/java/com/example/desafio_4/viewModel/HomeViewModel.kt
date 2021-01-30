package com.example.desafio_4.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.desafio_4.business.HomeBusiness
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    private val business by lazy {
        HomeBusiness()
    }
    val docList: MutableLiveData<MutableList<DocumentSnapshot>> = MutableLiveData()

    fun getGameCollection() {
        viewModelScope.launch {
            docList.postValue(business.getGameCollection())
        }
    }

    fun signOut() {
        viewModelScope.launch {
            business.signOut()
        }
    }
}