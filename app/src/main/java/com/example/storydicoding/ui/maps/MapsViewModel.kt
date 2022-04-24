package com.example.storydicoding.ui.maps

import androidx.lifecycle.*
import com.example.storydicoding.data.model.User
import com.example.storydicoding.data.response.ListStoryItem
import com.example.storydicoding.data.response.StoriesResponse
import com.example.storydicoding.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback

class MapsViewModel:ViewModel() {
    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getListStoryMaps(token: String): LiveData<List<ListStoryItem>> {
        _isLoading.value = true
        val listStory = MutableLiveData<List<ListStoryItem>>()
        val client = ApiConfig.getApiService().getStoryMaps(token)
        client.enqueue(object : Callback<StoriesResponse> {
            override fun onResponse(
                call: Call<StoriesResponse>,
                response: retrofit2.Response<StoriesResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    listStory.value = response.body()?.listStory
                    _error.value = false
                } else {
                    _error.value = true
                }
            }

            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                _isLoading.value = false
                _error.value = true
            }
        })
        return listStory
    }
}