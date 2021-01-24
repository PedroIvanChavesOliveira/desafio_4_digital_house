package com.example.desafio_4.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.desafio_4.R
import com.example.desafio_4.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}