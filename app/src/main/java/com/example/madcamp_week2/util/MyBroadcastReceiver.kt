package com.example.madcamp_week2.util

import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.provider.BaseColumns
import android.util.Log
import com.example.madcamp_week2.api.data.ChatMessage
import com.example.madcamp_week2.db.ChatLogDBHelper
import com.example.madcamp_week2.db.ChatLogReaderContract
import com.example.madcamp_week2.db.ChatRoom
import com.example.madcamp_week2.db.RoomDBHelper
import com.example.madcamp_week2.db.RoomReaderContract
import com.example.madcamp_week2.ui.chat.ChatListAdapter

class MyBroadcastReceiver: BroadcastReceiver() {
    private lateinit var adapter: ChatListAdapter;

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("MyReceiver", "Intent: $intent")
        val receiverId = intent.getStringExtra("receiverId")!!
        val roomNumber = intent.getStringExtra("roomNumber")!!
        val message = intent.getStringExtra("message")!!
        val senderName = intent.getStringExtra("senderName")!!
        val senderId = intent.getStringExtra("senderId")!!
        val senderProfileImage = intent.getStringExtra("senderProfileImage")
        val roomAdded = intent.getBooleanExtra("roomAdded", false)

        val roomData = ChatRoom(
            id = null,
            roomNumber = roomNumber,
            myId = receiverId,
            otherId = senderId,
            otherUsername = senderName,
            otherProfileImage = senderProfileImage,
            latestMessage = message
        )
        Log.d("receiverId", receiverId!!)
        Log.d("roomNumber", roomNumber!!)
        Log.d("message", message!!)
        Log.d("senderName", senderName!!)
        Log.d("senderId", senderId!!)
        Log.d("senderProfileImage", "$senderProfileImage")


        if (roomAdded) {
            adapter.addChatRoom(roomData)
            adapter.notifyDataSetChanged()
        }
        else {
            val index = adapter.updateLatestMessage(roomNumber, message)
            adapter.notifyItemChanged(index)
        }
    }
    
    fun setAdapter(adapter: ChatListAdapter) {
        this.adapter = adapter
    }
}