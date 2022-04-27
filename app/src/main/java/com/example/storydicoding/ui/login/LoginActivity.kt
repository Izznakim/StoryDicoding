package com.example.storydicoding.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.storydicoding.R
import com.example.storydicoding.ViewModelFactory
import com.example.storydicoding.data.model.UserPreference
import com.example.storydicoding.databinding.ActivityLoginBinding
import com.example.storydicoding.setupViewWelcomePage
import com.example.storydicoding.ui.WelcomeActivity
import com.example.storydicoding.ui.main.MainActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewWelcomePage(window, supportActionBar)
        setupViewModel()
        setupAction()
        playAnimation()
    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(this,UserPreference.getInstance(dataStore))
        )[LoginViewModel::class.java]

        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun setupAction() {
        binding.apply {
            btnLogin.setOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                when {
                    email.isEmpty() -> etEmail.error = getString(R.string.error_email_empty)
                    password.isEmpty() -> etPassword.error =
                        getString(R.string.error_password_empty)
                    else -> {
                        loginViewModel.loginUser(email, password)
                        loginViewModel.error.observe(this@LoginActivity) {
                            AlertDialog.Builder(this@LoginActivity).apply {
                                val message: String
                                setTitle(getString(R.string.login_alert_dialog))
                                if (!it) {
                                    message = getString(R.string.message_login_success)
                                    setMessage(message)
                                    setPositiveButton(getString(R.string.next)) { _, _ ->
                                        Intent(this@LoginActivity, MainActivity::class.java).also {
                                            it.flags =
                                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                            startActivity(it)
                                            finish()
                                        }
                                    }
                                } else {
                                    message = getString(R.string.message_fail_login)
                                    setMessage(message)
                                    setNegativeButton(getString(R.string.back)) { _, _ -> }
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

    private fun playAnimation() {
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

        val togetherTitle = AnimatorSet().apply {
            playTogether(title, desc)
        }
        val togetherEmail = AnimatorSet().apply {
            playTogether(email, emailEdit)
        }
        val togetherPassword = AnimatorSet().apply {
            playTogether(password, passwordEdit)
        }

        AnimatorSet().apply {
            playSequentially(
                togetherTitle,
                togetherEmail,
                togetherPassword, login
            )
            start()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}