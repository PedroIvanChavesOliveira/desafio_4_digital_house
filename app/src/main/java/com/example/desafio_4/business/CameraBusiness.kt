package com.example.desafio_4.business

import android.net.Uri
import com.example.desafio_4.model.Game
import com.example.desafio_4.repository.CameraRepository
import com.example.desafio_4.utils.Constants
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class CameraBusiness {
    private val repository by lazy {
        CameraRepository()
    }

    suspend fun getCollectionSize(): Int {
        return repository.getCollectionSize()
    }

    suspend fun putFileToStorage(uri: Uri, id: Int): Uri {
        return repository.putFileToStorage(uri, id)
    }

    suspend fun setGameValue(game: Game, id: Int) {
       repository.setGameValue(game, id)
    }

    suspend fun setPhotoValue(photo: HashMap<String, String>, id: Int) {
        repository.setPhotoValue(photo, id)
    }
}