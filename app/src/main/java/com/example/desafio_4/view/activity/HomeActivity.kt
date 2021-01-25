package com.example.desafio_4.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.desafio_4.adapter.GameAdapter
import com.example.desafio_4.databinding.ActivityHomeBinding
import com.example.desafio_4.utils.Constants.Firebase.DATABASE_GAMES
import com.example.desafio_4.utils.Constants.Firebase.DATABASE_USERS
import com.example.desafio_4.utils.Constants.GameList.ID_GAME
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val firebaseAuth by lazy {
        Firebase.auth
    }
    private val db by lazy {
        Firebase.firestore
    }

    val userRef = db.collection(DATABASE_USERS).document(firebaseAuth.currentUser?.uid ?: "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fabHome.setOnClickListener {
            val intent = Intent(this, AddAndEditGameActivity::class.java)
            startActivity(intent)
        }

        binding.btLogOut.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.rvHome.apply {
            layoutManager = GridLayoutManager(this@HomeActivity, 2)
            userRef.collection(DATABASE_GAMES).get()
                .addOnSuccessListener {
                    adapter = GameAdapter(it.documents) {
                        val intent = Intent(this@HomeActivity, GameDescriptionActivity::class.java)
                        intent.putExtra("id", it["id"].toString())
                        startActivity(intent)
                    }
                }
                .addOnFailureListener {  }
        }
    }
}