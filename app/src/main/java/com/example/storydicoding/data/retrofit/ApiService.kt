package com.example.storydicoding.data.retrofit

import com.example.storydicoding.data.response.LoginResponse
import com.example.storydicoding.data.response.RegisterResponse
import com.example.storydicoding.data.response.StoriesResponse
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
    fun getStories(@Header("Authorization") authorization: String): Call<StoriesResponse>
}