package com.example.storydicoding.ui.signup

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.storydicoding.databinding.ActivitySignupBinding
import com.example.storydicoding.ui.WelcomeActivity

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val signupViewModel by viewModels<SignupViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WelcomeActivity.setupView(window, supportActionBar)
        setupAction()
    }

    private fun setupAction() {
        binding.apply {
            btnRegister.setOnClickListener {
                val name = etName.text.toString()
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                when {
                    name.isEmpty() -> etName.error = "Masukkan nama"
                    email.isEmpty() -> etEmail.error = "Masukkan email"
                    password.isEmpty() -> etPassword.error = "Masukkan password"
                    else -> {
                        signupViewModel.registerUser(name,email, password).observe(this@SignupActivity){
                            AlertDialog.Builder(this@SignupActivity).apply {
                                setTitle("SignUp!")
                                setMessage(it)
                                setPositiveButton("Next"){_,_->
                                    finish()
                                }
                                create()
                                show()
                            }
                        }
                    }
                }
            }
        }
    }
}