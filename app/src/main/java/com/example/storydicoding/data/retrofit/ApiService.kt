package com.example.storydicoding.data.retrofit

import com.example.storydicoding.data.response.Response
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun registerUser(
        @Field("name")name:String,
        @Field("email")email:String,
        @Field("password")password:String
    ):Call<Response.RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun loginUser(
        @Field("email")email:String,
        @Field("password")password:String
    ):Call<Response.LoginResponse>
}