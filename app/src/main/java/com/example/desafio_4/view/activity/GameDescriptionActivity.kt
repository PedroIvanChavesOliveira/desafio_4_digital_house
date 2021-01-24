package com.example.desafio_4.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.desafio_4.R
import com.example.desafio_4.databinding.ActivityGameDescriptionBinding

class GameDescriptionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameDescriptionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}