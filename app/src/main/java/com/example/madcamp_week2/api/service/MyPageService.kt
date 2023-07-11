package com.example.madcamp_week2.api.service

import com.example.madcamp_week2.api.data.board.GetMyBoardListResponseBody
import com.example.madcamp_week2.api.data.guest.GetMyGusetListResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface MyPageService {
    @GET("/board/list/auth")
    fun getMyBoardList(
        @Header("Authorization") token: String
    ): Call<GetMyBoardListResponseBody>

    @GET("/guest/list/auth")
    fun getMyGuestList(
        @Header("Authorization") token: String
    ): Call<GetMyGusetListResponseBody>
}