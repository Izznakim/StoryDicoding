package com.example.storydicoding.ui.welcomepage.signup

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.storydicoding.ViewModelFactory
import com.example.storydicoding.databinding.ActivitySignupBinding
import com.example.storydicoding.data.model.User
import com.example.storydicoding.data.model.UserPreference
import com.example.storydicoding.ui.welcomepage.WelcomeActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var signupViewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WelcomeActivity.setupView(window, supportActionBar)
        setupViewModel()
        setupAction()
    }

    private fun setupViewModel() {
        signupViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[SignupViewModel::class.java]
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
                        signupViewModel.saveUser(User(name, email, password, false))
                        signupViewModel.registerUser(User(name, email, password, false))
                        signupViewModel.message.observe(this@SignupActivity){
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