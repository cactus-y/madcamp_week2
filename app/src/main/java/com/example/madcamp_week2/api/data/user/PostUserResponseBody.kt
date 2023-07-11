package com.example.madcamp_week2.api.data.user

import com.google.gson.annotations.SerializedName

data class PostUserResponseBody(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("token")
    val token: String?,
    @SerializedName("user")
    val user: User?,
    @SerializedName("message")
    val message: String?
)