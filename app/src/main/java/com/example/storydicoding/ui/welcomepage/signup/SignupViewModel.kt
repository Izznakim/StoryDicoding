package com.example.storydicoding.ui.welcomepage.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storydicoding.data.model.User
import com.example.storydicoding.data.model.UserPreference
import com.example.storydicoding.data.response.Response
import com.example.storydicoding.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback

class SignupViewModel(private val pref:UserPreference):ViewModel() {

    private val _message=MutableLiveData<String>()
    val message:LiveData<String> =_message

    fun registerUser(user:User){
        val client = ApiConfig.getApiService().registerUser(user.name,user.email,user.password)
        client.enqueue(object :Callback<Response.RegisterResponse>{
            override fun onResponse(
                call: Call<Response.RegisterResponse>,
                response: retrofit2.Response<Response.RegisterResponse>
            ) {
                if (response.body()?.error == false){
                    _message.value="Akunmu sudah jadi ea. Yuk, login dan bagikan cerita belajarmu di dicoding."
                }else{
                    _message.value="Akun ini sudah pernah dibuat, coba kamu login terlebih dahulu."
                }
            }

            override fun onFailure(call: Call<Response.RegisterResponse>, t: Throwable) {
                _message.value=t.message.toString()
            }
        })
    }

    fun saveUser(user: User){
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }
}