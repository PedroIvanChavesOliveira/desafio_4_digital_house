package com.example.desafio_4.view.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import androidx.core.content.edit
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.desafio_4.R
import com.example.desafio_4.utils.Constants.SharedPreferences.CHECKED_SHAREDPREFERENCE
import com.example.desafio_4.utils.Constants.SharedPreferences.EMAIL_SHAREDPREFERENCE
import com.example.desafio_4.utils.Constants.SharedPreferences.NAME_SHAREDPREFERENCE
import com.example.desafio_4.utils.Constants.SharedPreferences.PASSWORD_SHAREDPREFERENCE
import com.example.desafio_4.databinding.ActivityLoginBinding
import com.example.desafio_4.viewModel.LoginViewModel
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModelLogin: LoginViewModel
    private var sharedPreferences: SharedPreferences? = null
    private var emailCheck = false
    private var passwordCheck = false
    private var validationEmail = false
    private var textInputEmail = ""
    private var textInputPassword = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelLogin = ViewModelProvider(this).get(LoginViewModel::class.java)
        sharedPreferences = getSharedPreferences(NAME_SHAREDPREFERENCE, Context.MODE_PRIVATE)

        viewModelLogin.startAnalytics()

        validatingEditText(binding.tietEmail, binding.tilEmail, R.string.string_email)
        validatingEditText(binding.tietPassword, binding.tilPassword, R.string.string_password)

        binding.btRegister.setOnClickListener {
            viewModelLogin.startAnalytic.observe(this) {
                it.logEvent("register_button", null)
            }
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            sharedPreferences?.edit {
                putString(EMAIL_SHAREDPREFERENCE, textInputEmail)
                putString(PASSWORD_SHAREDPREFERENCE, textInputPassword)
            }
            finish()
        }
        binding.btLogIn.setOnClickListener {
            logInAccount(binding.tietEmail.text.toString(), binding.tietPassword.text.toString())
            viewModelLogin.startAnalytic.observe(this) {
                val bundle = Bundle()
                val method = null
                bundle.putString(FirebaseAnalytics.Param.METHOD, method)
                it.logEvent(FirebaseAnalytics.Event.LOGIN, bundle)
            }
            sharedPreferences?.edit {
                putString(EMAIL_SHAREDPREFERENCE, textInputEmail)
                putString(PASSWORD_SHAREDPREFERENCE, textInputPassword)
            }
        }

        binding.cbRemember.setOnCheckedChangeListener { _, isChecked ->
            viewModelLogin.startAnalytic.observe(this) {
                it.logEvent("remember_button", null)
            }
            sharedPreferences?.edit {
                putBoolean(CHECKED_SHAREDPREFERENCE, isChecked)
                putString(EMAIL_SHAREDPREFERENCE, textInputEmail)
                putString(PASSWORD_SHAREDPREFERENCE, textInputPassword)
            }
        }

        initComponents()
    }

    override fun onStart() {
        super.onStart()
        viewModelLogin.login.observe(this) {
            updateUI(it)
        }
    }

    private fun logInAccount(email: String, password: String) {
        if(!activatingButtonLogin()){
            return
        }

        viewModelLogin.signInAuthentication(email, password)
        viewModelLogin.login.observe(this) {
            updateUI(it)
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if(user != null) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun initComponents() {
        val checkBoxRemember = sharedPreferences?.getBoolean(CHECKED_SHAREDPREFERENCE, false)
        val emailRemember = sharedPreferences?.getString(EMAIL_SHAREDPREFERENCE, "")
        val passwordRemember = sharedPreferences?.getString(PASSWORD_SHAREDPREFERENCE, "")

        binding.cbRemember.isChecked = checkBoxRemember ?: false
        if(binding.cbRemember.isChecked) {
            binding.tietEmail.setText(emailRemember)
            binding.tietPassword.setText(passwordRemember)
        }
    }

    private fun validatingEditText(editText: EditText, textInput: TextInputLayout, errorString: Int) {
        editText.doOnTextChanged { text, _, _, _ ->
            if(text?.isBlank() == true) {
                textInput.error = getString(R.string.string_error_message, getString(errorString))
                getByTag(editText.tag as String, false)
            }else {
                textInput.isErrorEnabled = false
                getByTag(editText.tag as String, true)
            }

            if(editText.tag == getString(R.string.string_email)) {
                validatingEmail(text.toString())
            }

            when(editText.tag) {
                getString(R.string.string_email) -> {
                    textInputEmail = text.toString()
                }
                getString(R.string.string_password) -> {
                    textInputPassword = text.toString()
                }
            }
            activatingButtonLogin()
        }
    }

    private fun getByTag(tag: String, check: Boolean) {
        when(tag) {
            getString(R.string.string_email) -> emailCheck = check
            getString(R.string.string_password) -> passwordCheck = check
        }
    }

    private fun validatingEmail(email: String) {
        if(Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            validationEmail = true
            binding.tilEmail.isErrorEnabled = false
        }else {
            validationEmail = false
            binding.tilEmail.error = getString(R.string.validationEmail)
        }
    }

    private fun activatingButtonLogin(): Boolean {
        val isOk: Boolean
        if(emailCheck && passwordCheck) {
            binding.btLogIn.isEnabled = true
            isOk = true
        }else {
            binding.btLogIn.isEnabled = false
            isOk = false
        }
        return isOk
    }
}