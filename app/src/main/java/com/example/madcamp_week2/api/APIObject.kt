package com.example.madcamp_week2.api

import com.example.madcamp_week2.api.service.SocialLoginService
import com.example.madcamp_week2.api.service.UserService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object APIObject {
    private const val BASE_URL = "https://6e70-192-249-19-234.jp.ngrok.io/"

    private val getRetrofit by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val getSocialLoginService : SocialLoginService by lazy { getRetrofit.create(SocialLoginService::class.java)}
    val getUserService: UserService by lazy { getRetrofit.create(UserService::class.java) }
}