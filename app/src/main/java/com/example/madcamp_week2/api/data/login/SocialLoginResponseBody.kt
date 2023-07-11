package com.example.madcamp_week2.api.data.login

import com.example.madcamp_week2.api.data.user.User
import com.google.gson.annotations.SerializedName

data class SocialLoginResponseBody(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("token")
    val token: String?,
    @SerializedName("user")
    val user: User
)
