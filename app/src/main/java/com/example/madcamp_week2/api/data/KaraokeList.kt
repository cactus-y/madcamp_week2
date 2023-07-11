package com.example.madcamp_week2.api.data

import com.google.gson.annotations.SerializedName

data class KaraokeList(
    @SerializedName("total_cnt")
    val count: Int,
    @SerializedName("list")
    val karaokeList: List<Karaoke>
)
