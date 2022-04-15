package com.example.storydicoding.ui.welcomepage.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storydicoding.model.User
import com.example.storydicoding.model.UserPreference
import kotlinx.coroutines.launch

class SignupViewModel(private val pref:UserPreference):ViewModel() {
    fun saveUser(user: User){
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }
}