package com.example.madcamp_week2.api.service

import com.example.madcamp_week2.api.data.PutDeviceTokenRequestBody
import com.example.madcamp_week2.api.data.SuccessResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT

interface DeviceTokenService {
    @PUT("user/device")
    fun putDeviceToken(
        @Header("Authorization") token: String,
        @Body body: PutDeviceTokenRequestBody
    ): Call<SuccessResponseBody>
}