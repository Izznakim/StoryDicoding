package com.example.storydicoding.ui.liststory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storydicoding.data.response.ListStoryItem
import com.example.storydicoding.data.response.StoriesResponse
import com.example.storydicoding.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback

class ListStoryViewModel : ViewModel() {
    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error
    fun getListStories(token: String): LiveData<List<ListStoryItem>> {
        val listStory = MutableLiveData<List<ListStoryItem>>()
        val client = ApiConfig.getApiService().getStories(token)
        client.enqueue(object : Callback<StoriesResponse> {
            override fun onResponse(
                call: Call<StoriesResponse>,
                response: retrofit2.Response<StoriesResponse>
            ) {
                if (response.isSuccessful) {
                    listStory.value = response.body()?.listStory
                    _error.value = false
                } else {
                    _error.value = true
                }
            }

            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                _error.value = true
            }
        })
        return listStory
    }
}