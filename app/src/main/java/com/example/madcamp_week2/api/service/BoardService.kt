package com.example.madcamp_week2.api.service

import com.example.madcamp_week2.api.data.board.GetBoardListResponseBody
import com.example.madcamp_week2.api.data.board.PostBoardRequestBody
import com.example.madcamp_week2.api.data.board.PostBoardResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface BoardService {
    @GET("board/list")
    fun getBoardList(
        @Query("karaokeId") karaokeId: String
    ): Call<GetBoardListResponseBody>


    @POST("board")
    fun postNewBoard(
        @Header("Authorization") token: String,
        @Body info: PostBoardRequestBody
    ): Call<PostBoardResponseBody>

    @PATCH("board/{id}")
    fun updateBoard(
        @Header("Authorization") token: String,
        @Path("id") boardObjectId: String,
        @Body updateInfo: PostBoardRequestBody
    ): Call<PostBoardResponseBody>
}