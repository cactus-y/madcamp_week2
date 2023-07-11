package com.example.madcamp_week2.ui.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp_week2.R
import com.example.madcamp_week2.databinding.ActivityChatRoomBinding
import com.example.madcamp_week2.sample.globalChatRoomList

class ChatRoomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatRoomBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)

        binding = ActivityChatRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val chatRoomIndex = intent.getIntExtra("chatRoomIndex", -1)
        if(chatRoomIndex != -1) {
            val currentChatRoom = globalChatRoomList[chatRoomIndex]
            supportActionBar!!.title = currentChatRoom.otherUser.name
            binding.rcvChatMsgList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding.rcvChatMsgList.adapter = ChatMessageListAdapter(currentChatRoom.chatlog)
        }
    }
}