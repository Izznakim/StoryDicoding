package com.example.storydicoding.data.response

import com.google.gson.annotations.SerializedName

class Response {
    data class RegisterResponse(
        @field:SerializedName("error")
        val error:Boolean,

        @field:SerializedName("message")
        val message:String,
    )

    data class LoginResponse(

        @field:SerializedName("loginResult")
        val loginResult: LoginResult,

        @field:SerializedName("error")
        var error: Boolean,

        @field:SerializedName("message")
        var message: String
    )

    data class LoginResult(

        @field:SerializedName("name")
        val name: String,

        @field:SerializedName("userId")
        val userId: String,

        @field:SerializedName("token")
        val token: String
    )
}