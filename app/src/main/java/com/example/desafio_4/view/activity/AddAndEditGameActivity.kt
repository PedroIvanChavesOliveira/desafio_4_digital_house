package com.example.desafio_4.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.desafio_4.databinding.ActivityAddAndEditGameBinding
import com.example.desafio_4.model.Game
import com.example.desafio_4.utils.Constants.Firebase.DATABASE_GAMES
import com.example.desafio_4.utils.Constants.Firebase.DATABASE_USERS
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddAndEditGameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddAndEditGameBinding
    private val db by lazy {
        Firebase.firestore.collection(DATABASE_USERS).document(firebaseAuth.currentUser?.uid ?: "")
    }
    private val firebaseAuth by lazy {
        Firebase.auth
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAndEditGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btSaveGame.isEnabled = true
        binding.btSaveGame.setOnClickListener {
            addNewGame()
        }
    }

    private fun addNewGame() {
        db.collection(DATABASE_GAMES).orderBy("id")
            .get()
            .addOnSuccessListener {
                val id = it.size()
                val game = Game(id,null, binding.tietDescriptionGame.text.toString(),
                    binding.tietReleaseDataGame.text.toString(),
                    binding.tietNameGame.text.toString()
                )

                db.collection(DATABASE_GAMES).document("$id")
                    .set(game)
                    .addOnSuccessListener {
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener{
                        Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
                    }
            }
            .addOnFailureListener{
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
            }
    }
}