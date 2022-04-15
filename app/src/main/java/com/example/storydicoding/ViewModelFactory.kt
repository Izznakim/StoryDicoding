package com.example.storydicoding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storydicoding.ui.main.MainViewModel
import com.example.storydicoding.model.UserPreference
import com.example.storydicoding.ui.welcomepage.login.LoginViewModel
import com.example.storydicoding.ui.welcomepage.signup.SignupViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory(private val pref: UserPreference):ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(MainViewModel::class.java)-> MainViewModel(pref) as T
            modelClass.isAssignableFrom(LoginViewModel::class.java)->LoginViewModel(pref) as T
            modelClass.isAssignableFrom(SignupViewModel::class.java)->SignupViewModel(pref) as T
            else->throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}