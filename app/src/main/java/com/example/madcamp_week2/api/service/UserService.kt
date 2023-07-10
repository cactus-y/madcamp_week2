package com.example.madcamp_week2.api.service

import android.graphics.PostProcessor
import com.example.madcamp_week2.api.data.PostUserResponseBody
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UserService {
    @Multipart
    @POST("user")
    fun createUser(
        @Part file: MultipartBody.Part?,
        @Part data: MultipartBody.Part
    ): Call<PostUserResponseBody>
}