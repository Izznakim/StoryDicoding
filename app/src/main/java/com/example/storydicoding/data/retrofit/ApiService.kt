package com.example.storydicoding.data.retrofit

import com.example.storydicoding.data.response.ListStoryItem
import com.example.storydicoding.data.response.LoginResponse
import com.example.storydicoding.data.response.RegisterResponse
import com.example.storydicoding.data.response.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") authorization: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): StoriesResponse

    @Multipart
    @POST("stories")
    fun addNewStory(
        @Header("Authorization") authorization: String,
        @Part("description") desc: RequestBody,
        @Part photo: MultipartBody.Part,
        @Part("lat") lat:Float?=null,
        @Part("lon") lon:Float?=null
    ): Call<RegisterResponse>

    @GET("stories?location=1")
    fun getStoryMaps(@Header("Authorization") authorization: String): Call<StoriesResponse>
}