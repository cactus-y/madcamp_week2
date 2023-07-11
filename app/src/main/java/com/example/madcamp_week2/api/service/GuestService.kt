package com.example.madcamp_week2.api.service

import com.example.madcamp_week2.api.data.guest.GetGuestListResponseBody
import com.example.madcamp_week2.api.data.guest.PostGuestRequestBody
import com.example.madcamp_week2.api.data.guest.PostGuestResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface GuestService {
    @POST("guest")
    fun postNewGuest(
        @Header("Authorization") token: String,
        @Body info: PostGuestRequestBody
    ): Call<PostGuestResponseBody>

    @GET("guest/list")
    fun getGuestList(
        @Query("boardId") boardId: String,
        @Query("accepted") accepted: Boolean
    ): Call<GetGuestListResponseBody>
}