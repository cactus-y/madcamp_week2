package com.example.madcamp_week2.ui.map

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.madcamp_week2.R
import com.example.madcamp_week2.api.APIObject
import com.example.madcamp_week2.api.data.board.PostBoardRequestBody
import com.example.madcamp_week2.api.data.board.PostBoardResponseBody
import com.example.madcamp_week2.databinding.ActivityNewPostBinding
import com.example.madcamp_week2.util.getUserInfoFromToken
import com.example.madcamp_week2.util.getUserToken
import retrofit2.Call
import retrofit2.Response

class NewBoardActivity : AppCompatActivity() {
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

        val user = getUserInfoFromToken(applicationContext)
        val userToken = "Bearer ${getUserToken(applicationContext)}"

        binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val karaokeObjectId = intent.getStringExtra("karaokeObjectId")

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

        // set default time value
        year = binding.dpMapNewPost.year
        month = binding.dpMapNewPost.month + 1
        day = binding.dpMapNewPost.dayOfMonth
        hour = binding.tpMapNewPost.hour
        minute = binding.tpMapNewPost.minute
        
        // time changed listener
        binding.tpMapNewPost.setOnTimeChangedListener { view, hourOfDay, minute ->
            hour = hourOfDay
            this.minute = minute
            println("\n\n\nhour: ${hour}\nminute: ${this.minute}\n\n\n")
        }
        
        // date changed listener
        binding.dpMapNewPost.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
            this.year = year
            month = monthOfYear + 1
            day = dayOfMonth

            println("\n\n\nyear: ${this.year}\nmonth: ${month}\nday: ${day}\n\n\n")
        }

        // button click listener
        binding.tvMapNewPostCancel.setOnClickListener {
            finish()
        }
        binding.tvMapNewPostSave.setOnClickListener {
            val comment = binding.etMapNewPostComment.text.toString()

            // date converting into string
            var monthString: String
            var dayString: String
            var hourString: String
            var minuteString: String

            if(month / 10 == 0)
                monthString = "0${month}"
            else
                monthString = month.toString()

            if(day / 10 == 0)
                dayString = "0${day}"
            else
                dayString = day.toString()

            if(hour / 10 == 0)
                hourString = "0${hour}"
            else
                hourString = hour.toString()

            if(minute / 10 == 0)
                minuteString = "0${minute}"
            else
                minuteString = minute.toString()

            val deadline =  "${year}-${monthString}-${dayString}T${hourString}:${minuteString}:00"

            println("\n\n\n${deadline}\n\n\n")

            // pass all data to the server
            if(karaokeObjectId != null) {
                val call = APIObject.getBoardService.postNewBoard(userToken, PostBoardRequestBody(karaokeObjectId, comment, deadline))
                call.enqueue(object: retrofit2.Callback<PostBoardResponseBody> {
                    override fun onResponse(
                        call: Call<PostBoardResponseBody>,
                        response: Response<PostBoardResponseBody>
                    ) {
                        if(response.isSuccessful) {
                            val data: PostBoardResponseBody? = response.body()
                            Log.d(
                                "Successful POST Board",
                                "boardID: ${data?.createdBoard?.boardObjectId}"
                            )
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<PostBoardResponseBody>, t: Throwable) {
                        Toast.makeText(applicationContext, "New board post failed!", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }


    }
}