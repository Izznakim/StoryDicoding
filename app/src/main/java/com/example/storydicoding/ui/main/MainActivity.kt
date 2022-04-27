package com.example.storydicoding.ui.main

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storydicoding.R
import com.example.storydicoding.ViewModelFactory
import com.example.storydicoding.databinding.ActivityMainBinding
import com.example.storydicoding.data.model.UserPreference
import com.example.storydicoding.data.response.ListStoryItem
import com.example.storydicoding.setupView
import com.example.storydicoding.ui.WelcomeActivity
import com.example.storydicoding.ui.adapter.LoadingStateAdapter
import com.example.storydicoding.ui.adapter.StoryAdapter
import com.example.storydicoding.ui.addstory.AddStoryActivity
import com.example.storydicoding.ui.detailstory.DetailActivity
import com.example.storydicoding.ui.login.LoginActivity
import com.example.storydicoding.ui.maps.MapsActivity
import com.google.android.material.snackbar.Snackbar

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter:StoryAdapter

    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView(window)
        setupAdapter()
        setupViewModel()
        setupActionView()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_logout -> {
                mainViewModel.logout()
                true
            }
            R.id.menu_translate -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            R.id.menu_map_mode -> {
                startActivity(
                    Intent(
                        this,
                        MapsActivity::class.java
                    ).putExtra(AddStoryActivity.TOKEN, token)
                )
                true
            }
            else -> true
        }
    }

    private fun setupAdapter(){
        binding.rvStory.layoutManager=LinearLayoutManager(this)
        adapter=StoryAdapter{
            startActivity(
                Intent(this, DetailActivity::class.java).putExtra(
                    DetailActivity.STORY,
                    it
                )
            )
        }
        binding.rvStory.adapter=adapter.withLoadStateFooter(
            footer = LoadingStateAdapter{
                adapter.retry()
            }
        )
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MainViewModel::class.java]

        mainViewModel.getUser().observe(this) {
            if (it.isLogin) {
                mainViewModel.getListStories("Bearer ${it.token}").observe(this){ list->
                    adapter.submitData(lifecycle,list)
                }
                token = it.token
            } else {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }
    }

    private fun setupActionView() {
        binding.fabCreateStory.setOnClickListener {
            startActivity(
                Intent(this, AddStoryActivity::class.java)
                    .putExtra(AddStoryActivity.TOKEN, token)
            )
        }
    }
}
