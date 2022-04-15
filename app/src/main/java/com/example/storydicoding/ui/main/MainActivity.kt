package com.example.storydicoding.ui.main

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.storydicoding.R
import com.example.storydicoding.ViewModelFactory
import com.example.storydicoding.databinding.ActivityMainBinding
import com.example.storydicoding.model.UserPreference
import com.example.storydicoding.ui.welcomepage.WelcomeActivity

private val Context.dataStore:DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
    }

    private fun setupView(){
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    private fun setupViewModel(){
        mainViewModel=ViewModelProvider(this,ViewModelFactory(UserPreference.getInstance(dataStore)))[MainViewModel::class.java]

        mainViewModel.getUser().observe(this){
            if (it.isLogin){
                binding.tvWelcomeName.text=getString(R.string.welcome_name,it.name)
            }else{
                startActivity(Intent(this,WelcomeActivity::class.java))
                finish()
            }
        }
    }

    private fun setupAction(){
        binding.btnLogout.setOnClickListener {
            mainViewModel.logout()
        }
    }

    companion object{
        private const val TAG = "MainActivity"
    }
}
