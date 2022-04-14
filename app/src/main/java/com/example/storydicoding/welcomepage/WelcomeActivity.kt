package com.example.storydicoding.welcomepage

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.fragment.app.commit
import com.example.storydicoding.R
import com.example.storydicoding.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupFragment()
    }

    private fun setupView(){
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupFragment(){
        val mFragmentManager=supportFragmentManager
        val fragment=mFragmentManager.findFragmentByTag(WelcomeFragment::class.java.simpleName)

        if (fragment !is WelcomeFragment){
            mFragmentManager.commit {
                add(R.id.fragment_welcome_container, WelcomeFragment(), WelcomeFragment::class.java.simpleName)
            }
        }
    }
}