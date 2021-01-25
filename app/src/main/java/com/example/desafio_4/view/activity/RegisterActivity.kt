package com.example.desafio_4.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.example.desafio_4.R
import com.example.desafio_4.databinding.ActivityRegisterBinding
import com.example.desafio_4.utils.Constants.Firebase.DATABASE_GAMES
import com.example.desafio_4.utils.Constants.Firebase.DATABASE_USERS
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val firebaseAuth by lazy {
        Firebase.auth
    }
    private val db by lazy {
        Firebase.firestore
    }
    private var checkEmail = false
    private var checkName = false
    private var checkPassword = false
    private var checkConfirmPassword = false
    private var validationEmail = false
    private var nameText = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        validatingEditText(binding.tietNameRegister, binding.tilNameRegister, R.string.string_name)
        validatingEditText(binding.tietEmailRegister, binding.tilEmailRegister, R.string.string_email)
        validatingEditText(binding.tietPasswordRegister, binding.tilPasswordRegister, R.string.string_password)
        validatingEditText(binding.tietConfirmPasswordRegister, binding.tilConfirmPasswordRegister, R.string.string_confirm_password)

        binding.btRegister.setOnClickListener {
            createAccount(binding.tietEmailRegister.text.toString(), binding.tietPasswordRegister.text.toString())
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        val user = firebaseAuth.currentUser
        updateUI(user)
    }

    private fun setUpUser() {
        val user = hashMapOf(
            "name" to binding.tietNameRegister.text.toString(),
            "email" to binding.tietEmailRegister.text.toString()
        )

        db.collection(DATABASE_USERS)
            .document(firebaseAuth.currentUser?.uid ?: "")
            .set(user, SetOptions.merge())
            .addOnSuccessListener {}
            .addOnFailureListener{
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
            }
    }

    private fun createAccount(email: String, password: String) {
        if(!activatingButtonRegister()){
            return
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    updateUI(user)
                    setUpUser()
                } else {
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if(user != null) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validatingEditText(editText: EditText, textInput: TextInputLayout, errorString: Int) {
        editText.doOnTextChanged { text, start, _, _ ->
            if(text?.isBlank() == true) {
                textInput.error = getString(R.string.string_error_message, getString(errorString))
                getByTag(editText.tag as String, false)
            }else {
                textInput.isErrorEnabled = false
                getByTag(editText.tag as String, true)
            }

            when(editText.tag) {
                getString(R.string.string_email) -> validatingEmail(text.toString())
                getString(R.string.string_name) -> nameText = text.toString()
                getString(R.string.string_password) -> {
                    password = text.toString()
                    passwordValidation(start+1)
                }
                getString(R.string.string_confirm_password) -> confirmPasswordValidation(password, text.toString())
            }

            activatingButtonRegister()
        }
    }

    private fun getByTag(tag: String, check: Boolean) {
        when(tag) {
            getString(R.string.string_email) -> checkEmail = check
            getString(R.string.string_password) -> checkPassword = check
            getString(R.string.string_name) -> checkName = check
            getString(R.string.string_confirm_password) -> checkConfirmPassword = check
        }
    }

    private fun validatingEmail(email: String) {
        if(Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            validationEmail = true
            binding.tilEmailRegister.isErrorEnabled = false
        }else {
            validationEmail = false
            binding.tilEmailRegister.error = getString(R.string.validationEmail)
        }
    }

    private fun passwordValidation(password: Int) {
        if(password >= 6) {
            checkPassword = true
            binding.tilPasswordRegister.isErrorEnabled = false
        }else {
            checkPassword = false
            binding.tietPasswordRegister.error = getString(R.string.validationPassword)
        }
    }

    private fun confirmPasswordValidation(password: String, confirmPassword: String) {
        if(password.isNotBlank() && password == confirmPassword) {
            checkConfirmPassword = true
            binding.tilConfirmPasswordRegister.isErrorEnabled = false
        }else {
            checkConfirmPassword = false
            binding.tilConfirmPasswordRegister.error = getString(R.string.validationConfirmPassword)
        }
    }

    private fun activatingButtonRegister(): Boolean {
        val isOk: Boolean
        if(checkEmail && checkPassword && checkName && checkConfirmPassword) {
            binding.btRegister.isEnabled = true
            isOk = true
        }else {
            binding.btRegister.isEnabled = false
            isOk = false
        }
        return isOk
    }
}