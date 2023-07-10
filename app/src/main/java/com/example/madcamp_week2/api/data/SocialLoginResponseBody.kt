package com.example.madcamp_week2.api.data

import com.google.gson.annotations.SerializedName

data class SocialLoginResponseBody(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("token")
    val token: String?,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("profileImage")
    val profileImage: String
)
