package com.example.storydicoding.ui

import androidx.lifecycle.MutableLiveData
import com.example.storydicoding.data.response.ListStoryItem
import com.example.storydicoding.data.response.StoriesResponse

object DataDummy {
    fun generateDummyStoryResponse(): List<ListStoryItem>{
        val items: ArrayList<ListStoryItem> = arrayListOf()
        for (i in 0..100){
            val story=ListStoryItem(
                "photo$i",
                "0$i-04-2022",
                "name $i",
                "desc $i",
                i.toDouble(),
                i.toString(),
                i.toDouble()
            )
            items.add(story)
        }
        return items
    }
}