package com.example.madcamp_week2.api.data

data class ChatMessage(
    val id: Long?,
    val receiverId: String,
    val senderId: String,
    val senderName: String,
    val senderProfileImage: String?,
    val msg: String,
    val roomNumber: String,
    val timestamp: Long
)