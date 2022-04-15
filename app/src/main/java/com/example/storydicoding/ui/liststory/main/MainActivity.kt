package com.example.storydicoding.ui.liststory.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.example.storydicoding.R
import com.example.storydicoding.ViewModelFactory
import com.example.storydicoding.databinding.ActivityMainBinding
import com.example.storydicoding.data.model.UserPreference
import com.example.storydicoding.ui.liststory.ListStoryFragment
import com.example.storydicoding.ui.welcomepage.WelcomeActivity

private val Context.dataStore:DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WelcomeActivity.setupView(window,supportActionBar)
        setupViewModel()
        setupAction()
    }

    private fun setupViewModel(){
        mainViewModel=ViewModelProvider(this,ViewModelFactory(UserPreference.getInstance(dataStore)))[MainViewModel::class.java]

        mainViewModel.getUser().observe(this){
            if (it.isLogin){
                setupFragment()
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

    private fun setupFragment(){
//        val mFragmentManager=supportFragmentManager
//        val fragment=mFragmentManager.findFragmentByTag(ListStoryFragment::class.java.simpleName)
//
//        if (fragment !is ListStoryFragment){
//            mFragmentManager.commit {
//                add(R.id.fragment_story_container, ListStoryFragment(), ListStoryFragment::class.java.simpleName)
//            }
//        }
    }

    companion object{
        private const val TAG = "MainActivity"
    }
}
