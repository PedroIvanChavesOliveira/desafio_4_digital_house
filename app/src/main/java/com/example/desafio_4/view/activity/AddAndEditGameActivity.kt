package com.example.desafio_4.view.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.example.desafio_4.databinding.ActivityAddAndEditGameBinding
import com.example.desafio_4.model.Game
import com.example.desafio_4.utils.Constants.AdapterFields.DESCRIPTION
import com.example.desafio_4.utils.Constants.AdapterFields.ID
import com.example.desafio_4.utils.Constants.AdapterFields.PHOTO
import com.example.desafio_4.utils.Constants.AdapterFields.RELEASE
import com.example.desafio_4.utils.Constants.AdapterFields.TITLE
import com.example.desafio_4.utils.Constants.Firebase.DATABASE_GAMES
import com.example.desafio_4.utils.Constants.Firebase.DATABASE_USERS
import com.example.desafio_4.utils.Constants.Firebase.ID_GAME
import com.example.desafio_4.utils.Constants.Firebase.ORIGIN_INTENT
import com.example.desafio_4.viewModel.AddAndEditViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File

class AddAndEditGameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddAndEditGameBinding
    private lateinit var viewModelAddAndEdit: AddAndEditViewModel
    var getId = ""
    var getOrigin = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAndEditGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelAddAndEdit = ViewModelProvider(this).get(AddAndEditViewModel::class.java)
        getId = intent.getStringExtra(ID_GAME).toString()
        getOrigin = intent.getIntExtra(ORIGIN_INTENT, 0)

        if(getOrigin == 2 || getOrigin == 3) {
            setUpGameInfo()
        }

        binding.btSaveGame.isEnabled = true
        binding.btSaveGame.setOnClickListener {
            when(getOrigin) {
                1 -> addNewGame()
                2 -> editGame()
                3 -> fromCamera()
            }
        }

        binding.ivGamePhoto.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            intent.putExtra(ID_GAME, getId)
            intent.putExtra(ORIGIN_INTENT, getOrigin)
            startActivity(intent)
        }
    }

    private fun fromCamera() {
        viewModelAddAndEdit.getUriFromStorage(getId.toInt())
        viewModelAddAndEdit.uri.observe(this) {uri ->
            val game = Game(getId.toInt(), uri.toString(), binding.tietDescriptionGame.text.toString(),
                    binding.tietReleaseDataGame.text.toString(),
                    binding.tietNameGame.text.toString()
            )

            viewModelAddAndEdit.setGameValue(game, getId.toInt())
            viewModelAddAndEdit.onSucess.observe(this) {
                if(it) {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun addNewGame() {
        viewModelAddAndEdit.getCollectionSize()
        viewModelAddAndEdit.collectionSize.observe(this) {id ->
            viewModelAddAndEdit.getUriFromStorage(id)
            viewModelAddAndEdit.uri.observe(this) {uri ->
                val game = Game(id, uri.toString(), binding.tietDescriptionGame.text.toString(),
                        binding.tietReleaseDataGame.text.toString(),
                        binding.tietNameGame.text.toString()
                    )

                viewModelAddAndEdit.setGameValue(game, id)
                viewModelAddAndEdit.onSucess.observe(this) {
                    if(it) {
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }

    private fun editGame() {
        viewModelAddAndEdit.getUriFromStorage(getId.toInt())
        viewModelAddAndEdit.uri.observe(this) {uri ->
            val game = Game(getId.toInt(), uri.toString(), binding.tietDescriptionGame.text.toString(),
                    binding.tietReleaseDataGame.text.toString(),
                    binding.tietNameGame.text.toString()
            )
            viewModelAddAndEdit.setGameValue(game, getId.toInt())
            viewModelAddAndEdit.onSucess.observe(this) {
                if(it) {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun setUpGameInfo() {
        viewModelAddAndEdit.getGameById(getId.toInt())
        viewModelAddAndEdit.document.observe(this) {
            it.forEach { doc ->
                binding.tietNameGame.setText(doc[TITLE].toString())
                binding.tietReleaseDataGame.setText(doc[RELEASE].toString())
                binding.tietDescriptionGame.setText(doc[DESCRIPTION].toString())
                Glide.with(this).load(doc[PHOTO]).into(binding.ivGamePhoto)
            }
        }
    }
}