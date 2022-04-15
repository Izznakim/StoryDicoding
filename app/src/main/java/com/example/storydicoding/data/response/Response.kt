package com.example.storydicoding.data.response

import com.google.gson.annotations.SerializedName

class Response {
    data class RegisterResponse(
        @field:SerializedName("error")
        val error:Boolean,

        @field:SerializedName("message")
        val message:String,
    )
}