package com.example.desafio_4.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.example.desafio_4.databinding.ActivityGameDescriptionBinding
import com.example.desafio_4.utils.Constants.AdapterFields.DESCRIPTION
import com.example.desafio_4.utils.Constants.AdapterFields.PHOTO
import com.example.desafio_4.utils.Constants.AdapterFields.RELEASE
import com.example.desafio_4.utils.Constants.AdapterFields.TITLE
import com.example.desafio_4.utils.Constants.Firebase.ID_GAME
import com.example.desafio_4.utils.Constants.Firebase.ORIGIN_INTENT
import com.example.desafio_4.viewModel.GameDescriptionViewModel

class GameDescriptionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameDescriptionBinding
    private lateinit var viewModelDescription: GameDescriptionViewModel
    private var getId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getId = intent.getStringExtra(ID_GAME).toString()
        viewModelDescription = ViewModelProvider(this).get(GameDescriptionViewModel::class.java)

        startInfos()

        binding.fabGameDescription.setOnClickListener {
            val intent = Intent(this, AddAndEditGameActivity::class.java)
            intent.putExtra(ID_GAME, getId)
            intent.putExtra(ORIGIN_INTENT, 2)
            startActivity(intent)
        }

        binding.ivArrowBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun startInfos() {
        viewModelDescription.getDocumentById(getId.toString())
        viewModelDescription.gameDescription.observe(this) {doc ->
            binding.tvGameName.text = doc[TITLE].toString()
                binding.tvGameNameTitle.text = doc[TITLE].toString()
                binding.tvGameRelease.text = doc[RELEASE].toString()
                binding.tvGameDescription.text = doc[DESCRIPTION].toString()
                Glide.with(this@GameDescriptionActivity).load(doc[PHOTO]).into(binding.ivGamePoster)
        }
    }
}