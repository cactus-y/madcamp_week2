package com.example.madcamp_week2.api.data.karaoke

import com.google.gson.annotations.SerializedName

data class KaraokeList(
    @SerializedName("totalCnt")
    val count: Int,
    @SerializedName("list")
    val karaokeList: List<Karaoke>
)
