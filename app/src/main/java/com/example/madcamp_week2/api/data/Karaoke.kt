package com.example.madcamp_week2.api.data

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Karaoke(
    @SerializedName("id")
    val objectId: String,
    @SerializedName("place_id")
    val placeId: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("road_address")
    val roadAddress: String,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("longitude")
    val longitude: String,
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("distance")
    val distance: Int
)
