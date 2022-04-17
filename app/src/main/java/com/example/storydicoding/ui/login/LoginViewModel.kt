package com.example.storydicoding.ui.login

import androidx.lifecycle.*
import com.example.storydicoding.data.model.UserPreference
import com.example.storydicoding.data.response.LoginResponse
import com.example.storydicoding.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback

class LoginViewModel(private val pref:UserPreference): ViewModel() {

    private val _message= MutableLiveData<String>()
    val message:LiveData<String> =_message

    private val _error= MutableLiveData<Boolean>()
    val error:LiveData<Boolean> =_error

    fun loginUser(email:String,password:String){
        val client=ApiConfig.getApiService().loginUser(email,password)
        client.enqueue(object :Callback<LoginResponse>{
            override fun onResponse(
                call: Call<LoginResponse>,
                response: retrofit2.Response<LoginResponse>
            ) {
                if (response.body()?.error==false){
                    _error.value=false
                    _message.value="Kamu sudah berhasil login. Waktunya membaca cerita dari teman-temanmu."
                    val name=response.body()?.loginResult?.name
                    val token=response.body()?.loginResult?.token
                    viewModelScope.launch {
                        if (name != null&&token != null) {
                            pref.saveUser(name,token)
                        }
                    }
                }else{
                    _error.value=true
                    _message.value="Email dan/atau Password kamu sepertinya salah."
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _message.value=t.message.toString()
            }

        })
    }
}