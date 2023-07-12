package com.example.madcamp_week2.api.data.guest

import com.example.madcamp_week2.api.data.user.User
import com.google.gson.annotations.SerializedName

data class GetGuestListResponseBody(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("list")
    val guestList: List<User>
)
