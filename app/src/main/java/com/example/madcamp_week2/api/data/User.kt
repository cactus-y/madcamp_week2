package com.example.madcamp_week2.api.data

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("gender")
    val gender: Boolean,
    @SerializedName("musicGenre")
    val musicGenre: ArrayList<String>,
    @SerializedName("profileImage")
    val profileImage: String?,
    @SerializedName("createdAt")
    val createdAt: String
)
