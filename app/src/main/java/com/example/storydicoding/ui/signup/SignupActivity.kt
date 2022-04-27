package com.example.storydicoding.ui.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.storydicoding.R
import com.example.storydicoding.databinding.ActivitySignupBinding
import com.example.storydicoding.setupViewWelcomePage
import com.example.storydicoding.ui.WelcomeActivity

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val signupViewModel by viewModels<SignupViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewWelcomePage(window, supportActionBar)
        setupAction()
        setupProgressBar()
        playAnimation()
    }

    private fun setupAction() {
        binding.apply {
            btnSignup.setOnClickListener {
                val name = etName.text.toString()
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                when {
                    name.isEmpty() -> etName.error = getString(R.string.error_input_name)
                    email.isEmpty() -> etEmail.error = getString(R.string.error_input_email)
                    password.isEmpty() -> etPassword.error =
                        getString(R.string.error_input_password)
                    else -> {
                        signupViewModel.registerUser(name, email, password)
                            .observe(this@SignupActivity) {
                                AlertDialog.Builder(this@SignupActivity).apply {
                                    setTitle(getString(R.string.signup_alert_dialog))
                                    val message: String = if (!it) {
                                        getString(R.string.message_success_signup)
                                    } else {
                                        getString(R.string.message_fail_signup)
                                    }
                                    setMessage(message)
                                    setPositiveButton(getString(R.string.next)) { _, _ ->
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

    private fun setupProgressBar() {
        signupViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ivSignup, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.tvTitle, View.ALPHA, 1f).setDuration(500)
        val name = ObjectAnimator.ofFloat(binding.tvName, View.ALPHA, 1f).setDuration(500)
        val nameEdit = ObjectAnimator.ofFloat(binding.etName, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(500)
        val emailEdit = ObjectAnimator.ofFloat(binding.etEmail, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(500)
        val passwordEdit =
            ObjectAnimator.ofFloat(binding.etPassword, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.btnSignup, View.ALPHA, 1f).setDuration(500)

        val togetherName = AnimatorSet().apply {
            playTogether(name, nameEdit)
        }
        val togetherEmail = AnimatorSet().apply {
            playTogether(email, emailEdit)
        }
        val togetherPassword = AnimatorSet().apply {
            playTogether(password, passwordEdit)
        }

        AnimatorSet().apply {
            playSequentially(
                title,
                togetherName,
                togetherEmail,
                togetherPassword, signup
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