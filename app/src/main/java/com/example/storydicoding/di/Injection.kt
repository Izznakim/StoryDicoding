package com.example.storydicoding.di

import android.content.Context
import com.example.storydicoding.data.StoryRepository
import com.example.storydicoding.data.retrofit.ApiConfig

object Injection {
    fun provideRepository(): StoryRepository {
        val apiService = ApiConfig.getApiService()
        return StoryRepository(apiService)
    }
}