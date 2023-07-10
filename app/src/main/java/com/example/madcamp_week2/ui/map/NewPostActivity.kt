package com.example.madcamp_week2.ui.map

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.madcamp_week2.R
import com.example.madcamp_week2.databinding.ActivityNewPostBinding

class NewPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewPostBinding

    private var hour = 0
    private var minute = 0
    private var year = 0
    private var month = 0
    private var day = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)

        binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvMapPickedKaraokeName.text = intent.getStringExtra("karaokeName")
        binding.tvMapPickedKaraokeAddr.text = intent.getStringExtra("karaokeAddr")
        binding.tvMapPickedKaraokeRoadAddr.text = intent.getStringExtra("karaokeRoadAddr")

        // if phone number is empty
        val karaokePhone = intent.getStringExtra("karaokePhone")
        if(karaokePhone == "") {
            binding.tvMapPickedKaraokePhone.text = "전화번호가 없어요!"
            binding.tvMapPickedKaraokePhone.setTextColor(Color.parseColor("#D3D3D3"))
        } else {
            binding.tvMapPickedKaraokePhone.text = karaokePhone
            binding.tvMapPickedKaraokePhone.setTextColor(Color.parseColor("#000000"))
        }
        
        // time changed listener
        binding.tpMapNewPost.setOnTimeChangedListener { view, hourOfDay, minute ->
            hour = hourOfDay
            this.minute = minute
        }
        
        // date changed listener
        binding.dpMapNewPost.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
            this.year = year
            month = monthOfYear + 1
            day = dayOfMonth
        }

        // button click listener
        binding.tvMapNewPostCancel.setOnClickListener {
            finish()
        }
        binding.tvMapNewPostSave.setOnClickListener {
            val comment = binding.etMapNewPostComment.text

            // pass time value to the server

            finish()
        }


    }
}