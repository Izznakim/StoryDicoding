package com.example.storydicoding.ui.liststory.list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storydicoding.data.response.ListStoryItem
import com.example.storydicoding.data.response.StoriesResponse
import com.example.storydicoding.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback

class ListStoryViewModel:ViewModel() {
    fun getListStories(token:String):LiveData<List<ListStoryItem>>{
        val listStory=MutableLiveData<List<ListStoryItem>>()
        val client= ApiConfig.getApiService().getStories(token)
        client.enqueue(object : Callback<StoriesResponse> {
            override fun onResponse(
                call: Call<StoriesResponse>,
                response: retrofit2.Response<StoriesResponse>
            ) {
                if (response.isSuccessful){
                    listStory.value=response.body()?.listStory
                }else{
                    Log.d(TAG, "onResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }
        })
        return listStory
    }

    companion object{
        private const val TAG = "ListStoryViewModel"
    }
}