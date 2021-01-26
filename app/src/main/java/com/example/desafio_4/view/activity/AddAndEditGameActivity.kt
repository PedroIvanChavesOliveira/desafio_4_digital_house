package com.example.desafio_4.view.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.net.toUri
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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File

class AddAndEditGameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddAndEditGameBinding
    private val db by lazy {
        Firebase.firestore.collection(DATABASE_USERS).document(firebaseAuth.currentUser?.uid ?: "")
    }
    private val firebaseAuth by lazy {
        Firebase.auth
    }
    private val firebaseStorageRef by lazy {
        Firebase.storage.reference
    }
    var getId = ""
    var getOrigin = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAndEditGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getId = intent.getStringExtra(ID_GAME).toString()
        getOrigin = intent.getIntExtra(ORIGIN_INTENT, 0)

        if(getOrigin == 2) {
            setUpGameInfo()
        }

        binding.btSaveGame.isEnabled = true
        binding.btSaveGame.setOnClickListener {
            when(getOrigin) {
                1 -> addNewGame()
                2 -> editGame()
            }
        }

        binding.ivGamePhoto.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            intent.putExtra(ID_GAME, getId)
            intent.putExtra(ORIGIN_INTENT, getOrigin)
            startActivity(intent)
        }
    }

    private fun addNewGame() {
        db.collection(DATABASE_GAMES).orderBy(ID)
            .get()
            .addOnSuccessListener {
                val id = it.size()
                firebaseStorageRef.child(
                    "${(firebaseAuth.currentUser?.uid ?: "")}/${id}gamePhoto.jpg"
                ).downloadUrl.addOnSuccessListener {uri ->
                    val game = Game(id, uri.toString(), binding.tietDescriptionGame.text.toString(),
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
            }
            .addOnFailureListener{
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
            }
    }

    private fun editGame() {
        db.collection(DATABASE_GAMES).whereEqualTo(ID, getId.toInt())
            .get()
            .addOnSuccessListener {
                val id = getId.toInt()
                firebaseStorageRef.child(
                    "${(firebaseAuth.currentUser?.uid ?: "")}/${id}gamePhoto.jpg"
                ).downloadUrl.addOnSuccessListener { uri ->
                    val game = Game(id, uri.toString(), binding.tietDescriptionGame.text.toString(),
                        binding.tietReleaseDataGame.text.toString(),
                        binding.tietNameGame.text.toString()
                    )

                    db.collection(DATABASE_GAMES).document("$id")
                        .set(game, SetOptions.merge())
                        .addOnSuccessListener {
                            val intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                        }
                        .addOnFailureListener{
                            Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
                        }
                }.addOnFailureListener {  }
            }
            .addOnFailureListener{
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
            }
    }

    private fun setUpGameInfo() {
        db.collection(DATABASE_GAMES).whereEqualTo(ID, getId.toInt())
            .get()
            .addOnSuccessListener {
                it.documents.forEach { doc ->
                    binding.tietNameGame.setText(doc[TITLE].toString())
                    binding.tietReleaseDataGame.setText(doc[RELEASE].toString())
                    binding.tietDescriptionGame.setText(doc[DESCRIPTION].toString())
                    Glide.with(this).load(doc[PHOTO]).into(binding.ivGamePhoto)
                }
            }.addOnFailureListener {  }
    }
}