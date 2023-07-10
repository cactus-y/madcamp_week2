package com.example.madcamp_week2

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.madcamp_week2.ui.auth.LoginActivity
import com.example.madcamp_week2.ui.chat.ChatRoomActivity


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val pref: SharedPreferences = applicationContext.getSharedPreferences(getString(R.string.pref_key), Activity.MODE_PRIVATE);
        val token: String? = pref.getString(getString(R.string.token_key), "");
        println("token: $token");
        val activityIntent: Intent = if (token != null || token == "") {
            Intent(this, LoginActivity::class.java)
        } else {
            Intent(this, MainActivity::class.java)
        }
        startActivity(activityIntent)
        finish()
    }
}