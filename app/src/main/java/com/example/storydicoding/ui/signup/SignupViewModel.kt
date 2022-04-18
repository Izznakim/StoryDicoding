package com.example.storydicoding.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storydicoding.data.response.RegisterResponse
import com.example.storydicoding.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback

class SignupViewModel : ViewModel() {

    fun registerUser(name: String, email: String, password: String): LiveData<Boolean> {
        val error = MutableLiveData<Boolean>()
        val client = ApiConfig.getApiService().registerUser(name, email, password)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: retrofit2.Response<RegisterResponse>
            ) {
                error.value = response.body()?.error != false
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                error.value = true
            }
        })
        return error
    }
}