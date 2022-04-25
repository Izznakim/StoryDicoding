package com.example.storydicoding.ui.addstory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.io.File
import org.mockito.Mockito.`when`

@RunWith(MockitoJUnitRunner::class)
class AddStoryViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var addStoryViewModel: AddStoryViewModel

    @Before
    fun setUp() {
        addStoryViewModel = AddStoryViewModel()
    }

    @Test
    fun `when return ErrorValue==false Should Post the Story`() {
        val myFile = File("dummyPhotoPath")
        val desc = "Dummy Desc"
        val description = desc.toRequestBody("text/plain".toMediaType())
        val requestImageFile = myFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part =
            MultipartBody.Part.createFormData("photo", myFile.name, requestImageFile)

        val expectedError = MutableLiveData<Boolean>()
        expectedError.value = false
        `when`(
            addStoryViewModel.addNewStory("dummyToken", description, imageMultipart)
        ).thenReturn(expectedError)
        Mockito.verify(addStoryViewModel).addNewStory("dummyToken", description, imageMultipart)
    }
}