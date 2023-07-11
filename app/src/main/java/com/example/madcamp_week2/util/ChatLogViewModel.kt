package com.example.madcamp_week2.util

import androidx.lifecycle.MutableLiveData
import com.example.madcamp_week2.api.data.ChatMessage
import com.example.madcamp_week2.api.data.ChatRoom

class ChatLogViewModel {
    private val roomNumber: String = "";
    private val chatLog = MutableLiveData<List<ChatMessage>>()


}