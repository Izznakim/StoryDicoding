package com.example.storydicoding.ui.addstory

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

class AddStoryViewModel : ViewModel() {
    fun addNewStory(
        token: String,
        desc: RequestBody,
        photo: MultipartBody.Part
    ): LiveData<Boolean> {
        val error = MutableLiveData<Boolean>()
        val client = ApiConfig.getApiService().addNewStory(token, desc, photo)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
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