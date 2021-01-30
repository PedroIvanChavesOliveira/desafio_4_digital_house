package com.example.desafio_4.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.desafio_4.business.GameDescriptionBusiness
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.launch

class GameDescriptionViewModel: ViewModel() {
    private val business by lazy {
        GameDescriptionBusiness()
    }
    val gameDescription: MutableLiveData<DocumentSnapshot> = MutableLiveData()

    fun getDocumentById(id: String) {
        viewModelScope.launch {
            gameDescription.postValue(business.getDocumentById(id))
        }
    }
}