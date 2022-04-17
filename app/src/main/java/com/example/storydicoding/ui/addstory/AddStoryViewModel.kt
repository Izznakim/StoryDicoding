package com.example.storydicoding.ui.addstory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storydicoding.data.response.RegisterResponse
import com.example.storydicoding.data.retrofit.ApiConfig
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddStoryViewModel:ViewModel() {
    fun addNewStory(token:String,desc:RequestBody,photo:MultipartBody.Part):LiveData<String>{
        val message=MutableLiveData<String>()
        val client=ApiConfig.getApiService().addNewStory(token,desc,photo)
        client.enqueue(object :Callback<RegisterResponse>{
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.body()?.error==false){
                    message.value="Yey!, cerita telah ditambahkan."
                }else{
                    message.value="Yaaaah, gagal menambahkan cerita."
                    Log.d(TAG, "onResponse: ${response.body()?.message}")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                message.value=t.message
            }
        })
        return message
    }

    companion object{
        private const val TAG = "AddStoryViewModel"
    }
}