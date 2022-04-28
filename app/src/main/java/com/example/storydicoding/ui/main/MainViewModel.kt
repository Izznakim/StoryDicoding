package com.example.storydicoding.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storydicoding.data.StoryRepository
import com.example.storydicoding.data.model.User
import com.example.storydicoding.data.model.UserPreference
import com.example.storydicoding.data.response.ListStoryItem
import kotlinx.coroutines.launch

class MainViewModel(
    private val pref: UserPreference,
    private val storyRepository: StoryRepository
) : ViewModel() {

    fun getListStories(token: String): LiveData<PagingData<ListStoryItem>> =
        storyRepository.getStoriesPaging(token).cachedIn(viewModelScope)

    fun getUser(): LiveData<User> {
        return pref.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }
}