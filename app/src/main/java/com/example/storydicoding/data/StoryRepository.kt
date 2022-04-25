package com.example.storydicoding.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storydicoding.data.response.ListStoryItem
import com.example.storydicoding.data.retrofit.ApiService

class StoryRepository(private val apiService: ApiService) {
    fun getStoriesPaging(token:String):LiveData<PagingData<ListStoryItem>>{
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService,token)
            }
        ).liveData
    }
}