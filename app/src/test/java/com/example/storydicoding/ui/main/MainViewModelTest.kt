package com.example.storydicoding.ui.main

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.storydicoding.data.model.User
import com.example.storydicoding.data.model.UserPreference
import com.example.storydicoding.data.response.ListStoryItem
import com.example.storydicoding.ui.DataDummy
import com.example.storydicoding.ui.MainCoroutineRule
import com.example.storydicoding.ui.adapter.StoryAdapter
import com.example.storydicoding.ui.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest{
    @get:Rule
    var instantExecutorRule=InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRules=MainCoroutineRule()

    @Mock
    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setUp() {
    }


    @Test
    fun `when Get Story Should Not Null`()=mainCoroutineRules.runBlockingTest {
        val dummyStory=DataDummy.generateDummyStoryResponse()
        val data=PagedTestDataSource.snapshot(dummyStory)
        val story=MutableLiveData<PagingData<ListStoryItem>>()
        story.value=data

        `when`(mainViewModel.getListStories("dummyToken")).thenReturn(story)
        val actualStory=mainViewModel.getListStories("dummyToken").getOrAwaitValue()

        val differ=AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = mainCoroutineRules.dispatcher,
            workerDispatcher = mainCoroutineRules.dispatcher
        )
        differ.submitData(actualStory)

        advanceUntilIdle()

        Mockito.verify(mainViewModel).getListStories("dummyToken")
        assertNotNull(differ.snapshot())
        assertEquals(dummyStory.size,differ.snapshot().size)
        assertEquals(dummyStory[0].description,differ.snapshot()[0]?.description)
    }

    class PagedTestDataSource private constructor(private val items:List<ListStoryItem>):PagingSource<Int,LiveData<List<ListStoryItem>>>(){
        override fun getRefreshKey(state: PagingState<Int, LiveData<List<ListStoryItem>>>): Int=0

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<ListStoryItem>>> =LoadResult.Page(
            emptyList(),0,1)

        companion object{
            fun snapshot(items: List<ListStoryItem>):PagingData<ListStoryItem>{
                return PagingData.from(items)
            }
        }
    }

    private val noopListUpdateCallback=object :ListUpdateCallback{
        override fun onInserted(position: Int, count: Int) {}

        override fun onRemoved(position: Int, count: Int) {}

        override fun onMoved(fromPosition: Int, toPosition: Int) {}

        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}