package com.example.madcamp_week2.api.data

import com.google.gson.annotations.SerializedName

data class SuccessResponseBody(
    @SerializedName("success")
    val success: Boolean
)
