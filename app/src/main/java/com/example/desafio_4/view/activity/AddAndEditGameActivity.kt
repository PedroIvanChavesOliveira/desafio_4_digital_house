package com.example.desafio_4.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.desafio_4.R
import com.example.desafio_4.databinding.ActivityAddAndEditGameBinding

class AddAndEditGameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddAndEditGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAndEditGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}