package com.example.storydicoding.ui.main

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowInsets
import android.view.WindowManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.example.storydicoding.R
import com.example.storydicoding.ViewModelFactory
import com.example.storydicoding.data.model.User
import com.example.storydicoding.databinding.ActivityMainBinding
import com.example.storydicoding.data.model.UserPreference
import com.example.storydicoding.ui.liststory.ListStoryFragment
import com.example.storydicoding.ui.WelcomeActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
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
            else -> true
        }
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MainViewModel::class.java]

        mainViewModel.getUser().observe(this) {
            if (it.isLogin) {
                setupFragment(it)
            } else {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }
    }

    private fun setupFragment(user: User) {
        val mFragmentManager = supportFragmentManager
        val fragment = mFragmentManager.findFragmentByTag(ListStoryFragment::class.java.simpleName)
        val mBundle = Bundle()
        val listStoryFragment = ListStoryFragment()

        mBundle.putParcelable(ListStoryFragment.USER, user)
        listStoryFragment.arguments = mBundle

        if (fragment !is ListStoryFragment) {
            mFragmentManager.commit {
                add(
                    R.id.fragment_story_container,
                    listStoryFragment,
                    ListStoryFragment::class.java.simpleName
                )
            }
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
}
