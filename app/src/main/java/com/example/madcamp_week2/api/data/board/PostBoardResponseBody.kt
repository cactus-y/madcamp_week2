package com.example.madcamp_week2.api.data.board

import com.google.gson.annotations.SerializedName

data class PostBoardResponseBody(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("board")
    val createdBoard: Board
)
