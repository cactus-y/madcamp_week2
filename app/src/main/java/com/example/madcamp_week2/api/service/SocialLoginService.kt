package com.example.madcamp_week2.api.service

import com.example.madcamp_week2.api.data.SocialLoginRequestBody
import com.example.madcamp_week2.api.data.SocialLoginResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface SocialLoginService {
    @Headers("Content-Type: application/json")
    @POST("auth/login/google")
    fun loginWithGoogle(@Body info: SocialLoginRequestBody): Call<SocialLoginResponseBody>
}