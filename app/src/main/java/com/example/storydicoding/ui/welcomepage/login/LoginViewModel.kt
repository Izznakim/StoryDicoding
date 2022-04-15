package com.example.storydicoding.ui.welcomepage.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.storydicoding.data.model.User
import com.example.storydicoding.data.model.UserPreference
import kotlinx.coroutines.launch

class LoginViewModel(private val pref:UserPreference): ViewModel() {
    fun getUser():LiveData<User>{
        return pref.getUser().asLiveData()
    }

    fun login(){
        viewModelScope.launch {
            pref.login()
        }
    }
}