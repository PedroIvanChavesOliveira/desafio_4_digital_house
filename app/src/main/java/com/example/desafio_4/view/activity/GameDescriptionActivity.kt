package com.example.desafio_4.view.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.desafio_4.R
import com.example.desafio_4.databinding.ActivityGameDescriptionBinding
import com.example.desafio_4.utils.Constants
import com.example.desafio_4.utils.Constants.AdapterFields.DESCRIPTION
import com.example.desafio_4.utils.Constants.AdapterFields.PHOTO
import com.example.desafio_4.utils.Constants.AdapterFields.RELEASE
import com.example.desafio_4.utils.Constants.AdapterFields.TITLE
import com.example.desafio_4.utils.Constants.Firebase.DATABASE_GAMES
import com.example.desafio_4.utils.Constants.Firebase.ID_GAME
import com.example.desafio_4.utils.Constants.Firebase.ORIGIN_INTENT
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File

class GameDescriptionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameDescriptionBinding
    private var getId = ""
    private val firebaseAuth by lazy {
        Firebase.auth
    }
    private val db by lazy {
        Firebase.firestore
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getId = intent.getStringExtra(ID_GAME).toString()
        startInfos()

        binding.fabGameDescription.setOnClickListener {
            val intent = Intent(this, AddAndEditGameActivity::class.java)
            intent.putExtra(ID_GAME, getId)
            intent.putExtra(ORIGIN_INTENT, 2)
            startActivity(intent)
        }

        binding.ivArrowBack.setOnClickListener {
            finish()
        }
    }

    private fun startInfos() {
         db.collection(Constants.Firebase.DATABASE_USERS)
            .document(firebaseAuth.currentUser?.uid ?: "")
            .collection(DATABASE_GAMES).document(getId).get()
            .addOnSuccessListener {doc ->
                binding.tvGameName.text = doc[TITLE].toString()
                binding.tvGameNameTitle.text = doc[TITLE].toString()
                binding.tvGameRelease.text = doc[RELEASE].toString()
                binding.tvGameDescription.text = doc[DESCRIPTION].toString()
                Glide.with(this).load(doc[PHOTO]).into(binding.ivGamePoster)
            }.addOnFailureListener {  }
    }
}