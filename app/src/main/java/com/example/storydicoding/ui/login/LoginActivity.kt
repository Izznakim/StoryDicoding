package com.example.storydicoding.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.storydicoding.ViewModelFactory
import com.example.storydicoding.databinding.ActivityLoginBinding
import com.example.storydicoding.data.model.User
import com.example.storydicoding.data.model.UserPreference
import com.example.storydicoding.ui.main.MainActivity
import com.example.storydicoding.ui.WelcomeActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WelcomeActivity.setupView(window,supportActionBar)
        setupViewModel()
        setupAction()
        playAnimation()
    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[LoginViewModel::class.java]
    }

    private fun setupAction(){
        binding.apply {
            btnLogin.setOnClickListener {
                val email=etEmail.text.toString()
                val password=etPassword.text.toString()
                when{
                    email.isEmpty()->etEmail.error="Masukkan email"
                    password.isEmpty()->etPassword.error="Masukkan password"
                    else->{
                        loginViewModel.loginUser(email, password)
                        loginViewModel.message.observe(this@LoginActivity){
                            AlertDialog.Builder(this@LoginActivity).apply{
                                setTitle("Login!")
                                setMessage(it)
                                if(loginViewModel.error.value==false) {
                                    setPositiveButton("Next") { _, _ ->
                                        Intent(this@LoginActivity, MainActivity::class.java).also {
                                            it.flags =
                                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                            startActivity(it)
                                            finish()
                                        }
                                    }
                                }else{
                                    setNegativeButton("Back") { _, _ ->}
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

    private fun playAnimation(){
        ObjectAnimator.ofFloat(binding.ivLogin, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.tvTitle, View.ALPHA, 1f).setDuration(500)
        val desc = ObjectAnimator.ofFloat(binding.tvDesc, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(500)
        val emailEdit = ObjectAnimator.ofFloat(binding.etEmail, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(500)
        val passwordEdit =
            ObjectAnimator.ofFloat(binding.etPassword, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)

        val togetherName = AnimatorSet().apply {
            playTogether(title, desc)
        }
        val togetherEmail = AnimatorSet().apply {
            playTogether(email, emailEdit)
        }
        val togetherPassword = AnimatorSet().apply {
            playTogether(password, passwordEdit)
        }

        AnimatorSet().apply {
            playSequentially(title,
                togetherName,
                togetherEmail,
                togetherPassword, login
            )
            start()
        }
    }
}