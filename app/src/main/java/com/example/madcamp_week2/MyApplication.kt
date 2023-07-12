package com.example.madcamp_week2

import android.app.Application
import android.util.Log
import com.example.madcamp_week2.util.ForegroundDetector


class MyApplication : Application() {
    private var foregroundDetector: ForegroundDetector? = null
    override fun onCreate() {
        super.onCreate()
        foregroundDetector = ForegroundDetector(this@MyApplication)
        foregroundDetector!!.addListener(object : ForegroundDetector.Listener {
            override fun onBecameForeground() {
                Log.d("TAG", "Became Foreground")
            }

            override fun onBecameBackground() {
                Log.d("TAG", "Became Background")
            }
        })
    }

    override fun onTerminate() {
        super.onTerminate()
        foregroundDetector!!.unregisterCallbacks()
    }
}