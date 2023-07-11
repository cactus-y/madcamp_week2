package com.example.madcamp_week2.api.data.guest

import com.google.gson.annotations.SerializedName

data class PostGuestResponseBody(
    @SerializedName("success")
    val success: Boolean
)
