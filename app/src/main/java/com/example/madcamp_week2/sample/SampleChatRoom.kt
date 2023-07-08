package com.example.madcamp_week2.sample

data class SampleChatRoom(
    val otherUser: SampleUser,
    val chatlog: ArrayList<SampleChatMessage>
)