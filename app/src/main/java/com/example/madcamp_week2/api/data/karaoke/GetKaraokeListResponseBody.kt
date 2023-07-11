package com.example.madcamp_week2.api.data.karaoke

import com.google.gson.annotations.SerializedName

data class GetKaraokeListResponseBody(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("data")
    val karaokeListData: KaraokeList
)
