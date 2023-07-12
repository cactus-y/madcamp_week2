package com.example.madcamp_week2.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.madcamp_week2.api.data.ChatMessage
import com.example.madcamp_week2.db.ChatRoom
import com.example.madcamp_week2.ui.chat.ChatListAdapter
import com.example.madcamp_week2.ui.chat.ChatMessageListAdapter

class ChatMessageBroadcastReceiver : BroadcastReceiver() {
    private lateinit var adapter: ChatMessageListAdapter;
    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        val id = intent.getLongExtra("id", -1L)
        val receiverId = intent.getStringExtra("receiverId")!!
        val roomNumber = intent.getStringExtra("roomNumber")!!
        val message = intent.getStringExtra("message")!!
        val senderName = intent.getStringExtra("senderName")!!
        val senderId = intent.getStringExtra("senderId")!!
        val senderProfileImage = intent.getStringExtra("senderProfileImage")
        val timestamp = intent.getStringExtra("timestamp")!!

        val data = ChatMessage(
            id = id,
            roomNumber = roomNumber,
            receiverId = receiverId,
            senderProfileImage = senderProfileImage,
            senderId = senderId,
            senderName = senderName,
            timestamp = timestamp.toLong(),
            msg = message
        )
        adapter.addItem(data)
        adapter.notifyItemInserted(adapter.itemCount - 1)

    }
    fun setAdapter(adapter: ChatMessageListAdapter) {
        this.adapter = adapter
    }
}