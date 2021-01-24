package com.example.desafio_4.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.desafio_4.R
import com.example.desafio_4.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}