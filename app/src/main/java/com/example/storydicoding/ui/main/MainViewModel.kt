package com.example.storydicoding.ui.main

import androidx.lifecycle.*
import com.example.storydicoding.data.model.User
import com.example.storydicoding.data.model.UserPreference
import com.example.storydicoding.data.response.ListStoryItem
import com.example.storydicoding.data.response.StoriesResponse
import com.example.storydicoding.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback

class MainViewModel(private val pref: UserPreference) : ViewModel() {
    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getUser(): LiveData<User> {
        return pref.getUser().asLiveData()
    }
    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }

    fun getListStories(token: String): LiveData<List<ListStoryItem>> {
        _isLoading.value = true
        val listStory = MutableLiveData<List<ListStoryItem>>()
        val client = ApiConfig.getApiService().getStories(token)
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