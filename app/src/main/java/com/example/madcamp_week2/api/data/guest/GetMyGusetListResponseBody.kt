package com.example.madcamp_week2.api.data.guest

import com.google.gson.annotations.SerializedName

data class GetMyGusetListResponseBody(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("list")
    val boardList: List<MyGuest>
)
