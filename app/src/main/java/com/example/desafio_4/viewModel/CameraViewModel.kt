package com.example.desafio_4.viewModel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.desafio_4.business.CameraBusiness
import com.example.desafio_4.model.Game
import kotlinx.coroutines.launch

class CameraViewModel: ViewModel() {
    var collectionSize: MutableLiveData<Int> = MutableLiveData()
    var getUri: MutableLiveData<Uri> = MutableLiveData()
    var onSucess: MutableLiveData<Boolean> = MutableLiveData()
    private val business by lazy {
        CameraBusiness()
    }

    fun getCollectionSize() {
        viewModelScope.launch {
            collectionSize.postValue(business.getCollectionSize())
        }
    }

    fun putFileToStorage(uri: Uri, id: Int) {
        viewModelScope.launch {
           getUri.postValue(business.putFileToStorage(uri, id))
        }
    }

    fun setGameValue(game: Game, id: Int) {
        viewModelScope.launch {
            business.setGameValue(game, id).let {
                onSucess.postValue(true)
            }
        }
    }

    fun setPhotoValue(photo: HashMap<String, String>, id: Int) {
        viewModelScope.launch {
            business.setPhotoValue(photo, id).let {
                onSucess.postValue(true)
            }
        }
    }
}