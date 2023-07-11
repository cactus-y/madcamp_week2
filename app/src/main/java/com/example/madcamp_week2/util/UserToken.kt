package com.example.madcamp_week2.util

import android.app.Activity
import android.content.Context
import android.util.Log
import com.auth0.android.jwt.JWT
import com.example.madcamp_week2.R
import com.example.madcamp_week2.api.data.User

fun getUserToken(context: Context): String {
    val pref =
        context.getSharedPreferences(context.getString(R.string.pref_key), Activity.MODE_PRIVATE)
    return pref.getString(context.getString(R.string.token_key), "")!!
}
fun getUserInfoFromToken(context: Context, ): User {
    val pref =
        context.getSharedPreferences(context.getString(R.string.pref_key), Activity.MODE_PRIVATE);
    val jwt = JWT(pref.getString(context.getString(R.string.token_key), "")!!)
    val userId = jwt.getClaim("userId").asString()
    val email = jwt.getClaim("email").asString()
    val nickname = jwt.getClaim("nickname").asString()
    val musicGenre = jwt.getClaim("musicGenre").asList(String::class.java)
    val profileImage = jwt.getClaim("profileImage").asString()
    val gender = jwt.getClaim("gender").asBoolean()
    val list = ArrayList<String>()
    Log.d("user token", "" + musicGenre.size)
    for (i: Int in musicGenre.indices) {
        list.add(musicGenre[i])
    }
    return User(
        id = userId!!,
        email = email!!,
        nickname = nickname!!,
        profileImage = profileImage,
        gender = gender!!,
        musicGenre = list,
        createdAt = null
    )
}