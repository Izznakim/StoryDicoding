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
import com.example.storydicoding.ui.WelcomeActivity
import com.example.storydicoding.ui.adapter.StoryAdapter
import com.example.storydicoding.ui.addstory.AddStoryActivity
import com.google.android.material.snackbar.Snackbar

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
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
            else -> true
        }
    }

    private fun setupAdapter() {
        binding.apply {
            rvStory.layoutManager = LinearLayoutManager(this@MainActivity)
            rvStory.setHasFixedSize(true)
        }
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MainViewModel::class.java]

        mainViewModel.getUser().observe(this) {
            if (it.isLogin) {
                mainViewModel.getListStories("Bearer ${it.token}").observe(this) { listStory ->
                    binding.rvStory.adapter = setStories(listStory)
                }
                token = it.token
            } else {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

        mainViewModel.error.observe(this) {
            val message = if (!it) {
                getString(R.string.message_success_load_list)
            } else {
                getString(R.string.message_fail_load_list)
            }
            Snackbar.make(window.decorView, message, Snackbar.LENGTH_SHORT).show()
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
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

    private fun setStories(stories: List<ListStoryItem>): StoryAdapter {
        val listStory = ArrayList<ListStoryItem>()
        if (stories.isNotEmpty()) {
            stories.forEach {
                listStory.add(it)
            }
        } else {
            Snackbar.make(window.decorView, getString(R.string.empty_data), Snackbar.LENGTH_LONG)
                .show()
        }
        return StoryAdapter(listStory)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}
