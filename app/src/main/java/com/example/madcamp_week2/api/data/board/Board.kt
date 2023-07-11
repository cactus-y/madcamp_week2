package com.example.madcamp_week2.api.data.board

import com.example.madcamp_week2.api.data.user.User
import com.google.gson.annotations.SerializedName

data class Board(
    @SerializedName("id")
    val boardObjectId: String,
    @SerializedName("content")
    val comment: String,
    @SerializedName("karaokeId")
    val karaokeObjectId: String,
    @SerializedName("togetherCount")
    val guestCount: Int,
    @SerializedName("author")
    val author: User,
    @SerializedName("guestList")
    val guestList: List<User>
)
