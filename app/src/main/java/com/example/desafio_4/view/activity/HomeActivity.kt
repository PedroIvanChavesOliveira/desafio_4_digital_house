package com.example.desafio_4.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.desafio_4.adapter.GameAdapter
import com.example.desafio_4.databinding.ActivityHomeBinding
import com.example.desafio_4.utils.Constants.AdapterFields.ID
import com.example.desafio_4.utils.Constants.AdapterFields.TITLE
import com.example.desafio_4.utils.Constants.Firebase.DATABASE_GAMES
import com.example.desafio_4.utils.Constants.Firebase.DATABASE_USERS
import com.example.desafio_4.utils.Constants.Firebase.ID_GAME
import com.example.desafio_4.utils.Constants.Firebase.ORIGIN_INTENT
import com.example.desafio_4.utils.Constants.VoiceRecognation.REQUEST_CODE
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val firebaseAuth by lazy {
        Firebase.auth
    }
    private val db by lazy {
        Firebase.firestore
    }
    val docList = mutableListOf<DocumentSnapshot>()
    var start = 0
    private val userRef = db.collection(DATABASE_USERS).document(firebaseAuth.currentUser?.uid ?: "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fabHome.setOnClickListener {
            val intent = Intent(this, AddAndEditGameActivity::class.java)
            intent.putExtra(ORIGIN_INTENT, 1)
            startActivity(intent)
        }

        binding.btLogOut.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.tilSearchGame.setEndIconOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Voice searching...")
            startActivityForResult(intent, REQUEST_CODE)
        }

        if(start == 0) {
            setUpRecyclerView(null)
            start++
        }
        searchGame()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val getString = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            getString?.let {
                if(getString.isNotEmpty()) {
                    val query = getString[0]
                    binding.tietSearchGame.setText(query.toLowerCase())
                }
            }
        }
    }

    private fun searchGame() {
        binding.tietSearchGame.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                setUpRecyclerView(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {  }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {  }

        })
    }

    private fun setUpRecyclerView(text: String?) {
        binding.rvHome.apply {
            layoutManager = GridLayoutManager(this@HomeActivity, 2)
            userRef.collection(DATABASE_GAMES).get()
                .addOnSuccessListener {
                    it.documents.forEach {doc ->
                        if(docList.size < it.size()) {
                            docList.add(doc)
                        }
                    }
                    val filteredList = ArrayList<DocumentSnapshot>()
                    if(text.isNullOrBlank()) {
                        adapter = GameAdapter(docList) {doc ->
                            val intent = Intent(this@HomeActivity, GameDescriptionActivity::class.java)
                            intent.putExtra(ID_GAME, doc[ID].toString())
                            startActivity(intent)
                        }
                    }else {
                        for(i in docList) {
                            if(i[TITLE].toString().toLowerCase().contains(text.toLowerCase())) {
                                filteredList.add(i)
                            }
                        }
                        adapter = GameAdapter(filteredList) {doc ->
                            val intent = Intent(this@HomeActivity, GameDescriptionActivity::class.java)
                            intent.putExtra(ID_GAME, doc[ID].toString())
                            startActivity(intent)
                        }
                    }
                }.addOnFailureListener {  }
        }
    }
}