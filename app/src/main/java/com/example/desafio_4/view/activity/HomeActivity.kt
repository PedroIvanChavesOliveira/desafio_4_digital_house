package com.example.desafio_4.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import com.example.desafio_4.adapter.GameAdapter
import com.example.desafio_4.databinding.ActivityHomeBinding
import com.example.desafio_4.utils.Constants.AdapterFields.ID
import com.example.desafio_4.utils.Constants.AdapterFields.TITLE
import com.example.desafio_4.utils.Constants.Firebase.ID_GAME
import com.example.desafio_4.utils.Constants.Firebase.ORIGIN_INTENT
import com.example.desafio_4.utils.Constants.VoiceRecognation.REQUEST_CODE
import com.example.desafio_4.viewModel.HomeViewModel
import com.google.firebase.firestore.DocumentSnapshot

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModelHome: HomeViewModel
    var start = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelHome = ViewModelProvider(this).get(HomeViewModel::class.java)

        binding.fabHome.setOnClickListener {
            val intent = Intent(this, AddAndEditGameActivity::class.java)
            intent.putExtra(ORIGIN_INTENT, 1)
            startActivity(intent)
        }

        binding.btLogOut.setOnClickListener {
            viewModelHome.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.tilSearchGame.setEndIconOnClickListener {
            startVoiceCall()
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
            viewModelHome.getGameCollection()
            viewModelHome.docList.observe(this@HomeActivity) {
                adapter = GameAdapter(filteredList(it, text)) {doc ->
                    val intent = Intent(this@HomeActivity, GameDescriptionActivity::class.java)
                    intent.putExtra(ID_GAME, doc[ID].toString())
                    startActivity(intent)
                }
            }
        }
    }

    private fun filteredList(doc: List<DocumentSnapshot>, text: String?): List<DocumentSnapshot> {
        val filteredList = ArrayList<DocumentSnapshot>()
        return if(text.isNullOrBlank()) {
            doc
        }else {
            for(i in doc) {
                if(i[TITLE].toString().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(i)
                }
            }
            filteredList
        }
    }

    private fun startVoiceCall() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Voice searching...")
        startActivityForResult(intent, REQUEST_CODE)
    }
}