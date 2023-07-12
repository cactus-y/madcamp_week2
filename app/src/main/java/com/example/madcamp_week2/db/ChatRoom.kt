package com.example.madcamp_week2.db

data class ChatRoom(
    val id: Long?,
    val roomNumber: String,
    val myId: String,
    val otherId: String,
    val otherUsername: String,
    val otherProfileImage: String?,
    var latestMessage: String
)
