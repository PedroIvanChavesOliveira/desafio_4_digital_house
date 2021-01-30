package com.example.desafio_4.viewModel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.desafio_4.business.AddAndEditBusiness
import com.example.desafio_4.model.Game
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.launch

class AddAndEditViewModel: ViewModel() {
    private val business by lazy {
        AddAndEditBusiness()
    }
    var collectionSize: MutableLiveData<Int> = MutableLiveData()
    var uri: MutableLiveData<Uri> = MutableLiveData()
    var onSucess: MutableLiveData<Boolean> = MutableLiveData()
    var document: MutableLiveData<List<DocumentSnapshot>> = MutableLiveData()
    var onFailure: MutableLiveData<Boolean> = MutableLiveData()

    fun getCollectionSize() {
        viewModelScope.launch {
            collectionSize.postValue(business.getCollectionSize())
        }
    }

    fun getUriFromStorage(id: Int) {
        viewModelScope.launch {
            uri.postValue(business.getUriFromStorage(id))
        }
    }

    fun setGameValue(game: Game, id: Int) {
        viewModelScope.launch {
            business.setGameValue(game, id).let {
                onSucess.postValue(true)
            }
        }
    }

    fun getGameById(id: Int) {
        viewModelScope.launch {
            business.getGameById(id)?.let {
                document.postValue(it)
            }?: run {
                document.postValue(null)
            }
        }
    }

}