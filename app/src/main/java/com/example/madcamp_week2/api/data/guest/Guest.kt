package com.example.madcamp_week2.api.data.guest

import com.google.gson.annotations.SerializedName

data class Guest(
    @SerializedName("board_id")
    val boardObjectId: String,
    @SerializedName("guest_id")
    val guestObjectId: String,
    @SerializedName("accepted")
    val accepted: Boolean
)
