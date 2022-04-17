package com.example.storydicoding.ui

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        binding=ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView(window,supportActionBar)
        setupAction()
        supportActionBar?.hide()
    }

    private fun setupAction(){
        binding.apply {
            btnLogin.setOnClickListener {
                startActivity(Intent(this@WelcomeActivity, LoginActivity::class.java))
            }
            btnSignup.setOnClickListener {
                startActivity(Intent(this@WelcomeActivity, SignupActivity::class.java))
            }
        }
    }

    companion object{
        fun setupView(window: Window, actBar:ActionBar?){
            @Suppress("DEPRECATION")
            if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.R){
                window.insetsController?.hide(WindowInsets.Type.statusBars())
            }else{
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
                )
            }
            actBar?.hide()
        }
    }
}