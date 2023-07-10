package com.example.madcamp_week2.api.data

data class PostUserRequestBody(
    val email: String,
    val nickname: String,
    val gender: Boolean,
    val musicGenre: ArrayList<String>
)
