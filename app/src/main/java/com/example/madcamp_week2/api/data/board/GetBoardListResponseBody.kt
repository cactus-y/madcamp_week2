package com.example.madcamp_week2.api.data.board

import com.google.gson.annotations.SerializedName

data class GetBoardListResponseBody(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("boardList")
    val boardList: List<Board>
)
