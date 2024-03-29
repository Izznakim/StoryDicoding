package com.example.storydicoding.di

import android.content.Context
import com.example.storydicoding.data.StoryRepository
import com.example.storydicoding.data.database.StoryDatabase
import com.example.storydicoding.data.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(database, apiService)
    }
}