package com.example.madcamp_week2.api

import com.example.madcamp_week2.api.service.BoardService
import com.example.madcamp_week2.api.service.GuestService
import com.example.madcamp_week2.api.service.KaraokeService
import com.example.madcamp_week2.api.service.SocialLoginService
import com.example.madcamp_week2.api.service.UserService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object APIObject {
    private const val BASE_URL = "http://34.64.184.111:3030/"

    private val getRetrofit by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val getSocialLoginService : SocialLoginService by lazy { getRetrofit.create(SocialLoginService::class.java)}
    val getUserService: UserService by lazy { getRetrofit.create(UserService::class.java) }
    val getKaraokeService: KaraokeService by lazy { getRetrofit.create(KaraokeService::class.java) }
    val getBoardService: BoardService by lazy { getRetrofit.create(BoardService::class.java) }
    val getGuestService: GuestService by lazy { getRetrofit.create(GuestService::class.java)}
}