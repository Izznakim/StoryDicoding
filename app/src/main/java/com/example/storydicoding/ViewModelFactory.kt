package com.example.storydicoding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storydicoding.ui.main.MainViewModel
import com.example.storydicoding.data.model.UserPreference
import com.example.storydicoding.ui.login.LoginViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory(private val pref: UserPreference) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(pref) as T
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(pref) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}