package com.example.storydicoding.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storydicoding.data.model.UserPreference
import com.example.storydicoding.data.response.LoginResponse
import com.example.storydicoding.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback

class LoginViewModel(private val pref: UserPreference) : ViewModel() {

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    fun loginUser(email: String, password: String) {
        val client = ApiConfig.getApiService().loginUser(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: retrofit2.Response<LoginResponse>
            ) {
                if (response.body()?.error == false) {
                    _error.value = false
                    val name = response.body()?.loginResult?.name
                    val token = response.body()?.loginResult?.token
                    viewModelScope.launch {
                        if (name != null && token != null) {
                            pref.saveUser(name, token)
                        }
                    }
                } else {
                    _error.value = true
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _error.value = true
            }

        })
    }
}