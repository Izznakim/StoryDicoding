package com.example.storydicoding.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.storydicoding.data.database.StoryDatabase
import com.example.storydicoding.data.response.ListStoryItem
import com.example.storydicoding.data.retrofit.ApiService

class StoryRepository(private val storyDatabase: StoryDatabase,private val apiService: ApiService) {
    fun getStoriesPaging(token:String):LiveData<PagingData<ListStoryItem>>{
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(token,storyDatabase,apiService),
            pagingSourceFactory = {
//                StoryPagingSource(apiService,token)
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }
}