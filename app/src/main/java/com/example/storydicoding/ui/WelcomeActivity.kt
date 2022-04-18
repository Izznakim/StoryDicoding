package com.example.storydicoding.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.ActionBar
import com.example.storydicoding.databinding.ActivityWelcomeBinding
import com.example.storydicoding.ui.login.LoginActivity
import com.example.storydicoding.ui.signup.SignupActivity

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView(window, supportActionBar)
        setupAction()
        playAnimation()
    }

    private fun setupAction() {
        binding.apply {
            btnLogin.setOnClickListener {
                startActivity(Intent(this@WelcomeActivity, LoginActivity::class.java))
            }
            btnSignup.setOnClickListener {
                startActivity(Intent(this@WelcomeActivity, SignupActivity::class.java))
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ivWelcome, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.tvTitle, View.ALPHA, 1f).setDuration(500)
        val desc = ObjectAnimator.ofFloat(binding.tvDesc, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.btnSignup, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(login, signup)
        }

        AnimatorSet().apply {
            playSequentially(title, desc, together)
            start()
        }
    }

    companion object {
        fun setupView(window: Window, actBar: ActionBar?) {
            @Suppress("DEPRECATION")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.insetsController?.hide(WindowInsets.Type.statusBars())
            } else {
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
                )
            }
            actBar?.hide()
        }
    }
}