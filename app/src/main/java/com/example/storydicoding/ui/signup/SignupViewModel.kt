package com.example.storydicoding.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storydicoding.data.response.RegisterResponse
import com.example.storydicoding.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback

class SignupViewModel:ViewModel() {

    fun registerUser(name:String,email:String,password:String):LiveData<String>{
        val message=MutableLiveData<String>()
        val client = ApiConfig.getApiService().registerUser(name,email,password)
        client.enqueue(object :Callback<RegisterResponse>{
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: retrofit2.Response<RegisterResponse>
            ) {
                if (response.body()?.error == false){
                    message.value="Akunmu sudah jadi ea. Yuk, login dan bagikan cerita belajarmu di dicoding."
                }else{
                    message.value="Kamu sepertinya sudah pernah membuat Akun. Coba kamu login terlebih dahulu."
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                message.value=t.message.toString()
            }
        })
        return message
    }
}