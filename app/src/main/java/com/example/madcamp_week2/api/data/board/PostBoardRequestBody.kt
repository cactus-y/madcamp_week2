package com.example.madcamp_week2.api.data.board

data class PostBoardRequestBody(
    val karaokeId: String,
    val content: String,
    val deadline: String
)
